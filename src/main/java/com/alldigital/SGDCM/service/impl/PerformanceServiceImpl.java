package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.*;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.repository.*;
import com.alldigital.SGDCM.service.PerformanceService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    @Autowired
    private IMoocRepository moocRepository;

    @Autowired
    private IUserMoocRepository userMoocRepository;

    @Autowired
    private IPerformanceRepository performanceRepository;

    @Autowired
    private ICutoffDateRepository cutoffDateRepository;

    @Autowired
    private IUserRepository userRepository;

    @Override
    public void processExcel(MultipartFile file) throws IOException {
        try(InputStream inputStream = file.getInputStream()){
            Workbook workbook = new XSSFWorkbook(inputStream);

            for (int i=0; i<workbook.getNumberOfSheets(); i++){
                Sheet sheet = workbook.getSheetAt(i);
                processSheet(sheet);
            }
            workbook.close();
        }
    }

    private void processSheet(Sheet sheet) {
        Iterator<Row> rowIterator = sheet.iterator();

        // Obtener el nombre del curso desde la segunda fila (fila 2, índice 1)
        Row nameMoocRow = sheet.getRow(1); // Segunda fila (fila 2)
        if (nameMoocRow == null) {
            System.out.println("La fila del nombre del curso está vacía. Saltando esta hoja.");
            return; // Salir del método si no hay fila
        }

        Cell nombreCursoCell = nameMoocRow.getCell(0); // Primera columna (columna 0)
        if (nombreCursoCell == null) {
            System.out.println("La celda del nombre del curso está vacía. Saltando esta hoja.");
            return; // Salir del método si no hay celda
        }

        String fullName = nombreCursoCell.getStringCellValue();
        String name = fullName.split("\\d{4}-\\d")[0].trim();

        // Obtener la fecha de corte
        String cutoffDateStr = getCourtDate(sheet);

        Optional<Mooc> moocOptional = moocRepository.findByNameContainingIgnoreCase(name.trim());

        // Si no se encuentra el MOOC, continuar con el siguiente curso
        if (!moocOptional.isPresent()) {
            System.out.println("Mooc no encontrado con nombre: " + name + ". Saltando esta hoja.");
            return;
        }
        Mooc mooc = moocOptional.get();

        // Parsear la fecha de corte
        Date fechaCorte = parseCutoffDate(cutoffDateStr);

        // Verificar si ya existe una fecha de corte para este MOOC
        Optional<CutoffDate> existingCutoffDate = cutoffDateRepository.findByMoooc_Id(mooc.getId());
        if (!existingCutoffDate.isPresent()) {
            // Guardar la fecha de corte solo si no existe
            CutoffDate fechaCorteEntity = new CutoffDate();
            fechaCorteEntity.setMoooc(mooc); // Asignar el MOOC correcto
            fechaCorteEntity.setCutoffDate(fechaCorte);
            cutoffDateRepository.save(fechaCorteEntity);
            System.out.println("Fecha de corte guardada para el MOOC: " + mooc.getId());
        } else {
            System.out.println("Ya existe una fecha de corte para el MOOC: " + mooc.getId());
        }

        // Saltar las filas de encabezado (primera fila es la fecha de corte, segunda fila es el nombre del curso, tercera fila es el encabezado de la tabla)
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        // Determinar el tipo de hoja (si tiene columnas separadas para matrícula y dominio)
        boolean tieneColumnasSeparadas = sheet.getRow(3).getPhysicalNumberOfCells() > 3;

        // Procesar las filas restantes
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            try {
                // Obtener los valores de las celdas
                Cell correoCell = row.getCell(0);
                Cell calificacionCell = tieneColumnasSeparadas ? row.getCell(3) : row.getCell(1);
                Cell cursoConcluidoCell = tieneColumnasSeparadas ? row.getCell(4) : row.getCell(2);

                // Verificar si las celdas están vacías o no son del tipo esperado
                if (correoCell == null || calificacionCell == null || cursoConcluidoCell == null) {
                    System.out.println("Una o más celdas están vacías en la fila " + row.getRowNum() + ". Saltando esta fila.");
                    continue;
                }

                String correo = correoCell.getStringCellValue();

                if (!correo.endsWith("@merida.tecnm.mx")) {
                    System.out.println("El correo " + correo + " no es de Mérida. Saltando esta fila.");
                    continue;
                }

                // Manejar el caso en que la celda de calificación sea de tipo STRING
                int calificacion;
                if (calificacionCell.getCellType() == CellType.STRING) {
                    try {
                        calificacion = Integer.parseInt(calificacionCell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        System.out.println("La calificación en la fila " + row.getRowNum() + " no es un número válido. Saltando esta fila.");
                        continue;
                    }
                } else if (calificacionCell.getCellType() == CellType.NUMERIC) {
                    calificacion = (int) calificacionCell.getNumericCellValue();
                } else {
                    System.out.println("Tipo de celda no soportado para la calificación en la fila " + row.getRowNum() + ". Saltando esta fila.");
                    continue;
                }

                String cursoConcluido = cursoConcluidoCell.getStringCellValue();

                // Obtener matrícula y dominio según el tipo de hoja
                String matricula;
                String dominio;
                if (tieneColumnasSeparadas) {
                    Cell matriculaCell = row.getCell(1);
                    Cell dominioCell = row.getCell(2);
                    if (matriculaCell == null || dominioCell == null) {
                        System.out.println("La matrícula o el dominio están vacíos en la fila " + row.getRowNum() + ". Saltando esta fila.");
                        continue;
                    }
                    matricula = matriculaCell.getStringCellValue();
                    dominio = dominioCell.getStringCellValue();
                } else {
                    String[] partesCorreo = correo.split("@");
                    matricula = partesCorreo[0];
                    dominio = partesCorreo[1];
                }

                // Buscar el usuario por matrícula
                Optional<User> userOptional = userRepository.findByMatricula(matricula);
                if (!userOptional.isPresent()) {
                    System.out.println("Usuario no encontrado con matrícula: " + matricula + ". Saltando esta fila.");
                    continue; // Continuar con la siguiente fila si no se encuentra el usuario
                }

                User user = userOptional.get();

                // Verificar la relación en UserMooc por id_usuario e id_mooc
                Optional<UserMooc> userMooc = userMoocRepository.findByUserIdAndMoocId(user.getId(), mooc.getId());

                if (userMooc.isPresent()) {
                    Performance desempeno = new Performance();
                    desempeno.setUsuarioMooc(userMooc.get());
                    desempeno.setQualification(calificacion);
                    desempeno.setStatus(cursoConcluido.charAt(0));
                    desempeno.setMatricula(matricula);
                    desempeno.setDomain(dominio);
                    performanceRepository.save(desempeno);
                } else {
                    System.out.println("No se encontró relación entre el usuario " + matricula + " y el MOOC " + mooc.getId() + ". Saltando esta fila.");
                }
            } catch (Exception e) {
                System.out.println("Error al procesar la fila " + row.getRowNum() + ": " + e.getMessage());
                continue; // Continuar con la siguiente fila en caso de error
            }
        }
    }

    private String getCourtDate(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        Cell fechaCorteCell = headerRow.getCell(0);
        return fechaCorteCell.getStringCellValue().replace("Fecha de Corte de Datos: ", "");
    }

    private Date parseCutoffDate(String fechaCorteStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            return sdf.parse(fechaCorteStr);
        } catch (ParseException e) {
            throw new RuntimeException("Formato de fecha de corte inválido: " + fechaCorteStr, e);
        }
    }
}

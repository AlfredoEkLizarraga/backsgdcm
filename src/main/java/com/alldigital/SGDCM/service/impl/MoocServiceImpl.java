package com.alldigital.SGDCM.service.impl;
import com.alldigital.SGDCM.entity.Mooc;
import com.alldigital.SGDCM.repository.IMoocRepository;
import com.alldigital.SGDCM.service.MoocService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MoocServiceImpl implements MoocService {

    private static final Logger logger = LoggerFactory.getLogger(MoocServiceImpl.class);

    @Autowired
    private IMoocRepository moocRepository;

    @Override
    public Mooc findOneById(Long id) {
        return moocRepository.findById(id).orElse(null);
    }

    /*Metodo para procesar un archivo PDF */
    public void processPdfMooc(MultipartFile file) throws IOException {
        try {
            logger.info("Cargando el archivo PDF...");
            PDDocument document = PDDocument.load(file.getInputStream());
            // Crear un objeto PDFTextStripper para extraer el texto
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);

            //cerrar el documento
            document.close();

            logger.info("Contenido del PDF:\n{}", text); // Imprime el contenido del PDF
            logger.info("Parseando el texto a objetos Mooc...");
            List<Mooc> moocs = parseTextToMoocs(text);
            logger.info("Número de moocs encontrados: {}", moocs.size());

            if (!moocs.isEmpty()){
                logger.info("Guardando los moocs en la base de datos...");
                moocRepository.saveAll(moocs);
                logger.info("Moocs guardados exitosamente.");
            }else{
                logger.warn("No se encontraron datos válidos en el PDF =(");
            }
        } catch (Exception e) {
            logger.error("Error al procesar el PDF: ", e);
            throw e;
        }
    }

    // metodo para agregar la información del PDF a una Tabla de la BD
    private List<Mooc> parseTextToMoocs(String text) {
        logger.info("Iniciando parseTextToMoocs...");
        List<Mooc> moocs = new ArrayList<>();
        String[] lines = text.split("\n");

        boolean insideTable = false;
        boolean headerProcessed = false;
        StringBuilder buffer = new StringBuilder();
        Pattern codePattern = Pattern.compile("TNM-M\\d+\\.\\d+-MOOC-\\d{4}-\\d{2}"); //detectar el patron de codigos


        // recorrer linea por linea el texto del pdf
        for (String line : lines) {
            line = line.trim(); // ELIMINA ESPACIOS AL INICIO Y AL FINAL
            logger.debug("Línea actual: {}", line);

            // Filtrar líneas irrelevantes
            if (line.contains("Av. Universidad") || line.contains("www.tecnm.mx") || line.contains("Tel.") || line.contains("e-mail:") || line.isEmpty()) {
                logger.debug("Línea filtrada: {}", line);
                continue; // Ignorar estas líneas
            }


            // Detectar el inicio de la tabla
            if (line.contains("Nombre del MOOC del")) {
                insideTable = true;
                logger.info("Inicio de tabla detectado :{}",line);
                continue;
            }

            // saltar los titulos de las filas
            if(line.contains("TecNM") || line.contains("Hrs") || line.contains("Periodo de") || line.contains("Impartición") || line.contains("Perfil del Curso") || line.contains("Codigo del curso")){
                continue;
            }

            // Manejar líneas dentro de la tabla
            if (insideTable) {
                buffer.append(line).append(" ");
                logger.debug("Procesando línea dentro de la tabla: {}", line);
                //verificar si la linea actual contiene el codigo de curso
                Matcher matcher  = codePattern.matcher(line);
                if (matcher.find()){
                    //procesar el buffer acumulado
                    processBuffer(buffer.toString(), moocs);
                    buffer.setLength(0); // Reiniciar buffer
                }

            }
        }

        // Procesar el último buffer si queda contenido
        if (buffer.length() > 0) {
            logger.debug("Procesando último buffer: {}", buffer);
            processBuffer(buffer.toString(), moocs);
        }

        logger.info("Total de moocs procesados: {}", moocs.size());
        return moocs;
    }

    //metodo para almacenar datos en memoria (de manera temporal)
    private void processBuffer(String bufferline, List<Mooc> moocs) {

        // Patrón para capturar: Nombre, Horas, Periodo, Perfil, Código
        Pattern pattern = Pattern.compile("(.+?)\\s+(\\d+)\\s+(E-J\\s+\\d{4})\\s+(Actualización\\s+Profesional|Formación\\s+Docente|.+?)\\s+(TNM-M\\d+\\.\\d+-MOOC-\\d{4}-\\d{2})");
        Matcher matcher = pattern.matcher(bufferline);

        if (matcher.find()){
            try{
                Mooc mooc = new Mooc();
                mooc.setName(matcher.group(1).trim());
                mooc.setHours(Integer.parseInt(matcher.group(2).trim()));
                mooc.setPeriod(matcher.group(3).trim());
                mooc.setProfile(matcher.group(4).trim());
                mooc.setCode(matcher.group(5).trim());
                moocs.add(mooc);// agregar los datos
                logger.info("Mooc procesado: {}", mooc);
            } catch (Exception e) {
                logger.error("Error al procesar línea: {}", bufferline, e);
            }
        }
        else{
            logger.warn("linea no valida {}",bufferline);
        }
    }

    @Override
    public List<Mooc> findAll() {
        return moocRepository.findAll();
    }

    @Override
    public List<Mooc> findByNameContaining(String name) {
        return moocRepository.findByNameContaining(name);
    }

    @Override
    public List<Mooc> findByPeriod(String period) {
        return moocRepository.findByPeriod(period);
    }

    @Override
    public List<Mooc> findAllByNameAndPeriod(String name, String period) {
        return moocRepository.findByNameContainingAndPeriod(name, period);
    }

    @Override
    @Transactional
    public Mooc saveCourse(Mooc mooc) {
        return moocRepository.save(mooc);
    }

    @Override
    @Transactional
    public Mooc updateOneById(Long id, Mooc mooc) {
        Mooc oldMooc = this.findOneById(id);
        oldMooc.setName(mooc.getName());
        oldMooc.setHours(mooc.getHours());
       oldMooc.setPeriod(mooc.getPeriod());
       oldMooc.setProfile(mooc.getProfile());
       oldMooc.setProfile(mooc.getProfile());
        return moocRepository.save(oldMooc);
    }

    @Override
    @Transactional
    public void deleteOneById(Long id) {
        moocRepository.deleteById(id);
    }
}

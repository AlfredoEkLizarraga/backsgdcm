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
    public void processPdfMooc(MultipartFile file) throws IOException {
        try {
            logger.info("Cargando el archivo PDF...");
            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
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
                logger.warn("No se encontraron datos válidos en el PDF.");
            }
        } catch (Exception e) {
            logger.error("Error al procesar el PDF: ", e);
            throw e;
        }
    }

    private List<Mooc> parseTextToMoocs(String text) {
        List<Mooc> moocs = new ArrayList<>();
        String[] lines = text.split("\n");

        boolean insideTable = false;

        for (String line : lines) {
            line = line.trim();

            if (line.contains("Nombre del MOOC del TecNM") && line.contains("Hrs") && line.contains("Período de Impartición") && line.contains("Perfil del Curso") && line.contains("Código del Curso")) {
                insideTable = true; // Entramos en la tabla
                continue; // Saltar la línea de encabezados
            }

            if (insideTable && line.matches(".*\\|.*\\|.*\\|.*\\|.*")) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    try {
                        Mooc mooc = new Mooc();

                        mooc.setName(parts[0].trim());

                        mooc.setHours(Integer.parseInt(parts[1].trim()));

                        mooc.setPeriod(parts[2].trim());

                        mooc.setProfile(parts[3].trim());

                        mooc.setCode(parts[4].trim());

                        moocs.add(mooc);

                        logger.info("Mooc procesado:\n" +
                                        "Nombre: {}\n" +
                                        "Horas: {}\n" +
                                        "Período: {}\n" +
                                        "Perfil: {}\n" +
                                        "Código: {}\n",
                                mooc.getName(), mooc.getHours(), mooc.getPeriod(), mooc.getProfile(), mooc.getCode());
                    } catch (NumberFormatException e) {
                        logger.warn("Error al convertir horas: {}", line);
                    }
                }
            }

            if (line.isEmpty()) {
                insideTable = false; // Salimos de la tabla
            }
        }

        if (moocs.isEmpty()) {
            logger.warn("No se encontraron datos válidos en el PDF");
        } else {
            logger.info("Número de moocs: {}", moocs.size());
        }

        return moocs;
    }

    @Override
    public Mooc findOneById(Long id) {
        return moocRepository.findById(id).get();
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

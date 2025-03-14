package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.Mooc;
import com.alldigital.SGDCM.entity.User;
import com.alldigital.SGDCM.entity.UserMooc;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.repository.IMoocRepository;
import com.alldigital.SGDCM.repository.IUserMoocRepository;
import com.alldigital.SGDCM.repository.IUserRepository;
import com.alldigital.SGDCM.service.MoocService;
import com.alldigital.SGDCM.service.UserService;
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

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IUserMoocRepository userMoocRepository;

    @Override
    @Transactional
    public void processPdfMooc(MultipartFile file) throws IOException {
        logger.info("Iniciando procesamiento del PDF...");

        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfStripper = new PDFTextStripper();
        String text = pdfStripper.getText(document);
        document.close();

        logger.info("Contenido del PDF: {}", text);

        List<Mooc> moocs = parseTextToMoocs(text);
        logger.info("Número de MOOCs procesados: {}", moocs.size());

        moocRepository.saveAll(moocs);
        logger.info("Datos guardados en la base de datos.");
    }

    private List<Mooc> parseTextToMoocs(String text) {
        List<Mooc> moocs = new ArrayList<>();
        String[] lines = text.split("\n");

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 1) {
                Mooc mooc = new Mooc();
                mooc.setName(parts[1].trim());
                mooc.setHours(Integer.parseInt(parts[2].trim()));
                mooc.setPeriod(parts[3].trim());
                mooc.setProfile(parts[4].trim());
                mooc.setCode(parts[5].trim());
                moocs.add(mooc);
            }
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

    @Override
    @Transactional
    public void assignUserToMooc(String matricula, Long idMooc) {
        User user = userRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con matrícula: " + matricula));
        Mooc mooc = moocRepository.findById(idMooc)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado con id: " + idMooc));

        boolean existsRelation = userMoocRepository.existsByUserAndMooc(user, mooc);

        if (!existsRelation){
            UserMooc userMooc = new UserMooc();
            userMooc.setUser(user);
            userMooc.setMooc(mooc);

            userMoocRepository.save(userMooc);
        }else {
            throw new NotFoundException("El usuario ya esta asignado al curso");
        }
    }
}

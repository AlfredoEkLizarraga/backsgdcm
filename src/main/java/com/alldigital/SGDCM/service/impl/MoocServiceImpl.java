package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.Mooc;
import com.alldigital.SGDCM.repository.IMoocRepository;
import com.alldigital.SGDCM.service.MoocService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MoocServiceImpl implements MoocService {

    @Autowired
    private IMoocRepository moocRepository;

    @Override
    public void processPdfMooc(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        String text = pdfTextStripper.getText(document);
        document.close();

        List<Mooc> mooc = parseTextToMoocs(text);
        moocRepository.saveAll(mooc);
    }

    private List<Mooc> parseTextToMoocs(String text) {
        List<Mooc> moocs = new ArrayList<>();
        String[] lines = text.split("\n");

        for (String line : lines) {
            String[] parts = line.split("\\|");
            if (parts.length > 1) {
                Mooc mooc = new Mooc();
                mooc.setName(parts[1].trim());
//                mooc.setHoras(Integer.parseInt(parts[2].trim()));
//                mooc.setPeriodo(parts[3].trim());
//                mooc.setPerfil(parts[4].trim());
//                mooc.setCodigo(parts[5].trim());
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
    public List<Mooc> findByCutoffDate(LocalDate cutoffDate) {
        return moocRepository.findByCutoffDate(cutoffDate);
    }

    @Override
    public List<Mooc> findAllByNameAndPeriodAndCutoffDate(String name, String period, LocalDate cutoffDate) {
        return moocRepository.findByNameContainingAndPeriodAndCutoffDate(name, period, cutoffDate);
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
        oldMooc.setMinQualification(mooc.getMinQualification());
        oldMooc.setCutoffDate(mooc.getCutoffDate());
        oldMooc.setPeriod(mooc.getPeriod());
        oldMooc.setDescription(mooc.getDescription());
        return moocRepository.save(oldMooc);
    }

    @Override
    @Transactional
    public void deleteOneById(Long id) {
        moocRepository.deleteById(id);
    }
}

package com.alldigital.SGDCM.service;

import com.alldigital.SGDCM.entity.Mooc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MoocService {

    void processPdfMooc(MultipartFile file) throws IOException;

    Mooc findOneById(Long id);

    List<Mooc> findAll();

    List<Mooc> findByNameContaining(String name);

    List<Mooc> findByPeriod(String period);

    List<Mooc> findAllByNameAndPeriod(String name, String period);

    Mooc saveCourse(Mooc mooc);

    Mooc updateOneById(Long id, Mooc mooc);

    void deleteOneById(Long id);

    void assignUserToMooc(String matricula, Long idMooc);
}

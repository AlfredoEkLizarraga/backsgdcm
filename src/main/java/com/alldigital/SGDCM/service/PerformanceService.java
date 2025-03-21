package com.alldigital.SGDCM.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PerformanceService {
    void processExcel(MultipartFile file) throws IOException;
}

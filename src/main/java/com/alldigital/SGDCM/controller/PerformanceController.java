package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/desempeno")
public class PerformanceController {

    @Autowired
    private PerformanceService performanceService;

    @PostMapping("/cargar-excel")
    public void cargarExcel(@RequestParam("file") MultipartFile file) throws IOException {
        performanceService.processExcel(file);
    }
}

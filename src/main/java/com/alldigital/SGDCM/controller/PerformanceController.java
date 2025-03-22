package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> cargarExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try{
            performanceService.processExcel(file);
            return ResponseEntity.badRequest().body("El archivo Excel se procesó correctamente.");
        }catch (IOException ex){
            return ResponseEntity.badRequest().body("Error al procesar el archivo: "+ex.getMessage());
        }catch (NotFoundException ex){
            return ResponseEntity.status(400).body(ex.getMessage());
        }
    }
}

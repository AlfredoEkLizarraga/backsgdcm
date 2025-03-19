package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.entity.Mooc;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.service.MoocService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/moocs")
public class MoocController {

    private static final Logger logger = LoggerFactory.getLogger(MoocController.class);

    @Autowired
    private MoocService moocService;

    @GetMapping("/lista")
    public ResponseEntity<List<Mooc>> getAllCourses(){
        var courses = moocService.findAll();
        if (courses.isEmpty()){
            throw new NotFoundException("No se encontraron cursos disponibles");
        }
        courses.forEach((course -> logger.info(course.toString())));
        return ResponseEntity.ok(courses);
    }

    @GetMapping
    public ResponseEntity<List<Mooc>> findAllByName(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String period){ //al periodo le agregamos un required
        List<Mooc> mooc = null;

        if (StringUtils.hasText(name) && period != null){
            mooc = moocService.findAllByNameAndPeriod(name, period);
            if (mooc.isEmpty()){
                throw new NotFoundException("No se encontraron cursos de"+name+ " en el perido "+period);
            }

        }else if(StringUtils.hasText(name)){
            mooc = moocService.findByNameContaining(name);
            if (mooc.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos de '" + name + "'.");
            }
        } else if (period != null) {
            mooc = moocService.findByPeriod(period);
            if (mooc.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos en el período '" + period + "'.");
            }
        }
        return ResponseEntity.ok(mooc);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUploadMooc(@RequestParam("file") MultipartFile file){
        //validar que el archivo sea un PDF
        if (!file.getContentType().equals("application/pdf")) {
            return ResponseEntity.badRequest().body("Solo se permiten archivos PDF.");
        }

        try {
            moocService.processPdfMooc(file);
            return ResponseEntity.ok("File uploaded and processed successfully. =)");
        }catch (IOException e){
            logger.error("Error processing the file: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + e.getMessage());
        }catch (Exception e) {
            logger.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createOneCourse(@RequestBody Mooc newMooc, HttpServletRequest request){
        try{
            Mooc createdMooc = moocService.saveCourse(newMooc);
            String urlBase = request.getRequestURI().toString();
            URI newLocation = URI.create(urlBase + "/" + createdMooc.getId());
            return ResponseEntity.created(newLocation).body(createdMooc);
        }catch (NotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Mooc> updateCourse(@PathVariable Long id, @RequestBody Mooc mooc){
        try {
            Mooc updatedMooc = moocService.updateOneById(id, mooc);
            return ResponseEntity.ok().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable Long id){
        try {
            moocService.deleteOneById(id);
            return ResponseEntity.noContent().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

}

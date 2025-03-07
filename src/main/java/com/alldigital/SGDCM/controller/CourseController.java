package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.entity.Course;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.service.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping("/lista")
    public List<Course> getAllCourses(){
        var courses = courseService.findAll();
        if (courses.isEmpty()){
            throw new NotFoundException("No se encontraron cursos disponibles");
        }
        courses.forEach((course -> logger.info(course.toString())));
        return courses;
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAllByName(@RequestParam(required = false) String name, @RequestParam String period, @RequestParam(required = false) LocalDate cutoffDate){
        List<Course> course = null;

        if (StringUtils.hasText(name) && period != null && cutoffDate != null){
            course = courseService.findAllByNameAndPeriodAndCutoffDate(name, period, cutoffDate);
            if (course.isEmpty()){
                throw new NotFoundException("No se encontraron cursos de"+name+ " en el perido "+period);
            }
        }else if(StringUtils.hasText(name)){
            course = courseService.findByNameContaining(name);
            if (course.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos de '" + name + "'.");
            }
        } else if (period != null) {
            course = courseService.findByPeriod(period);
            if (course.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos en el período '" + period + "'.");
            }
        } else if (cutoffDate != null) {
            course = courseService.findByCutoffDate(cutoffDate);
        }
        return ResponseEntity.ok(course);
    }

    @PostMapping
    public ResponseEntity<?> createOneCourse(@RequestBody Course newCourse, HttpServletRequest request){
        try{
            Course createdCourse = courseService.saveCourse(newCourse);
            String urlBase = request.getRequestURI().toString();
            URI newLocation = URI.create(urlBase + "/" +createdCourse.getId());
            return ResponseEntity.created(newLocation).body(createdCourse);
        }catch (NotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course){
        try {
            Course updatedCourse = courseService.updateOneById(id, course);
            return ResponseEntity.ok().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCourseById(@PathVariable Long id){
        try {
            courseService.deleteOneById(id);
            return ResponseEntity.noContent().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

}

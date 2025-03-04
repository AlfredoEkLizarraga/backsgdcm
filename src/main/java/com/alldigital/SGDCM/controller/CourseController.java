package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.entity.Course;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private CourseService courseService;

    @GetMapping("/lista")
    public List<Course> getCourses(){
        var courses = courseService.findAll();
        if (courses.isEmpty()){
            throw new NotFoundException("No se encontraron cursos disponibles");
        }
        courses.forEach((course -> logger.info(course.toString())));
        return courses;
    }

    @GetMapping
    public ResponseEntity<List<Course>> findAllByName(@RequestParam(required = false) String name, @RequestParam String period){
        List<Course> course = null;

        if (StringUtils.hasText(name) && period != null){
            course = courseService.findAllByNameAndPeriod(name, period);
            if (course.isEmpty()){
                throw new NotFoundException("No se encontraron cursos de"+name+ " en el perido "+period);
            }
        }else if(StringUtils.hasText(name)){
            course = courseService.findAllByName(name);
            if (course.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos de '" + name + "'.");
            }
        } else if (period != null) {
            course = courseService.findByPeriod(period);
            if (course.isEmpty()) {
                throw new NotFoundException("No se encontraron cursos en el período '" + period + "'.");
            }
        }
        return ResponseEntity.ok(course);
    }
}

package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.entity.Course;
import com.alldigital.SGDCM.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        courses.forEach((empleado -> logger.info(empleado.toString())));
        return courses;
    }

}

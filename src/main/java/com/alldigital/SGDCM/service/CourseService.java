package com.alldigital.SGDCM.service;

import com.alldigital.SGDCM.entity.Course;

import java.util.List;

public interface CourseService {

    Course findOneById(Long id);

    List<Course> findAll();

    List<Course> findAllByName(String name);

    Course saveCourse(Course course);


    void deleteOneById(Long id);
}

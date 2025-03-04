package com.alldigital.SGDCM.service;

import com.alldigital.SGDCM.entity.Course;

import java.util.List;

public interface CourseService {

    Course findOneById(Long id);

    List<Course> findAll();

    List<Course> findAllByName(String name);

    List<Course> findByPeriod(String period);

    List<Course> findAllByNameAndPeriod(String name, String period);

    Course saveCourse(Course course);


    void deleteOneById(Long id);
}

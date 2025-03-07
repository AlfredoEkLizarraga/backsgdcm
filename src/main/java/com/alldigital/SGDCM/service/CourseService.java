package com.alldigital.SGDCM.service;

import com.alldigital.SGDCM.entity.Course;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {

    Course findOneById(Long id);

    List<Course> findAll();

    List<Course> findByNameContaining(String name);

    List<Course> findByPeriod(String period);

    List<Course> findByCutoffDate(LocalDate cutoffDate);

    List<Course> findAllByNameAndPeriodAndCutoffDate(String name, String period, LocalDate cutoffDate);

    Course saveCourse(Course course);

    Course updateOneById(Long id, Course course);

    void deleteOneById(Long id);
}

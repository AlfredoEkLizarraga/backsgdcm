package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.Course;
import com.alldigital.SGDCM.repository.ICourseRepository;
import com.alldigital.SGDCM.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private ICourseRepository courseRepository;

    @Override
    public Course findOneById(Long id) {
        return courseRepository.findById(id).get();
    }

    @Override
    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> findAllByName(String name) {
        return courseRepository.findByName(name);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }



    @Override
    public void deleteOneById(Long id) {
        courseRepository.deleteById(id);
    }
}

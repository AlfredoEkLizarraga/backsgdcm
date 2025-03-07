package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.Course;
import com.alldigital.SGDCM.entity.User;
import com.alldigital.SGDCM.repository.ICourseRepository;
import com.alldigital.SGDCM.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<Course> findByNameContaining(String name) {
        return courseRepository.findByNameContaining(name);
    }

    @Override
    public List<Course> findByPeriod(String period) {
        return courseRepository.findByPeriod(period);
    }

    @Override
    public List<Course> findByCutoffDate(LocalDate cutoffDate) {
        return courseRepository.findByCutoffDate(cutoffDate);
    }

    @Override
    public List<Course> findAllByNameAndPeriodAndCutoffDate(String name, String period, LocalDate cutoffDate) {
        return courseRepository.findByNameContainingAndPeriodAndCutoffDate(name, period, cutoffDate);
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateOneById(Long id, Course course) {
        Course oldCourse = this.findOneById(id);
        oldCourse.setName(course.getName());
        oldCourse.setMinQualification(course.getMinQualification());
        oldCourse.setCutoffDate(course.getCutoffDate());
        oldCourse.setPeriod(course.getPeriod());
        oldCourse.setDescription(course.getDescription());
        return courseRepository.save(oldCourse);
    }


    @Override
    public void deleteOneById(Long id) {
        courseRepository.deleteById(id);
    }
}

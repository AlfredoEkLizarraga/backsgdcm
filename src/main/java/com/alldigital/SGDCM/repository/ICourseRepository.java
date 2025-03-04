package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);
}

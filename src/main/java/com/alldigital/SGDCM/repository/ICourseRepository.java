package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ICourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByNameContaining(String name);

    List<Course> findByPeriod(String period);

    List<Course> findByCutoffDate(LocalDate cutoffDate);

    List<Course> findByNameContainingAndPeriodAndCutoffDate(String name, String period, LocalDate cutoffDate);
}

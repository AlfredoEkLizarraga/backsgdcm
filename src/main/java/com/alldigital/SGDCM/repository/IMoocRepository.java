package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.Mooc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IMoocRepository extends JpaRepository<Mooc, Long> {

    Optional<Mooc> findByNameContaining(String name);

    List<Mooc> findByPeriod(String period);

    //List<Mooc> findByCutoffDate(LocalDate cutoffDate);

    List<Mooc> findByNameContainingAndPeriod(String name, String period);

    Optional<Mooc> findByNameContainingIgnoreCase(String name);
}

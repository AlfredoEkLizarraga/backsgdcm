package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.CutoffDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface ICutoffDateRepository extends JpaRepository<CutoffDate, Long> {

    Optional<CutoffDate> findByMoooc_IdAndCutoffDate(Long moocId, Date cutoffDate);

    Optional<CutoffDate> findByMoooc_Id(Long id);
}

package com.alldigital.SGDCM.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cursos")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_curso")
     Long id;

    @Column(name = "nombre_curso", nullable = false)
     String name;

    @Column(name = "calificacion_min", nullable = false)
     int minQualification;

    @Column(name = "fecha_corte", nullable = false)
     LocalDate cutoffDate;

    @Column(name = "periodo", nullable = false)
     String period;

    @Column(name = "descripcion")
     String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinQualification() {
        return minQualification;
    }

    public void setMinQualification(int minQualification) {
        this.minQualification = minQualification;
    }

    public LocalDate getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(LocalDate cutoffDate) {
        this.cutoffDate = cutoffDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

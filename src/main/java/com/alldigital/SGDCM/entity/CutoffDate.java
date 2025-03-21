package com.alldigital.SGDCM.entity;

import jakarta.persistence.*;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "fecha_corte")
public class CutoffDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fechaCorte")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_mooc")
    private Mooc moooc;

    @Column(name = "fecha_corte")
    private Date cutoffDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mooc getMoooc() {
        return moooc;
    }

    public void setMoooc(Mooc moooc) {
        this.moooc = moooc;
    }

    public Date getCutoffDate() {
        return cutoffDate;
    }

    public void setCutoffDate(Date cutoffDate) {
        this.cutoffDate = cutoffDate;
    }
}

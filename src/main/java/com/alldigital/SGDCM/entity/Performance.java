package com.alldigital.SGDCM.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "desempeno")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_desempeno")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario_mooc", nullable = false)
    private UserMooc usuarioMooc;  // Relación ManyToOne con UserMooc

    @Column(name = "calificacion")
    private int qualification;

    private Character status;

    private String matricula;

    @Column(name = "dominio")
    private String domain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserMooc getUsuarioMooc() {
        return usuarioMooc;
    }

    public void setUsuarioMooc(UserMooc usuarioMooc) {
        this.usuarioMooc = usuarioMooc;
    }

    public int getQualification() {
        return qualification;
    }

    public void setQualification(int qualification) {
        this.qualification = qualification;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}

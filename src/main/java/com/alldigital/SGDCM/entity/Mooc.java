package com.alldigital.SGDCM.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "moocs")
public class Mooc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_mooc")
    private Long id;

    @Column(name = "nombre_curso")
    private String name;

    @Column(name = "horas")
    private int hours;

    @Column(name = "periodo")
    private String period;

    @Column(name = "perfil")
    private String profile;

    @Column(name = "codigo")
    private String code;

    @OneToMany(mappedBy = "mooc", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserMooc> users;

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

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserMooc> getUsers() {
        return users;
    }

    public void setUsers(List<UserMooc> users) {
        this.users = users;
    }
}

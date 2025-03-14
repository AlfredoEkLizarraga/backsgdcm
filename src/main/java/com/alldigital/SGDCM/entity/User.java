package com.alldigital.SGDCM.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_usuario")
    private Long id;

    @Column(name = "nombre_usuario", nullable = false)
    private String name;

    @Column(name = "apellido_pat", nullable = false)
    private String paternalSurname;

    @Column(name = "apellido_mat", nullable = false)
    private String maternalSurname;

    @Column(name = "correo_inst", nullable = false, unique = true)
    private String email;

    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;

    @Column(name = "telefono", nullable = false)
    private String phone;

    @Column(name = "password", nullable = false, unique = true)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMooc> moocs;

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

    public String getPaternalSurname() {
        return paternalSurname;
    }

    public void setPaternalSurname(String paternalSurname) {
        this.paternalSurname = paternalSurname;
    }

    public String getMaternalSurname() {
        return maternalSurname;
    }

    public void setMaternalSurname(String maternalSurname) {
        this.maternalSurname = maternalSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<UserMooc> getMoocs() {
        return moocs;
    }

    public void setMoocs(List<UserMooc> moocs) {
        this.moocs = moocs;
    }
}

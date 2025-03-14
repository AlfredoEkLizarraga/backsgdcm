package com.alldigital.SGDCM.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarioMooc", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_usuario", "id_mooc"})})
public class UserMooc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario-mooc")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_mooc", nullable = false)
    private Mooc mooc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Mooc getMooc() {
        return mooc;
    }

    public void setMooc(Mooc mooc) {
        this.mooc = mooc;
    }
}

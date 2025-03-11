package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    //User findByName(String name);

    boolean existsByMatricula(String matricula);

    @Modifying
    int deleteOneByMatricula(String matricula);

    Optional<User> findByMatricula(String matricula);
}

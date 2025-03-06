package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findByName(String name);
}

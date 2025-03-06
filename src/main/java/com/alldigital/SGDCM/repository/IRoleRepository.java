package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

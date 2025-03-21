package com.alldigital.SGDCM.repository;

import com.alldigital.SGDCM.entity.Mooc;
import com.alldigital.SGDCM.entity.User;
import com.alldigital.SGDCM.entity.UserMooc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserMoocRepository extends JpaRepository<UserMooc, Long> {

    boolean existsByUserAndMooc(User user, Mooc mooc);

    Optional<UserMooc> findByUserIdAndMoocId(Long idUser, Long idMooc);


}

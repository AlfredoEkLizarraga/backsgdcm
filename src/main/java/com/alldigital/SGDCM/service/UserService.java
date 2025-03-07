package com.alldigital.SGDCM.service;

import com.alldigital.SGDCM.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User findOneById(Long id);

    User saveUserWithRole(User user, String rolName);

    User updateOneById(Long id, User user);

    //void deleteOneById(Long id);

    void deleteByMatricula(String matricula);
}

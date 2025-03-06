package com.alldigital.SGDCM.service.impl;

import com.alldigital.SGDCM.entity.Role;
import com.alldigital.SGDCM.entity.User;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.repository.IRoleRepository;
import com.alldigital.SGDCM.repository.IUserRepository;
import com.alldigital.SGDCM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public User saveUserWithRole(User user, String rolName) {
        Role role = roleRepository.findByName(rolName);
        if (role == null){
            throw new NotFoundException("Rol no encontrado para "+rolName);
        }
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User updateOneById(Long id, User user) {
        User oldUser = this.findOneById(id);
        oldUser.setName(user.getName());
        oldUser.setPaternalSurname(user.getPaternalSurname());
        oldUser.setMaternalSurname(user.getMaternalSurname());
        oldUser.setEmail(user.getEmail());
        oldUser.setMatricula(user.getMatricula());
        oldUser.setPhone(user.getPhone());

        return userRepository.save(oldUser);
    }

    @Override
    public void deleteOneById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findOneById(Long id) {
        return userRepository.findById(id).get();
    }
}

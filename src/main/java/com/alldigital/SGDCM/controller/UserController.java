package com.alldigital.SGDCM.controller;

import com.alldigital.SGDCM.entity.User;
import com.alldigital.SGDCM.exception.NotFoundException;
import com.alldigital.SGDCM.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        var users = userService.getAllUsers();
        if (users.isEmpty()){
            throw new NotFoundException("No se encontraron usuarios disponibles");
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/{matricula}")
    public ResponseEntity<User> findOneByMatricula(@PathVariable String matricula){
        try {
            return ResponseEntity.ok(userService.findOneByMatricula(matricula));
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createOneUser(@RequestBody User newUser, HttpServletRequest request){
        try{
            String rolName = newUser.getRole().getName();

            User userCreated = userService.saveUserWithRole(newUser, rolName);

            String baseUrl = request.getRequestURL().toString();
            URI newLocation = URI.create(baseUrl + "/"+userCreated.getId());

            return ResponseEntity.created(newLocation).body(userCreated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user){
        try {
            User updateUser = userService.updateOneById(id, user);
            return ResponseEntity.ok().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{matricula}")
    public ResponseEntity<Void> deleteOneByMatricula(@PathVariable String matricula){
        try{
            userService.deleteByMatricula(matricula);
            return ResponseEntity.noContent().build();
        }catch (NotFoundException ex){
            return ResponseEntity.notFound().build();
        }
    }
}

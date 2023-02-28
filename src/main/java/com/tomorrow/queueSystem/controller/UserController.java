package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/userManagement")
public class UserController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/allUsers")
    public List<User> findAll() {
        return userService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/user/{userId}")
    public User findById(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody User user){
        String encryptedPassword =  passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userService.save(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @DeleteMapping("/user")
    public ResponseEntity deleteUser(@RequestBody User user){
        userService.delete(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PostMapping("/firstUser")
    public ResponseEntity addFirstUser(@RequestBody User user){
        addUser(user);
        return new ResponseEntity(user, HttpStatus.OK);
    }

}

package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    public void save(User user) {
        userRepository.save(user);
    }

    public User findByName(String userName){
        Optional<User> user = userRepository.findByName(userName);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

}
package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.Role;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.repository.RoleRepository;
import com.tomorrow.queueSystem.repository.UserRepository;
import com.tomorrow.queueSystem.utility.JobExecutorEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public void save(User user) {
        Set<Role> roleSet = new HashSet<>();
        Set<Role> userRoles = user.getRoles();
        if(!userRoles.isEmpty()){
            for(Role userRole:userRoles){
                Optional<Role> roleByName = roleRepository.findByName(userRole.getName());
                if(roleByName.isPresent()){
                    roleSet.add(roleByName.get());
                }else {
                    roleSet.add(new Role(userRole.getName()));
                }
            }
            user.setRoles(roleSet);
        }
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

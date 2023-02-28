package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.Role;
import com.tomorrow.queueSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RoleRepository roleRepository;

    public void save(Role role) {
        roleRepository.save(role);
    }

    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void delete(Role role) {
        roleRepository.delete(role);
    }
}

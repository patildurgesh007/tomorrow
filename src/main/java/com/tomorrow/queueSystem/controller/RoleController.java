package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.Role;
import com.tomorrow.queueSystem.security.IAuthenticationFacade;
import com.tomorrow.queueSystem.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roleManagement")
public class RoleController {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/role")
    public ResponseEntity save(@RequestBody Role role) {
        roleService.save(role);
        return new ResponseEntity(role, HttpStatus.OK);
    }

    @GetMapping("/role/{roleId}")
    public Role findById(@PathVariable Long roleId) {
        return roleService.findById(roleId);
    }

    @GetMapping("/allRoles")
    public List<Role> findAll() {
        return roleService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @DeleteMapping("/role")
    public void delete(@RequestBody Role role) {
        roleService.delete(role);
    }
}

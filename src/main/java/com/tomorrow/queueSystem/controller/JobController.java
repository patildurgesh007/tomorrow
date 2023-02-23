package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.repository.JobRepository;
import com.tomorrow.queueSystem.repository.UserRepository;
import com.tomorrow.queueSystem.service.IAuthenticationFacade;
import com.tomorrow.queueSystem.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobManagement")
public class JobController {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobService jobService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/job")
    public void save(Job job) {
        jobService.save(job);
    }

    @GetMapping("/job/{jobId}")
    public Job findById(Long jobId) {
        return jobService.findById(jobId);
    }

    @GetMapping("/allJobs")
    public List<Job> findAll() {
        return jobService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @DeleteMapping("/job")
    public void delete(Job job) {
        jobService.delete(job);
    }
}

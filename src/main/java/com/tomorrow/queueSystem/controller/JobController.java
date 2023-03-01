package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.security.IAuthenticationFacade;
import com.tomorrow.queueSystem.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity save(@RequestBody @Valid Job job) {
        jobService.save(job);
        return new ResponseEntity(job, HttpStatus.OK);
    }

    @GetMapping("/job/{jobId}")
    public Job findById(@PathVariable Long jobId) {
        return jobService.findById(jobId);
    }

    @GetMapping("/allJobs")
    public List<Job> findAll() {
        return jobService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @DeleteMapping("/job")
    public void delete(@RequestBody Job job) {
        jobService.delete(job);
    }
}

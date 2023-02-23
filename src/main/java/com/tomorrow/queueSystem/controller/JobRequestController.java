package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.service.IAuthenticationFacade;
import com.tomorrow.queueSystem.service.JobRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobRequest")
public class JobRequestController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobRequestService jobRequestService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping(value = "/username")
    public String currentUserNameSimple() {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @PostMapping("/jobRequest")
    public void save(JobRequest jobRequest) {
        jobRequestService.save(jobRequest);
    }

    @GetMapping("/jobRequest/{jobId}")
    public ResponseEntity findById(Long requestId) {
        JobRequest jobRequest = jobRequestService.findById(requestId);
        if(jobRequest != null){
            if(jobRequestService.isAuthorizedUser(jobRequest)){
                return new ResponseEntity(jobRequest, HttpStatus.OK);
            }
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @GetMapping("/allJobRequests")
    public List<JobRequest> findAll() {
        return jobRequestService.findAll();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER')")
    @DeleteMapping("/jobRequest")
    public void delete(JobRequest jobRequest) {
        jobRequestService.delete(jobRequest);
    }
}

package com.tomorrow.queueSystem.controller;

import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.security.IAuthenticationFacade;
import com.tomorrow.queueSystem.service.JobRequestService;
import com.tomorrow.queueSystem.utility.Constants;
import com.tomorrow.queueSystem.utility.RoleEnum;
import com.tomorrow.queueSystem.utility.UtilsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/jobRequest")
    public ResponseEntity save(@RequestBody @Valid JobRequest jobRequest) {
        String validationStatus = jobRequestService.isValidJobRequest(jobRequest);
        if(Constants.VALID_REQUEST.equals(validationStatus)){
            jobRequestService.fillDefaultDetails(jobRequest);
            jobRequestService.save(jobRequest);
            jobRequestService.scheduleJob(jobRequest);
            return new ResponseEntity(jobRequest, HttpStatus.OK);
        }
        return new ResponseEntity(validationStatus,HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/jobRequestList")
    public ResponseEntity saveJobRequestList(@RequestBody List<JobRequest> jobRequestList) {
        for(JobRequest jobRequest : jobRequestList) {
            String validationStatus = jobRequestService.isValidJobRequest(jobRequest);
            if(!Constants.VALID_REQUEST.equals(validationStatus)){
                return new ResponseEntity(validationStatus,HttpStatus.BAD_REQUEST);
            }
        }
        for(JobRequest jobRequest : jobRequestList) {
            jobRequestService.fillDefaultDetails(jobRequest);
            jobRequestService.save(jobRequest);
        }
        jobRequestService.scheduleJob(jobRequestList);
        return new ResponseEntity(jobRequestList, HttpStatus.OK);
    }

    @GetMapping("/jobRequest/{jobId}")
    public ResponseEntity findById(@PathVariable  Long requestId) {
        JobRequest jobRequest = jobRequestService.findById(requestId);
        if(jobRequest != null){
            if(jobRequestService.isAuthorizedUser(jobRequest)){
                return new ResponseEntity(jobRequest, HttpStatus.OK);
            }
            return new ResponseEntity(Constants.NOT_AUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
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
    public void delete(@RequestBody JobRequest jobRequest) {
        jobRequestService.delete(jobRequest);
    }

    @PostMapping("/terminate")
    public ResponseEntity terminateJobRequest(@RequestBody @Valid JobRequest jobRequest) {
        if (jobRequest != null) {
            if (jobRequestService.isAuthorizedUser(jobRequest) || UtilsImpl.isCurrentUserRoleWithin(jobRequestService.getCurrentUser(), RoleEnum.ROLE_ADMIN,RoleEnum.ROLE_MANAGER)) {
               boolean success =  jobRequestService.terminateJob(jobRequest);
               if(success){
                   return new ResponseEntity(jobRequest, HttpStatus.OK);
               }
               return new ResponseEntity(jobRequest,HttpStatus.NOT_MODIFIED);
            }
            return new ResponseEntity(Constants.NOT_AUTHORIZED_USER, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}

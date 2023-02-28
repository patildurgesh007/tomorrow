package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.repository.JobRequestRepository;
import com.tomorrow.queueSystem.security.IAuthenticationFacade;
import com.tomorrow.queueSystem.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class JobRequestService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JobService jobService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    PriorityJobSchedulerService priorityJobSchedulerService;

    ExecutableJob executableJob;

    public void save(JobRequest jobRequest) {
        jobRequestRepository.save(jobRequest);
    }

    public JobRequest findById(Long id) {
        return jobRequestRepository.findById(id).orElse(null);
    }

    public List<JobRequest> findAll() {
        return jobRequestRepository.findAll();
    }

    public void delete(JobRequest jobRequest) {
        jobRequestRepository.delete(jobRequest);
    }

    public boolean isAuthorizedUser(JobRequest jobRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        String userName = authentication.getName();
        User currentUser = getCurrentUser();
        if(currentUser != null){
            if(userName.equalsIgnoreCase(currentUser.getName())){
                return true;
            }
        }
        return false;
    }

    public String isValidJobRequest(JobRequest jobRequest) {
        User currentUser = getCurrentUser();
        if(currentUser == null){
            return Constants.USER_NOT_EXISTS;
        }
        Job job = jobService.findById(jobRequest.getJobId());
        if(job == null){
            return Constants.JOB_NOT_EXISTS;
        }
        if(jobRequest.getPriority()!= null && Priority.HIGH.equals(jobRequest.getPriority())){
            if(!UtilsImpl.isCurrentUserRoleWithin(currentUser, RoleEnum.ROLE_ADMIN,RoleEnum.ROLE_MANAGER)){
                return Constants.PRIORITY_AUTHORIZATION_ISSUE;
            }
        }
        jobRequest.setUserId(currentUser.getUserId());
        return Constants.VALID_REQUEST;
    }

    public User getCurrentUser(){
        Authentication authentication = authenticationFacade.getAuthentication();
        String userName = authentication.getName();
        return userService.findByName(userName);
    }

    public void fillDefaultDetails(JobRequest jobRequest) {
        jobRequest.setJobRequestDescription("");
        jobRequest.setCreationDate(Date.from(Instant.now()));
        jobRequest.setStatus(Status.CREATE);
        if(jobRequest.getPriority() == null) {
            jobRequest.setPriority(Priority.LOW);
        }
    }

    public void scheduleJob(JobRequest jobRequest){
        executableJob = context.getBean(ExecutableJob.class);
        executableJob.setJobRequest(jobRequest);
        executableJob.setPriority(jobRequest.getPriority());
        priorityJobSchedulerService = context.getBean(PriorityJobSchedulerService.class);
        priorityJobSchedulerService.scheduleJob(executableJob);
    }

    public void scheduleJob(List<JobRequest> jobRequestList){
        priorityJobSchedulerService = context.getBean(PriorityJobSchedulerService.class);
        List<ExecutableJob> executableJobList = new ArrayList<>();
        for(JobRequest jobRequest:jobRequestList){
            executableJob = context.getBean(ExecutableJob.class);
            executableJob.setJobRequest(jobRequest);
            executableJob.setPriority(jobRequest.getPriority());
            executableJobList.add(executableJob);
        }
        if(!executableJobList.isEmpty()){
            priorityJobSchedulerService.scheduleJobs(executableJobList);
        }
    }
}

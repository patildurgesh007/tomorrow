package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.repository.JobRepository;
import com.tomorrow.queueSystem.repository.JobRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobRequestService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobRequestRepository jobRequestRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

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
        User user = userService.findById(jobRequest.getUserId());
        if(user != null){
            if(userName.equalsIgnoreCase(user.getName())){
                return true;
            }
        }
        return false;
    }
}

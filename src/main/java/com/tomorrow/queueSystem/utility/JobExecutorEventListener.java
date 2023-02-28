package com.tomorrow.queueSystem.utility;


import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.service.JobRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;

@Component
public class JobExecutorEventListener {

    @Autowired
    JobRequestService jobRequestService;

    @EventListener
    public void onJobExecutorEvent(JobExecutorEvent jobExecutorEvent) {
        if(Status.PROCESSING.equals(jobExecutorEvent.getStatus())){
            processingEventOperations(jobExecutorEvent.jobRequest);
        }else if(Status.PENDING.equals(jobExecutorEvent.getStatus())){
            pendingEventOperations(jobExecutorEvent.jobRequest);
        }else if(Status.COMPLETE.equals(jobExecutorEvent.getStatus())){
            completeEventOperations(jobExecutorEvent.jobRequest);
        } if(Status.FAILED.equals(jobExecutorEvent.getStatus())){
            failedEventOperations(jobExecutorEvent.jobRequest);
        }
        jobExecutorEvent.jobRequest.setStatus(jobExecutorEvent.getStatus());
        jobRequestService.save(jobExecutorEvent.jobRequest);
    }

    private void processingEventOperations(JobRequest jobRequest) {
        jobRequest.setStartDate(Date.from(Instant.now()));
    }

    private void pendingEventOperations(JobRequest jobRequest) {
    }

    private void completeEventOperations(JobRequest jobRequest) {
        jobRequest.setEndDate(Date.from(Instant.now()));
        intimateUser();
    }

    private void failedEventOperations(JobRequest jobRequest) {
        jobRequest.setJobRequestDescription(Constants.JOB_FAILED_MESSAGE);
        jobRequest.setEndDate(Date.from(Instant.now()));
        intimateUser();
    }

    private void intimateUser(){

    }
}

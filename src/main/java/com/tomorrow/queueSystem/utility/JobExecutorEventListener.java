package com.tomorrow.queueSystem.utility;


import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.service.JobRequestService;
import com.tomorrow.queueSystem.service.MailService;
import com.tomorrow.queueSystem.service.PriorityJobSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class JobExecutorEventListener {

    @Autowired
    private ApplicationContext context;

    @Autowired
    JobRequestService jobRequestService;

    ExecutorService priorityJobPoolExecutor = Executors.newFixedThreadPool(10);

    @EventListener
    public void onJobExecutorEvent(JobExecutorEvent jobExecutorEvent) {
        if(Status.PROCESSING.equals(jobExecutorEvent.getStatus())){
            processingEventOperations(jobExecutorEvent.jobRequest,jobExecutorEvent.getStatus());
        }else if(Status.PENDING.equals(jobExecutorEvent.getStatus())){
            pendingEventOperations(jobExecutorEvent.jobRequest,jobExecutorEvent.getStatus());
        }else if(Status.COMPLETE.equals(jobExecutorEvent.getStatus())){
            completeEventOperations(jobExecutorEvent.jobRequest,jobExecutorEvent.getStatus());
        } if(Status.FAILED.equals(jobExecutorEvent.getStatus())){
            failedEventOperations(jobExecutorEvent.jobRequest,jobExecutorEvent.getStatus());
        }
        jobRequestService.save(jobExecutorEvent.jobRequest);
    }

    private void processingEventOperations(JobRequest jobRequest, Status status) {
        jobRequest.setStartDate(Date.from(Instant.now()));
        jobRequest.setStatus(status);
    }

    private void pendingEventOperations(JobRequest jobRequest, Status status) {
    }

    private void completeEventOperations(JobRequest jobRequest, Status status) {
        jobRequest.setEndDate(Date.from(Instant.now()));
        jobRequest.setStatus(status);
        intimateUser(jobRequest);
    }

    private void failedEventOperations(JobRequest jobRequest, Status status) {
        jobRequest.setJobRequestDescription(Constants.JOB_FAILED_MESSAGE);
        jobRequest.setEndDate(Date.from(Instant.now()));
        jobRequest.setStatus(status);
        intimateUser(jobRequest);
    }

    private void intimateUser(JobRequest jobRequest){
        MailService mailService = context.getBean(MailService.class);
        mailService.setExecutableJob(jobRequest);
        priorityJobPoolExecutor.execute(mailService);
    }
}

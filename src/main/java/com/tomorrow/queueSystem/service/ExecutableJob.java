package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.utility.Constants;
import com.tomorrow.queueSystem.utility.JobExecutorEvent;
import com.tomorrow.queueSystem.utility.Priority;
import com.tomorrow.queueSystem.utility.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
@Scope("prototype")
public class ExecutableJob implements Runnable{

    @Autowired
    private ApplicationEventPublisher publisher;

    private JobRequest jobRequest;
    private Priority priority;

    public ExecutableJob() {
    }

    public ExecutableJob(JobRequest jobRequest, Priority priority) {
        this.jobRequest = jobRequest;
        this.priority = priority;
    }

    public JobRequest getJobRequest() {
        return jobRequest;
    }

    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public void run() {
        try {
            System.out.println("Executing Job : " + this.toString());
            publisher.publishEvent(new JobExecutorEvent(jobRequest,Status.PROCESSING));
            Thread.sleep(jobRequest.getDuration() * 1000);
            publisher.publishEvent(new JobExecutorEvent(jobRequest,Status.COMPLETE));
        } catch (InterruptedException e) {
            publisher.publishEvent(new JobExecutorEvent(jobRequest,Status.FAILED));
        }
    }

    @Override
    public String toString() {
        return "ExecutableJob{" +
                ", priority=" + priority +
                ", jobRequest=" + jobRequest +
                '}';
    }
}

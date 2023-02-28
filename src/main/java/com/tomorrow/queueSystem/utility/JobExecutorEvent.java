package com.tomorrow.queueSystem.utility;

import com.tomorrow.queueSystem.persistence.JobRequest;

public class JobExecutorEvent {
    JobRequest jobRequest;
    Status status;

    public JobExecutorEvent(JobRequest jobRequest, Status status) {
        this.jobRequest = jobRequest;
        this.status = status;
    }

    public JobRequest getJobRequest() {
        return jobRequest;
    }

    public void setJobRequest(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

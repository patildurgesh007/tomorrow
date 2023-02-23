package com.tomorrow.queueSystem.persistence;

import com.tomorrow.queueSystem.utility.Priority;
import com.tomorrow.queueSystem.utility.Status;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import java.time.Instant;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Repository
@Entity(name = "jobRequests")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobRequest {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long requestId;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long jobId;

    @NotEmpty
    private Long duration;

    private Date creationDate;

    private Date startDate;

    private Date endDate;

    private Status status;

    private String jobRequestDescription;

    private Priority priority;

    public JobRequest() {
        this.priority = Priority.LOW;
        this.creationDate = Date.from(Instant.now());
        this.status = Status.CREATE;
    }

    public JobRequest(Long userId, Long jobId, Long duration) {
        this.userId = userId;
        this.jobId = jobId;
        this.duration = duration;
        this.priority = Priority.LOW;
        this.creationDate = Date.from(Instant.now());
        this.status = Status.CREATE;
    }

    public JobRequest(Long userId, Long jobId, Long duration, Priority priority) {
        this.userId = userId;
        this.jobId = jobId;
        this.duration = duration;
        this.priority = priority;
        this.creationDate = Date.from(Instant.now());
        this.status = Status.CREATE;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getJobRequestDescription() {
        return jobRequestDescription;
    }

    public void setJobRequestDescription(String jobRequestDescription) {
        this.jobRequestDescription = jobRequestDescription;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
}

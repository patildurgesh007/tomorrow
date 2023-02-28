package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.utility.Constants;
import com.tomorrow.queueSystem.utility.JobExecutorEvent;
import com.tomorrow.queueSystem.utility.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Scope("singleton")
public class PriorityJobSchedulerService {

    @Autowired
    private ApplicationEventPublisher publisher;

    private ExecutorService priorityJobPoolExecutor;
    private ExecutorService priorityJobScheduler  = Executors.newSingleThreadExecutor();
    private PriorityBlockingQueue<ExecutableJob> priorityQueue;

    public ExecutorService getPriorityJobPoolExecutor() {
        return priorityJobPoolExecutor;
    }

    public void setPriorityJobPoolExecutor(ExecutorService priorityJobPoolExecutor) {
        this.priorityJobPoolExecutor = priorityJobPoolExecutor;
    }

    public ExecutorService getPriorityJobScheduler() {
        return priorityJobScheduler;
    }

    public void setPriorityJobScheduler(ExecutorService priorityJobScheduler) {
        this.priorityJobScheduler = priorityJobScheduler;
    }

    public PriorityBlockingQueue<ExecutableJob> getPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(PriorityBlockingQueue<ExecutableJob> priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public PriorityJobSchedulerService(Integer poolSize, Integer queueSize) {
        this.priorityJobPoolExecutor = Executors.newFixedThreadPool(poolSize);
        this.priorityQueue = new PriorityBlockingQueue<>(
                queueSize,
                Comparator.comparing(ExecutableJob::getPriority));
        priorityJobScheduler.execute(() -> {
            while (true) {
                try {
                    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) priorityJobPoolExecutor;
                    if(poolSize - threadPoolExecutor.getActiveCount() > Constants.ZERO){
                        ExecutableJob  executableJob = priorityQueue.take();
                        priorityJobPoolExecutor.execute(executableJob);
                    }
                } catch (Exception e) {
                    break;
                }
            }
        });
    }
    public void scheduleJob(ExecutableJob executableJob) {
        publisher.publishEvent(new JobExecutorEvent(executableJob.getJobRequest(), Status.PENDING));
        priorityQueue.add(executableJob);
    }

    public void scheduleJobs(List<ExecutableJob> executableJobList) {
        executableJobList.stream().forEach(executableJob -> publisher.publishEvent(new JobExecutorEvent(executableJob.getJobRequest(), Status.PENDING)));
        priorityQueue.addAll(executableJobList);
    }
}

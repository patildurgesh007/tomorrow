package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.Job;
import com.tomorrow.queueSystem.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JobRepository jobRepository;

    public void save(Job job) {
        jobRepository.save(job);
    }

    public Job findById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public void delete(Job job) {
        jobRepository.delete(job);
    }
}

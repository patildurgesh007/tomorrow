package com.tomorrow.queueSystem.persistence;

import com.tomorrow.queueSystem.service.PriorityJobSchedulerService;
import com.tomorrow.queueSystem.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Entity {

    @Autowired
    ApplicationEventPublisher publisher;

    @Value("${tomorrow.queueSystem.poolSize:1}")
    private int POOL_SIZE;

    @Value("${tomorrow.queueSystem.queueSize:100}")
    private int QUEUE_SIZE;

    @Bean
    public PriorityJobSchedulerService getPriorityJobSchedulerServiceBean(){
        return new PriorityJobSchedulerService(POOL_SIZE,QUEUE_SIZE);
    }
}

package com.tomorrow.queueSystem;

import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.service.MailService;
import com.tomorrow.queueSystem.utility.Priority;
import com.tomorrow.queueSystem.utility.Status;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.*;

@SpringBootTest
@Order(2)
public class MailServiceTest {

    @Mock
    MailService mailService;

    @Test
    public void runTest(){
        JobRequest jobRequest = new JobRequest();
        jobRequest.setJobId(1L);
        jobRequest.setUserId(1L);
        jobRequest.setDuration(10L);
        jobRequest.setStatus(Status.COMPLETE);
        jobRequest.setPriority(Priority.HIGH);
        mailService.setExecutableJob(jobRequest);
        doNothing().when(mailService).sendEmail();
        mailService.sendEmail();
        verify(mailService,times(1)).sendEmail();
    }
}

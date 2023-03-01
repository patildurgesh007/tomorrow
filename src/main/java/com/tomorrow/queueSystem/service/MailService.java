package com.tomorrow.queueSystem.service;

import com.tomorrow.queueSystem.persistence.JobRequest;
import com.tomorrow.queueSystem.persistence.User;
import com.tomorrow.queueSystem.utility.Constants;
import com.tomorrow.queueSystem.utility.UtilsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Scope("prototype")
public class MailService implements Runnable{

    private JobRequest jobRequest;

    @Autowired
    UserService userService;

    @Value("${tomorrow.queueSystem.mail.sender.emailAddress:tomorrowqueuesystemupdates@gmail.com}")
    private String senderEmailAddress;

    @Value("${tomorrow.queueSystem.mail.sender.password:ocnfxqksymernafm}")
    private String senderEmailPassword;

    @Value("${tomorrow.queueSystem.mail.smtp.host:smtp.gmail.com}")
    private String host;

    @Value("${tomorrow.queueSystem.mail.smtp.port:465}")
    private String port;

    @Value("${tomorrow.queueSystem.mail.smtp.ssl.enable:true}")
    private String sslEnable;

    @Value("${tomorrow.queueSystem.mail.smtp.auth:true}")
    private String smtpAuth;

    Logger logger = LoggerFactory.getLogger(MailService.class);

    public JobRequest getExecutableJob() {
        return jobRequest;
    }

    public void setExecutableJob(JobRequest jobRequest) {
        this.jobRequest = jobRequest;
    }

    public void sendEmail() {
        logger.debug("Inside sendEmail method for Request - " + jobRequest.getRequestId());
        String receiverEmailAddress = getCurrentUserEmailAddress(jobRequest);
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", sslEnable);
        properties.put("mail.smtp.auth", smtpAuth);
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmailAddress, senderEmailPassword);
            }
        });
        session.setDebug(false);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmailAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmailAddress));
            message.setSubject(Constants.EMAIL_SUBJECT_LINE + jobRequest.getRequestId());
            message.setText(jobRequest.toStringForEmail());
            Transport.send(message);
            logger.info("Successfully mail send for Request - " + jobRequest.getRequestId());
        } catch (MessagingException mex) {
            logger.info("Sending mail service failed for Request - " + jobRequest.getRequestId());
            mex.printStackTrace();
        }
        logger.debug("Complete sendEmail method for Request - " + jobRequest.getRequestId());
    }
    public String getCurrentUserEmailAddress(JobRequest jobRequest) {
        User user = userService.findById(jobRequest.getUserId());
        if(user!= null){
            return user.getEmailAddress();
        }
        logger.error("User OR Email Address is not available for request - " + jobRequest.getRequestId());
        return "";
    }

    @Override
    public void run() {
        sendEmail();
    }
}

package com.tomorrow.queueSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class QueueSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueSystemApplication.class, args);
	}

}

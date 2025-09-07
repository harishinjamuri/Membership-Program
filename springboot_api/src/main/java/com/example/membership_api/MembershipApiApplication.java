package com.example.membership_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@EnableJpaRepositories(basePackages = "com.example.membership_api.dao")
@SpringBootApplication
public class MembershipApiApplication {

	private static final Logger logger = LoggerFactory.getLogger(MembershipApiApplication.class);


	public static void main(String[] args) {
		logger.info("Starting Membership API Application...");
        SpringApplication.run(MembershipApiApplication.class, args);
        logger.info("Membership API Application started successfully.");
	}

}

package com.project.AzCar.Services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@ComponentScan({"com.project.AzCar"})
public class AzCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzCarApplication.class, args);
	}

}

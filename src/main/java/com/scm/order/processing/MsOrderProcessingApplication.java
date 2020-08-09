package com.scm.order.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.scm.order.processing.entity")
@SpringBootApplication
public class MsOrderProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrderProcessingApplication.class, args);
	}

}

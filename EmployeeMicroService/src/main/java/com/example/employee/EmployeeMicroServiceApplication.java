package com.example.employee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
public class EmployeeMicroServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeMicroServiceApplication.class, args);
		log.info("=== Employee MicroService Application Started Successfully ===");
	}

	@Bean
	@LoadBalanced // with this annotation framework will take care of load balancing
	// between multiple instances of a service
	// without this annotation it will call only one instance of a service
	// even if there are multiple instances of a service
	// this annotation is used to make RestTemplate load balanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}

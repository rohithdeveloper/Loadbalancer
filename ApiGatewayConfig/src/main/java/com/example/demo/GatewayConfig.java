package com.example.demo;

import org.springframework.cloud.gateway.route.RouteLocator;

import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("Employee-MicroService", r -> r
						.path("/api/employee/**")
						.uri("lb://Employee-MicroService")) // Enable load balancing
				.route("Address-MicroService", r -> r
						.path("/api/**")
//						.path("/api/address")
						.uri("http://localhost:8081"))
				.build();
	}
}

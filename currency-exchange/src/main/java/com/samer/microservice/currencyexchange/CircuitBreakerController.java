package com.samer.microservice.currencyexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
@RestController
public class CircuitBreakerController {
	
	public static final Logger logger=LoggerFactory.getLogger(CircuitBreakerController.class);

	@Autowired
	private Environment env;
	
	
	@GetMapping("/sample-api")
//	@Retry(name="sample-api",fallbackMethod = "hardcodedResponse")
	@CircuitBreaker(name = "default",fallbackMethod = "hardcodedResponse")
	public String sampleApi() {
		logger.info("sample api call recieved");
		
		ResponseEntity<String> response=
		new RestTemplate().getForEntity("http://localhost:8080/dummy-url", String.class);
		
		return response.getBody();
	}
	
	public String hardcodedResponse(Exception e)
	{
		return String.format("fallback response : Exception came : %s ,retry dones %s times ",
				e,
				env.getProperty("resilience4j.retry.instances.sample-api.max-attempts"));
	}
}

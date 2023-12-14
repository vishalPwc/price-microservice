package com.aafes.price.adapter;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public final class IntegrationRestCall {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(IntegrationRestCall.class);
	
	@Autowired
	private RestTemplate restTemplate;
	

	public ResponseEntity<String> execute(HttpEntity<String> requestEntity, String endpoint) {
		ResponseEntity<String> responseEntity = null;
		LOGGER.debug("IntegrationRestCall : execute : START");
		String completeEndpoint = null;
		if(System.getenv().get("testMode").equals("true")) {
			completeEndpoint = System.getenv().get("testHostname") + endpoint;
			LOGGER.debug("IntegrationRestCall : running in test mode : {}", System.getenv().get("testHostname"));			
		}
		else {
			completeEndpoint = System.getenv().get("hostname") + ":" + endpoint;
		}
		LOGGER.debug("IntegrationRestCall : completeEndpoint : {}", completeEndpoint);
		responseEntity = restTemplate.exchange(completeEndpoint, HttpMethod.POST, requestEntity,
				String.class, Collections.emptyMap());
		LOGGER.debug("IntegrationRestCall : response status code : {}", responseEntity.getStatusCodeValue());
		LOGGER.debug("IntegrationRestCall : response body : {}", responseEntity.getBody());
		LOGGER.debug("IntegrationRestCall : execute : END");
		return responseEntity;
	}
	
}
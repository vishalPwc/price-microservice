package com.aafes.price.adapter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.aafes.price.model.Request;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public final class IntegrationAdapter {
	
	public static final String CONTENT_TYPE_ATTRIBUTE = "Content-Type";
	public static final String CONTENT_TYPE_VALUE =  "application/json;charset=UTF-8";
	public static final String ACCEPT_ATTRIBUTE = "Accept";
	public static final String ACCEPT_VALUE = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	public static final String ACCEPT_LANGUAGE_ATTRIBUTE = "Accept-Language";
	public static final String ACCEPT_LANGUAGE_VALUE = "en-US,en;q=0.5";
	public static final String UPGRADE_INSECURE_REQUESTS_ATTRIBUTE = "upgrade-insecure-requests";
	public static final String UPGRADE_INSECURE_REQUESTS_VALUE = "1";
	public static final String CONNECTION_ATTRIBUTE_VALUE = "keep-alive";
	public static final String CACHE_ATTRIBUTE_VALUE = "no-cache";
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(IntegrationAdapter.class);
	
	@Autowired
	private IntegrationRestCall restCall;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * This method will get request entity for post call to group by
	 *
	 * @param request the request
	 * @return Value as outputPLPResponseDataJsonNode.
	 */
	private static HttpEntity<String> getRequestEntityForPostApi(String body, Map<String, String> httpHeadersMap) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(ACCEPT_ATTRIBUTE, ACCEPT_VALUE);
		headers.set(ACCEPT_LANGUAGE_ATTRIBUTE, ACCEPT_LANGUAGE_VALUE);
		headers.set(UPGRADE_INSECURE_REQUESTS_ATTRIBUTE, UPGRADE_INSECURE_REQUESTS_VALUE);
		headers.setCacheControl(CACHE_ATTRIBUTE_VALUE);
		headers.setConnection(CONNECTION_ATTRIBUTE_VALUE);
		headers.setPragma(CACHE_ATTRIBUTE_VALUE);
		if (httpHeadersMap != null && !httpHeadersMap.isEmpty()) {
			httpHeadersMap.forEach(headers::set);
		}
		return new HttpEntity<>(body, headers);
	}
	
	/**
	 * 
	 * @param requestObject
	 * @return
	 */
	public HttpEntity<String> constructPayloadRequest(Request requestObject){
		String requestDetails;
		HttpEntity<String> httpEntity = null;
		try {
			LOGGER.debug("IntegrationAdapter : constructPayloadRequest : START");
			requestDetails = objectMapper.writeValueAsString(requestObject);
			LOGGER.debug("IntegrationAdapter : requestDetails : {}", requestDetails);
			Map<String, String> httpHeaders = new HashMap<>();
			httpHeaders.put(CONTENT_TYPE_ATTRIBUTE, CONTENT_TYPE_VALUE);
			httpEntity = getRequestEntityForPostApi(requestDetails, httpHeaders);
			LOGGER.debug("IntegrationAdapter : constructPayloadRequest : END");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return httpEntity;
	}
	
	/**
	 * This method is used to process the outgoing GET request
	 * @param EndpointUrl
	 * @return
	 */
	public ResponseEntity<String> processGetRequest(String pEndPointUrl){
		LOGGER.debug("IntegrationAdapter : processGetRequest : START");
		ResponseEntity<String> responseEntity = null;
		LOGGER.debug("IntegrationAdapter : Endpoint : {}", pEndPointUrl);	
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> httpRequestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        responseEntity = restTemplate.exchange(pEndPointUrl, HttpMethod.GET, httpRequestEntity, String.class);
		LOGGER.debug("IntegrationAdapter : processRequest : END");
		return responseEntity;
	}
}

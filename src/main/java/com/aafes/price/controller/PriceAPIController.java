package com.aafes.price.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.price.adapter.IntegrationAdapter;
import com.aafes.price.utils.Utils;


@RestController
public class PriceAPIController{

	protected static final Logger LOGGER = LoggerFactory.getLogger(PriceAPIController.class);
	protected static final String PRICE_SPEC_PATH = "/priceJsonTransformSpec.json";
	protected static final String PRICE_ENDPOINT_URL = "/public/v1/skus/getSkuPriceDetails?skudIds=";
	protected static final String STORE_ID_PARAM = "storeIds=";
	protected static final String MOCK_RESPONSE = "{\"success\":true,\"responseCode\":200,\"headers\":{},\"data\":[{\"skuId\":\"3862842\",\"storePrice\":[],\"msrpPrice\":1000,\"listPrice\":900,\"salePrice\":800,\"unitCostPrice\":700},{\"skuId\":\"3862841\",\"storePrice\":[{\"storeId\":\"1105312\",\"price\":950},{\"storeId\":\"1105313\",\"price\":980}],\"msrpPrice\":null,\"listPrice\":900,\"salePrice\":null,\"unitCostPrice\":null}]}";
			
	@Autowired
	private IntegrationAdapter clientAdapter;

	@GetMapping(path = "/test/api", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getPriceParams(HttpServletRequest request) {
		String testmode = System.getenv().get("testmode");
		String env = System.getenv().get("env");
		return "Current environment is: " + env + " and test mode is: " + testmode;
	}



	@GetMapping(path = { "/price/v1/getItemPrice/skudIds/{listSkuIds}" , "/price/v1/getItemPrice/skudIds/{listSkuIds}/storeIds/{listStoreIds}"},
			produces = MediaType.APPLICATION_JSON_VALUE)
	public String getItemPrice(@PathVariable  String listSkuIds , @PathVariable (required = false) String listStoreIds) {

		LOGGER.debug("PriceAPIController : getItemPrice : START");
		
		String getPriceDetailsEndPoint = null;
		if((System.getenv().get("testmode")).equals("true")) {
			//getPriceDetailsEndPoint = MOCK_RESPONSE;
			return MOCK_RESPONSE;
		}
		else if(null != listStoreIds && !listStoreIds.isEmpty() ) {
			getPriceDetailsEndPoint = System.getenv().get("hosturl") + PRICE_ENDPOINT_URL + listSkuIds + STORE_ID_PARAM + listStoreIds;
		}else {
			getPriceDetailsEndPoint = System.getenv().get("hosturl")  + PRICE_ENDPOINT_URL + listSkuIds;
		}
		
		LOGGER.debug("PriceAPIController : enpoint for price details :" + getPriceDetailsEndPoint);

		ResponseEntity<String> responseEntity = clientAdapter.processGetRequest(getPriceDetailsEndPoint);
		LOGGER.debug("PriceAPIController : getItemPrice : END");
		return Utils.doJsonTransform(responseEntity.getBody(), PRICE_SPEC_PATH);


	}
}




package com.aafes.price.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;


public class Utils{

	protected static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

	/*
	 * This method will transform input json structure into output json structure as defined in the jolt spec
	 */
	public static String doJsonTransform(String pInputJsonString, String pSpecPath) {
		LOGGER.debug("jsonTransformation : START");
		LOGGER.debug("Input json: " + pInputJsonString);
		List chainrSpecJSON = JsonUtils.classpathToList(pSpecPath);
		Chainr chainr = Chainr.fromSpec( chainrSpecJSON );
		Object inputPriceJson = JsonUtils.jsonToObject(pInputJsonString);
		Object transformedOutput = chainr.transform( inputPriceJson );
		String outputJsonString = JsonUtils.toJsonString(transformedOutput);
		LOGGER.debug("output json: "+ outputJsonString);
		LOGGER.debug("jsonTransformation : END");
		return outputJsonString;
	}

}
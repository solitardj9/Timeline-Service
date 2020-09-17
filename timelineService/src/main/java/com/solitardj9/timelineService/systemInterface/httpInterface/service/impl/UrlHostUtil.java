package com.solitardj9.timelineService.systemInterface.httpInterface.service.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

public class UrlHostUtil {
	//
	private static final Logger logger = LoggerFactory.getLogger(UrlHostUtil.class);
	
	public static UriComponentsBuilder makeUriComponentsBuilder(String shceme, String url, String path) {
		//
		String host = getHost(url); 
		String port = getPort(url);
		
		if (checkUrlValidation(host, port)) {
			UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
																			.scheme(shceme)
																			.host(host)
																			.port(port)
																			.path(path);
			
			return uriComponentsBuilder;
		}
		
		return null;
	}
	
	private static Boolean checkUrlValidation(String host, String port) {
		//
		if (checkHostValidation(host) == true & checkPortValidation(port) == true) {
			return true;
		}
		return false;
	}
	
	private static Boolean checkHostValidation(String host) {
		//
		if (host != null) {
			return true;
		}
		return false;
	}
	
	private static Boolean checkPortValidation(String port) {
		//
		if (port != null && !port.equals("-1")) {
			return true;
		}
		return false;
	}
	
	private static String getHost(String url) {
		//
		URL tmpUrl = null;
		try {
			tmpUrl = new URL(url);
		}
		catch (MalformedURLException e) {
			logger.error("[UrlHostUtil].getHost : error = " + e.toString());
			return null;
		}
		
		String host = tmpUrl.getHost();
		
		return host;
	}
	
	private static String getPort(String url) {
		//
		URL tmpUrl = null;
		try {
			tmpUrl = new URL(url);
		}
		catch (MalformedURLException e) {
			logger.error("[UrlHostUtil].getPort : error = " + e.toString());			
			return null;
		}
		
		Integer port = tmpUrl.getPort();
		
		// if the port is not explicitly specified in the input, it will be -1.
		return port.toString();
	}
}
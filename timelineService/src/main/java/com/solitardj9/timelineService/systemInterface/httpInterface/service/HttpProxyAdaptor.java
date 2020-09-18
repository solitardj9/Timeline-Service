package com.solitardj9.timelineService.systemInterface.httpInterface.service;

import java.io.File;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface HttpProxyAdaptor {
	//
	public ResponseEntity<String> executeHttpProxy(String scheme, String url, String path, Map<String, Object> queryParams, HttpMethod method, HttpHeaders headers, String body);
	
	// https://www.baeldung.com/spring-resttemplate-download-large-file
	// https://www.baeldung.com/rest-template
	// https://stackoverflow.com/questions/48693858/spring-cloud-multiple-resttemplate
	// https://petervarga.org/2019/12/13/using-multiple-retstemplates-in-spring-boot-application/
	public File executeHttpProxyForFile(String scheme, String url, String path, Map<String, Object> queryParams, HttpHeaders headers, String body, String fileName);
}
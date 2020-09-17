package com.solitardj9.timelineService.systemInterface.httpInterface.service;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public interface HttpProxyAdaptor {
	//
	public ResponseEntity<String> executeHttpProxy(String scheme, String url, String path, Map<String, Object> queryParams, HttpMethod method, HttpHeaders headers, String body);
}
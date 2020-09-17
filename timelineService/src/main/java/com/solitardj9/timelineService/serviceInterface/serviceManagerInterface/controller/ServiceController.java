package com.solitardj9.timelineService.serviceInterface.serviceManagerInterface.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.service.serviceManager.service.ServiceManager;

@RestController
@RequestMapping(value="/timeline-service/management")
public class ServiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	ServiceManager serviceManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/service/start")
	public ResponseEntity startService(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[ServiceController].startService is called.");
		
		serviceManager.startService();
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/service/stop")
	public ResponseEntity stopService(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[ServiceController].stopService is called.");
		
		serviceManager.stopService();
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/service/instances")
	public ResponseEntity getServiceInstances(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[ServiceController].getServiceInstances is called.");
		
		Map<String, ServiceInstance> resultMap = serviceInstancesManager.getServiceInstances();
		try {
			String responseBody = om.writeValueAsString(resultMap);
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} catch (JsonProcessingException e) {
			logger.info("[ServiceController].getServiceInstances : error = " + e.getStackTrace());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
	
	// TODO : add to check health interface
}
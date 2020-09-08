package com.solitardj9.timelineService.serviceInterface.serviceManagerInterface.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.timelineService.application.serviceManager.service.ServiceManager;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/timeline-service/management")
public class ServiceController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	@Autowired
	ServiceManager serviceManager;
	
	@ApiOperation(value = "Service Start", notes = "Service Start")
	@RequestMapping(value="/service/start", method = RequestMethod.PUT)
	public ResponseEntity startService(@RequestBody(required=false) String requestBody) {
		//
		logger.info("startService is called.");
		
		serviceManager.startService();
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
	
	@ApiOperation(value = "Service Stop", notes = "Service Stop")
	@RequestMapping(value="/service/stop", method = RequestMethod.PUT)
	public ResponseEntity stopService(@RequestBody(required=false) String requestBody) {
		//
		logger.info("stopService is called.");
		
		serviceManager.startService();
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
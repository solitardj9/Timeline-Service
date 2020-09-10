package com.solitardj9.timelineService.serviceInterface.testManagerInterface.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.timelineService.application.testManager.service.TestManager;

@RestController
@RequestMapping(value="/timeline-service/test")
public class TestController {
	
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@Autowired
	TestManager testManager;
	
	@PutMapping(value="/publish")
	public ResponseEntity publishMessage(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[TestController].publishMessage is called.");
		
		testManager.publishMessage("sample data.");
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
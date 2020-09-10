package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;

@RestController
@RequestMapping(value="/timeline-service/")
public class TimelineSyncController {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineSyncController.class);

	@Autowired
	TimelineSyncManager timelineSyncManager;
	
	@PutMapping(value="/timeline/{timeline}")
	public ResponseEntity addTimeline(@PathVariable("timeline") String timeline,
									  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TestController].addTimeline is called.");
		
		try {
			timelineSyncManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
    }
}
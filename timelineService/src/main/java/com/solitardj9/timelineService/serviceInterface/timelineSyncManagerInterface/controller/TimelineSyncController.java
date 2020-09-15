package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.controller;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.ResultTimeline;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.ResultTimelineValues;

@RestController
@RequestMapping(value="/timeline-service/")
public class TimelineSyncController {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineSyncController.class);
	
	@Autowired
	TimelineManager timelineManager;

	@Autowired
	TimelineSyncManager timelineSyncManager;
	
	@SuppressWarnings("rawtypes")
	@PutMapping(value="/timelines/{timeline}")
	public ResponseEntity addTimeline(@PathVariable("timeline") String timeline,
										  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].addTimeline is called.");
		
		try {
			timelineSyncManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].addTimeline : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResultTimeline(HttpStatus.CONFLICT.value(), timeline), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<>(new ResultTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value="/timelines/{timeline}")
	public ResponseEntity deleteTimeline(@PathVariable("timeline") String timeline,
											  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].deleteTimeline is called.");
		
		try {
			timelineSyncManager.deleteTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].deleteTimeline : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResultTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResultTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/{timeline}")
	public ResponseEntity getTimeline(@PathVariable("timeline") String timeline,
										  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].getTimeline is called.");
		
		TreeMap<Long, String> retmap = null;
		try {
			retmap = timelineManager.getTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].getTimeline : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResultTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResultTimelineValues(HttpStatus.OK.value(), timeline, retmap), HttpStatus.OK);
	}
	
}
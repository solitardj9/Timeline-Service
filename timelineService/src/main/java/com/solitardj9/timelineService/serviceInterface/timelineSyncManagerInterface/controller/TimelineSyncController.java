package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.controller;

import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.RequestPut;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.RequestPutAll;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.ResponseTimeline;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.ResponseTimelineValues;

@RestController
@RequestMapping(value="/timeline-service/")
public class TimelineSyncController {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineSyncController.class);
	
	@Autowired
	TimelineManager timelineManager;

	@Autowired
	TimelineSyncManager timelineSyncManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/timelines/{timeline}")
	public ResponseEntity addTimeline(@PathVariable("timeline") String timeline,
										  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].addTimeline is called.");
		
		try {
			timelineSyncManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].addTimeline : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.CONFLICT.value(), timeline), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
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
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
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
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimelineValues(HttpStatus.OK.value(), timeline, retmap), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		{
	 * 			"time" : {Long},
	 * 			"value" : "{String}"
	 * 		}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/timelines/{timeline}/timeline/value")
	public ResponseEntity putTimelineValue(@PathVariable("timeline") String timeline,
												@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].putTimelineValue is called.");
		
		RequestPut request = null;
		try {
			request = om.readValue(requestBody, RequestPut.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.put(timeline, request.getTime(), request.getValue());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		{
	 * 			"values" : {
	 * 				"{time}":"{value}",
	 * 				"{time}":"{value}",
	 * 				...
	 * 				"{time}":"{value}"
	 * 			}
	 * 		}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/timelines/{timeline}/timeline/values")
	public ResponseEntity putTimelineValues(@PathVariable("timeline") String timeline,
												@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].putTimelineValues is called.");
		
		RequestPutAll request = null;
		try {
			request = om.readValue(requestBody, RequestPutAll.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.putAll(timeline, request.getValues());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValues : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValues : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		{
	 * 			"time" : {Long},
	 * 			"value" : "{String}"
	 * 		}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PatchMapping(value="/timelines/{timeline}/timeline/value")
	public ResponseEntity patchTimelineValue(@PathVariable("timeline") String timeline,
												  @RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].patchTimelineValue is called.");
		
		RequestPut request = null;
		try {
			request = om.readValue(requestBody, RequestPut.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.update(timeline, request.getTime(), request.getValue());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		{
	 * 			"values" : {
	 * 				"{time}":"{value}",
	 * 				"{time}":"{value}",
	 * 				...
	 * 				"{time}":"{value}"
	 * 			}
	 * 		}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PatchMapping(value="/timelines/{timeline}/timeline/values")
	public ResponseEntity patchTimelineValues(@PathVariable("timeline") String timeline,
													@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].patchTimelineValues is called.");
		
		RequestPutAll request = null;
		try {
			request = om.readValue(requestBody, RequestPutAll.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.updateAll(timeline, request.getValues());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e.getStackTrace());
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
}
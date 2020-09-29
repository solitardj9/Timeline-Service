package com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.solitardj9.timelineService.application.timelineManager.service.TimelineManager;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineConflictFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineInternalFailure;
import com.solitardj9.timelineService.application.timelineManager.service.exception.ExceptionTimelineResourceNotFound;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineSyncManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceClusterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.exception.FileDownloadException;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.put.RequestPut;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.request.put.RequestPutAll;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseDefualt;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseKeySetOfTimeline;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseKeySetOfTimelines;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseTimeline;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseTimelineEmpty;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseTimelineSize;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseTimelineValues;
import com.solitardj9.timelineService.serviceInterface.timelineSyncManagerInterface.model.response.ResponseTimelinesValues;
import com.solitardj9.timelineService.utils.fileUtil.MapFileUtil;

@RestController
@RequestMapping(value="/timeline-service/")
public class TimelineSyncController {
	
	private static final Logger logger = LoggerFactory.getLogger(TimelineSyncController.class);
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
	@Autowired
	TimelineManager timelineManager;

	@Autowired
	TimelineSyncManager timelineSyncManager;
	
	private ObjectMapper om = new ObjectMapper();
	
	private Boolean isServiceAvailable() {
		//
		if (serviceInstancesManager.isClustered().equals(ServiceInstanceClusterStatus.ONLINE)) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping(value="/timelines/{timeline}")
	public ResponseEntity addTimeline(@PathVariable("timeline") String timeline,
										  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].addTimeline is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].addTimeline : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		try {
			timelineSyncManager.addTimeline(timeline);
		} catch (ExceptionTimelineConflictFailure e) {
			logger.error("[TimelineSyncController].addTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.CONFLICT.value(), timeline), HttpStatus.CONFLICT);
		} catch (ExceptionTimelineInternalFailure e) {
			logger.error("[TimelineSyncController].addTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping(value="/timelines/{timeline}")
	public ResponseEntity deleteTimeline(@PathVariable("timeline") String timeline,
											  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].deleteTimeline is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].deleteTimeline : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		try {
			timelineSyncManager.deleteTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			logger.error("[TimelineSyncController].deleteTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/keySet")
	public ResponseEntity getkeySetOfTimelines(@RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].getkeySetOfTimelines is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getkeySetOfTimelines : error = service is not available.");
			return new ResponseEntity<>(new ResponseKeySetOfTimelines(HttpStatus.SERVICE_UNAVAILABLE.value(), new HashSet<String>()), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		Set<String> retSet = timelineManager.getkeySetOfTimelines();
		
		return new ResponseEntity<>(new ResponseKeySetOfTimelines(HttpStatus.OK.value(), retSet), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param type
	 * @param time
	 * @param fromTime
	 * @param toTime
	 * 		- get
	 * 			"type" : "get"
	 * 		- get by time
	 * 			"type" : "getByTime",
	 * 			"time" : {Long} 
	 * 		- get by period
	 * 			"type" : "getByPreiod",
	 * 			"fromTime" : {Long}, 
	 * 			"toTime" : {Long}
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/{timeline}/values")
	public ResponseEntity getTimelineValues(@PathVariable("timeline") String timeline,
											@RequestParam(value = "type", required=true) String type,
											@RequestParam(value = "time", required=false) Long time,
											@RequestParam(value = "fromTime", required=false) Long fromTime,
											@RequestParam(value = "toTime", required=false) Long toTime) {
		//
		logger.info("[TimelineSyncController].getTimelineValues is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getTimelineValues : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		TreeMap<Long, String> retMap = new TreeMap<>();
		if (type.equals("get")) {
			//
			try {
				retMap = timelineManager.get(timeline);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].get : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			}
		}
		else if (type.equals("getByTime")) {
			//
			try {
				retMap = timelineManager.getByTime(timeline, time);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].getByTime : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			}
		}
		else if (type.equals("getByPeriod")) {
			//
			try {
				retMap = timelineManager.getByPreiod(timeline, fromTime, toTime);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].getByPeriod : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			}
		}
		else {
			logger.error("[TimelineSyncController].getTimelineValues : error = not supported type to get." );
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimelineValues(HttpStatus.OK.value(), retMap), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/{timeline}/keySet")
	public ResponseEntity getkeySetOfTimeline(@PathVariable("timeline") String timeline,
											  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].getkeySetOfTimeline is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getkeySetOfTimeline : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		Set<Long> retSet;
		try {
			retSet = timelineManager.getkeySetOfTimeline(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].getkeySetOfTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseKeySetOfTimeline(HttpStatus.OK.value(), retSet), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		- get
	 * 			"type" : "get",
	 * 			"requestBody(timelines)" : ["{timeline}", "{timeline}",..., "{timeline}"]
	 * 		- get by time
	 * 			"type" : "getByTime",
	 * 			"time" : {Long},
	 * 			"requestBody(timelines)" : ["{timeline}", "{timeline}",..., "{timeline}"] 
	 * 		- get by period
	 * 			"type" : "getByPreiod",
	 * 			"fromTime" : {Long}, 
	 * 			"toTime" : {Long},
	 * 			"requestBody(timelines)" : ["{timeline}", "{timeline}",..., "{timeline}"]
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value="/timelines/values")
	public ResponseEntity getTimelinesValues(@RequestBody(required=true) String requestBody,
											 @RequestParam(value = "type", required=true) String type,
											 @RequestParam(value = "time", required=false) Long time,
											 @RequestParam(value = "fromTime", required=false) Long fromTime,
											 @RequestParam(value = "toTime", required=false) Long toTime) {
		//
		logger.info("[TimelineSyncController].getTimelinesValues is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getTimelinesValues : error = service is not available.");
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.SERVICE_UNAVAILABLE.value()), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		Map<String, TreeMap<Long, String>> retMap = new HashMap<>();
		try {
			List<String> timelines = om.readValue(requestBody, List.class);
			
			if (type.equals("get")) {
				//
				try {
					retMap = timelineManager.getTimelines(timelines);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.error("[TimelineSyncController].getTimelines : error = " + e);
					return new ResponseEntity<>(new ResponseDefualt(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else if (type.equals("getByTime")) {
				//
				try {
					retMap = timelineManager.getTimelinesByTime(timelines, time);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.error("[TimelineSyncController].getTimelinesByTime : error = " + e);
					return new ResponseEntity<>(new ResponseDefualt(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else if (type.equals("getByPeriod")) {
				//
				try {
					retMap = timelineManager.getTimelinesByPreiod(timelines, fromTime, toTime);
				} catch (Exception e) {
					//e.printStackTrace();
					logger.error("[TimelineSyncController].getTimelinesByPeriod : error = " + e);
					return new ResponseEntity<>(new ResponseDefualt(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else {
				logger.error("[TimelineSyncController].getTimelinesValues : error = not supported type to get." );
				return new ResponseEntity<>(new ResponseDefualt(HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
			}
			
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].getTimelinesValues : error = " + e);
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(new ResponseTimelinesValues(HttpStatus.OK.value(), retMap), HttpStatus.OK);
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
	@PostMapping(value="/timelines/{timeline}/value")
	public ResponseEntity putTimelineValue(@PathVariable("timeline") String timeline,
												@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].putTimelineValue is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].putTimelineValue : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		RequestPut request = null;
		try {
			request = om.readValue(requestBody, RequestPut.class);
		} catch (JsonProcessingException e) {
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.put(timeline, request.getTime(), request.getValue());
		} catch (ExceptionTimelineResourceNotFound e) {
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e);
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
	@PostMapping(value="/timelines/{timeline}/values")
	public ResponseEntity putTimelineValues(@PathVariable("timeline") String timeline,
												@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].putTimelineValues is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].putTimelineValues : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		RequestPutAll request = null;
		try {
			request = om.readValue(requestBody, RequestPutAll.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValue : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.putAll(timeline, request.getValues());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValues : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].putTimelineValues : error = " + e);
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
	@PatchMapping(value="/timelines/{timeline}/value")
	public ResponseEntity patchTimelineValue(@PathVariable("timeline") String timeline,
												  @RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].patchTimelineValue is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].patchTimelineValue : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		RequestPut request = null;
		try {
			request = om.readValue(requestBody, RequestPut.class);
		} catch (JsonProcessingException e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.update(timeline, request.getTime(), request.getValue());
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].patchTimelineValue : error = " + e);
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
	@PatchMapping(value="/timelines/{timeline}/values")
	public ResponseEntity patchTimelineValues(@PathVariable("timeline") String timeline,
													@RequestBody(required=true) String requestBody) {
		//
		logger.info("[TimelineSyncController].patchTimelineValues is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].patchTimelineValues : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		RequestPutAll request = null;
		try {
			request = om.readValue(requestBody, RequestPutAll.class);
		} catch (JsonProcessingException e) {
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
		}
		
		try {
			timelineSyncManager.updateAll(timeline, request.getValues());
		} catch (ExceptionTimelineResourceNotFound e) {
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		} catch (ExceptionTimelineInternalFailure e) {
			logger.error("[TimelineSyncController].patchTimelineValues : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	/**
	 * @param timeline
	 * @param requestBody
	 * 		- remove
	 * 				"type" : "remove",
	 * 				"time" : {Long} 
	 * 		- remove by times
	 * 				"type" : "removeByTimes",
	 * 				"requestBody(times)" : [{Long}, ... , {Long}] 
	 * 		- remove by period
	 * 				"type" : "removeByPeriod",
	 * 				"fromTime" : {Long}, 
	 * 				"toTime" : {Long}
	 * 		- remove by times
	 * 				"type" : "removeByBefore",
	 * 				"toTime" : {Long} 
	 * 		- clear
	 * 				"type" : "clear",
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@DeleteMapping(value="/timelines/{timeline}/values")
	public ResponseEntity deleteTimelineValues(@PathVariable("timeline") String timeline,
													 @RequestBody(required=false) String requestBody,
													 @RequestParam(value = "type", required=true) String type,
													 @RequestParam(value = "time", required=false) Long time,
													 @RequestParam(value = "fromTime", required=false) Long fromTime,
													 @RequestParam(value = "toTime", required=false) Long toTime) {
		//
		logger.info("[TimelineSyncController].deleteTimelineValues is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].deleteTimelineValues : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
			
		if (type.equals("remove")) {
			//
			try {
				timelineSyncManager.remove(timeline, time);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].remove : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			} catch (ExceptionTimelineInternalFailure e) {
				logger.error("[TimelineSyncController].remove : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else if (type.equals("removeByTimes")) {
			//
			try {
				List<Long> times = om.readValue(requestBody, List.class);
				
				try {
					timelineSyncManager.removeByTimes(timeline, times);
				} catch (ExceptionTimelineResourceNotFound e) {
					logger.error("[TimelineSyncController].removeByTimes : error = " + e);
					return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
				} catch (ExceptionTimelineInternalFailure e) {
					logger.error("[TimelineSyncController].removeByTimes : error = " + e);
					return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} catch (JsonProcessingException e) {
				logger.error("[TimelineSyncController].removeByTimes : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.BAD_REQUEST.value(), timeline), HttpStatus.BAD_REQUEST);
			}
		}
		else if (type.equals("removeByPeriod")) {
			//
			try {
				timelineSyncManager.removeByPeriod(timeline, fromTime, toTime);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].removeByPeriod : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			}
		}
		else if (type.equals("removeByBefore")) {
			//
			try {
				timelineSyncManager.removeByBefore(timeline, toTime);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].removeByBefore : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			}
		}
		else if (type.equals("clear")) {
			//
			try {
				timelineSyncManager.clear(timeline);
			} catch (ExceptionTimelineResourceNotFound e) {
				logger.error("[TimelineSyncController].clear : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
			} catch (ExceptionTimelineInternalFailure e) {
				logger.error("[TimelineSyncController].clear : error = " + e);
				return new ResponseEntity<>(new ResponseTimeline(HttpStatus.INTERNAL_SERVER_ERROR.value(), timeline), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			logger.error("[TimelineSyncController].deleteTimelineValues : error = not supported type to delete." );
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimeline(HttpStatus.OK.value(), timeline), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/{timeline}/empty")
	public ResponseEntity isEmpty(@PathVariable("timeline") String timeline,
									  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].isEmpty is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].isEmpty : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		Boolean ret = true;
		try {
			ret = timelineManager.isEmpty(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			logger.error("[TimelineSyncController].getTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimelineEmpty(HttpStatus.OK.value(), ret), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(value="/timelines/{timeline}/size")
	public ResponseEntity getSize(@PathVariable("timeline") String timeline,
								  	  @RequestBody(required=false) String requestBody) {
		//
		logger.info("[TimelineSyncController].getSize is called.");
		
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getSize : error = service is not available.");
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.SERVICE_UNAVAILABLE.value(), timeline), HttpStatus.SERVICE_UNAVAILABLE);
		}
		
		Integer ret = null;
		try {
			ret = timelineManager.size(timeline);
		} catch (ExceptionTimelineResourceNotFound e) {
			//e.printStackTrace();
			logger.error("[TimelineSyncController].getTimeline : error = " + e);
			return new ResponseEntity<>(new ResponseTimeline(HttpStatus.NOT_FOUND.value(), timeline), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(new ResponseTimelineSize(HttpStatus.OK.value(), ret), HttpStatus.OK);
	}
	
	@GetMapping("/timelines/backup/{fileName}")
	public ResponseEntity<Resource> getBackup(@PathVariable String fileName,
													HttpServletRequest request) {
		//
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].getBackup : error = service is not available.");
		}
		
		timelineManager.backupTimelines(fileName);
		
		// Load file as Resource
		Resource resource = loadFileAsResource(fileName);
		
		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			logger.error("[TimelineSyncController].getBackup : error = could not determine file type.");
		}
		
		// Fallback to the default content type if type could not be determined
		if(contentType == null) {
			contentType = "application/octet-stream";
		}
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("/timelines/backup/{fileName}")
    public ResponseEntity deleteBackup(@PathVariable String fileName) {
		//
		if (!isServiceAvailable()) {
			logger.error("[TimelineSyncController].deleteBackup : error = service is not available.");
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.OK);
		}
		
		if (MapFileUtil.deleteFile(fileName)) {
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.OK.value()), HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(new ResponseDefualt(HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.OK);
		}
	}
	
	private Resource loadFileAsResource(String fileName) {
		//
		try {
			Path fileLocation = Paths.get(fileName).toAbsolutePath().normalize();
			Resource resource = new UrlResource(fileLocation.toUri());
			
			if(resource.exists()) {
				return resource;
			}
			else {
				throw new FileDownloadException(fileName + " not found.");
			}
		} catch (MalformedURLException e) {
			throw new FileDownloadException(fileName + " not found.", e);
		}
	}
}
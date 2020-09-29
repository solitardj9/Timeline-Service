package com.solitardj9.timelineService.service.serviceInstancesManager.service.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.IMap;
import com.solitardj9.timelineService.application.timelineSyncManager.service.TimelineRestoreManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceClusterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceInMemoryMap;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceRegisterStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesCallback;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.serviceInterface.serviceManagerInterface.model.ServiceHealth;
import com.solitardj9.timelineService.systemInterface.httpInterface.service.HttpProxyAdaptor;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryEventListener;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryManager;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureCreationFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureNotFoundFailure;
import com.solitardj9.timelineService.systemInterface.schedulerInterface.service.SchedulerManager;

@Service("serviceInstancesManager")
public class ServiceInstancesManagerImpl implements ServiceInstancesManager, InMemoryEventListener, Job {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceInstancesManagerImpl.class);
	
	@Autowired
	InMemoryManager inMemoryManager;
	
	@Autowired
	SchedulerManager schedulerManager;
	
	@Autowired
	TimelineRestoreManager timelineRestoreManager;
	
	@Autowired
	HttpProxyAdaptor httpProxyAdaptor;
	
	@Value("${server.port}")
	private Integer port;
	
	@Value("${service.healthCheckBatch}")
	private String healthCheckBatch;
	
	@Value("${service.healthCheckMissTermByMs}")
	private Long healthCheckMissTermByMs;
	
	private String ip;
	
	private Integer backupCount = 2;
	
	private Boolean readBackupData = true;
	
	private ServiceInstancesCallback serviceInstancesCallback;
	
	private String serviceName;
	
	private ServiceInstanceRegisterStatus registerStatus;
	
	private ServiceInstanceClusterStatus clusterStatus;
	
	private ObjectMapper om = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		//
		registerStatus = ServiceInstanceRegisterStatus.UNREGISTERED;
		clusterStatus = ServiceInstanceClusterStatus.OFFLINE;
		
		InMemoryInstance inMemoryInstance = new InMemoryInstance(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap(), backupCount, readBackupData, null, this);
		try {
			inMemoryManager.addMap(inMemoryInstance);
		} catch (ExceptionHazelcastDataStructureCreationFailure | ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].init : error = " + e);
		}
		
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;
				
				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while(addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					if(Inet4Address.class == addr.getClass()) {
						ip = addr.getHostAddress();
						logger.info("[ServiceInstancesManager].init : ip = " + ip);
					}
				}
			}
		} catch (SocketException e) {
			logger.error("[ServiceInstancesManager].init : error = " + e);
		}
	}
	
	@Override
	public void setServiceInstancesCallback(ServiceInstancesCallback serviceInstancesCallback) {
		this.serviceInstancesCallback = serviceInstancesCallback;
	}

	@Override
	public void registerService(String serviceName) {
		//
		if (registerStatus.equals(ServiceInstanceRegisterStatus.UNREGISTERED)) {
			//
			this.serviceName = serviceName;
		
			try {
				if (!inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).containsKey(serviceName)) {
					ServiceInstance serviceInstance = new ServiceInstance(serviceName, ip, port, new Timestamp(System.currentTimeMillis()), registerStatus, clusterStatus, new Timestamp(System.currentTimeMillis()));
					inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).put(serviceName, serviceInstance);
				}
			} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
				logger.error("[ServiceInstancesManager].registerService : error = " + e);
			}
			
			registerStatus = ServiceInstanceRegisterStatus.REGISTERED;
			
			timelineRestoreManager.executeRestore();

			clusterStatus = ServiceInstanceClusterStatus.RESTORED;
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			timelineRestoreManager.restoreReplicationEvent();
			
			clusterStatus = ServiceInstanceClusterStatus.ONLINE;
			
			initializeScheduler();
		}
	}

	@Override
	public void unregisterService(String serviceName) {
		//
		try {
			inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).remove(serviceName);
			
			registerStatus = ServiceInstanceRegisterStatus.UNREGISTERED;
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].unregisterService : error = " + e);
		}
	}
	
	@Override
	public ServiceInstanceRegisterStatus isRegistered() {
		//
		return this.registerStatus;
	}
	
	@Override
	public ServiceInstanceClusterStatus isClustered() {
		//
		return this.clusterStatus;
	}

	@Override
	public void entryAdded(EntryEvent<Object, Object> event) {
		//
		String serviceName = (String)event.getKey();
		ServiceInstance serviceInstance = (ServiceInstance)event.getValue();
		
		logger.info("[ServiceInstancesManager].entryAdded : serviceInstance = " + serviceInstance.toString());
		
		if (serviceInstancesCallback != null) {
			serviceInstancesCallback.registeredService(serviceName);
		}
		else {
			logger.error("[ServiceInstancesManager].entryAdded : error = serviceInstancesCallback is null");
		}
	}

	@Override
	public void entryRemoved(EntryEvent<Object, Object> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void entryUpdated(EntryEvent<Object, Object> event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, ServiceInstance> getServiceInstances() {
		//
		Map<String, ServiceInstance> retMap = new HashMap<>();
		try {
			IMap<Object, Object> resultMap = inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap());

			for (Entry<Object, Object> iter : resultMap.entrySet()) {
				retMap.put((String)iter.getKey(), (ServiceInstance)iter.getValue());
			}
			
			return retMap;
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].getServiceInstances : error = " + e);
			return retMap;
		}
	}
	
	@Override
	public Map<String, ServiceInstance> getOnlineServiceInstancesWithoutMe() {
		//
		Map<String, ServiceInstance> retMap = new HashMap<>();
		try {
			IMap<Object, Object> resultMap = inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap());

			for (Entry<Object, Object> iter : resultMap.entrySet()) {
				if (!iter.getKey().equals(this.serviceName)) {
					if ( ((ServiceInstance)iter.getValue()).getRegisterStatus().equals(ServiceInstanceRegisterStatus.REGISTERED)
						&& ((ServiceInstance)iter.getValue()).getClusterStatus().equals(ServiceInstanceClusterStatus.ONLINE) ) {
						//
						retMap.put((String)iter.getKey(), (ServiceInstance)iter.getValue());
					}
				}
			}
			
			return retMap;
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].getServiceInstancesWithoutMe : error = " + e);
			return retMap;
		}
	}
	
	public void checkHealth() {
		//
		Map<String, ServiceInstance> serviceInstances = getServiceInstances();
		
		String scheme = "http";
		
		String path = "timeline-service/management/service/health";
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		
		for (Entry<String, ServiceInstance> iter : serviceInstances.entrySet()) {
			//
			String serviceName = iter.getValue().getServiceInstanceName();
			String ip = iter.getValue().getIp();
			Integer port = iter.getValue().getPort();
			String url = ip + ":" + port.toString();
			
			ResponseEntity<String> response = httpProxyAdaptor.executeHttpProxy(scheme, url, path, null, HttpMethod.GET, headers, null);
			HttpStatus httpStatus = response.getStatusCode();
			String responseBody = response.getBody();
			
			if (!httpStatus.equals(HttpStatus.OK)) {
				try {
					ServiceInstance serviceInstance = (ServiceInstance)inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).get(serviceName);
					
					Long currentTime = System.currentTimeMillis();
					Long updatedTime = serviceInstance.getUpdatedTime().getTime();
					
					Long diffTime = currentTime - updatedTime;
					if (diffTime > healthCheckMissTermByMs) {
						serviceInstance.setClusterStatus(ServiceInstanceClusterStatus.OFFLINE);
						inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).put(serviceName, serviceInstance);
					}
				} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
					logger.error("[ServiceInstancesManager].checkHealth : error = " + e);
				}
			}
			else {
				try {
					ServiceHealth serviceHealth = om.readValue(responseBody, ServiceHealth.class);
					
					if (serviceHealth.getClusterStatus().equals(ServiceInstanceClusterStatus.RESTORED) || serviceHealth.getClusterStatus().equals(ServiceInstanceClusterStatus.ONLINE)) {
						//
						ServiceInstance serviceInstance = (ServiceInstance)inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).get(serviceName);
						
						serviceInstance.setRegisterStatus(serviceHealth.getRegisterStatus());
						serviceInstance.setClusterStatus(serviceHealth.getClusterStatus());
						serviceInstance.setUpdatedTime(serviceHealth.getTimestamp());
						
						inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).put(serviceName, serviceInstance);
					}
				} catch (JsonProcessingException | ExceptionHazelcastDataStructureNotFoundFailure e) {
					logger.error("[ServiceInstancesManager].checkHealth : error = " + e);
				}
			}
		}
	}
	
	private void initializeScheduler() {
		//
		Scheduler scheduler = schedulerManager.getScheduler();
		
		try {
			if(scheduler.isStarted()) {
				String schedJobName = "CHECK_HEALTH_JOB";
				String schedJobGroup = "CHECK_HEALTH_JOB_GROUP";
				String schedTriggerName = "CHECK_HEALTH_TRIGGER";
				String schedTriggerGroup = "CHECK_HEALTH_TRIGGER_GROUP";
				
				JobDetail job = newJob(this.getClass()).withIdentity(schedJobName, schedJobGroup).storeDurably(true).build();
				
				JobDataMap jobDataMap = job.getJobDataMap();
				jobDataMap.put("serviceInstancesManager", this);
				
				CronTrigger trigger = newTrigger().withIdentity(schedTriggerName, schedTriggerGroup).withSchedule(cronSchedule(healthCheckBatch)).forJob(schedJobName, schedJobGroup).build();
				
				scheduler.scheduleJob(job, trigger);
			}
		}
		catch (SchedulerException e) {
			logger.error("[ServiceInstancesManager].initializeScheduler : error = " + e);
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//
		//logger.info("[ServiceInstancesManager].execute : " + new Timestamp(System.currentTimeMillis()).toString());
		
		JobDetail job = (JobDetail)context.getJobDetail();
		ServiceInstancesManager serviceInstancesManager = (ServiceInstancesManager)job.getJobDataMap().get("serviceInstancesManager");
		
		serviceInstancesManager.checkHealth();
	}
}
package com.solitardj9.timelineService.service.serviceInstancesManager.service.impl;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.IMap;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceInMemoryMap;
import com.solitardj9.timelineService.service.serviceInstancesManager.model.ServiceInstanceParamEnum.ServiceInstanceStatus;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesCallback;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.model.InMemoryInstance;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryEventListener;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.InMemoryManager;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureCreationFailure;
import com.solitardj9.timelineService.systemInterface.inMemoryInterface.service.impl.exception.ExceptionHazelcastDataStructureNotFoundFailure;

@Service("serviceInstancesManager")
public class ServiceInstancesManagerImpl implements ServiceInstancesManager, InMemoryEventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceInstancesManagerImpl.class);
	
	@Autowired
	InMemoryManager inMemoryManager;
	
	@Value("${server.port}")
	private Integer port;
	
	private String ip;
	
	private Integer backupCount = 2;
	
	private Boolean readBackupData = true;
	
	private ServiceInstancesCallback serviceInstancesCallback;
	
	private String serviceName;
	
	@PostConstruct
	public void init() {
		//
		InMemoryInstance inMemoryInstance = new InMemoryInstance(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap(), backupCount, readBackupData, null, this);
		try {
			inMemoryManager.addMap(inMemoryInstance);
		} catch (ExceptionHazelcastDataStructureCreationFailure | ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].init : error = " + e.getStackTrace());
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
			//e.printStackTrace();
			logger.error("[ServiceInstancesManager].init : error = " + e.getStackTrace());
		}
	}
	
	@Override
	public void setServiceInstancesCallback(ServiceInstancesCallback serviceInstancesCallback) {
		this.serviceInstancesCallback = serviceInstancesCallback;
	}

	@Override
	public void registerService(String serviceName) {
		//
		try {
			if (!inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).containsKey(serviceName)) {
				ServiceInstance serviceInstance = new ServiceInstance(serviceName, ip, port, new Timestamp(System.currentTimeMillis()), ServiceInstanceStatus.ONLINE.getStatus());
				inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).put(serviceName, serviceInstance);
				this.serviceName = serviceName;
			}
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].registerService : error = " + e.getStackTrace());
		}
	}

	@Override
	public void unregisterService(String serviceName) {
		//
		try {
			inMemoryManager.getMap(ServiceInstanceInMemoryMap.SERVICE_INSTANCE.getMap()).remove(serviceName);
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].unregisterService : error = " + e.getStackTrace());
		}
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
			logger.error("[ServiceInstancesManager].getServiceInstances : error = " + e.getStackTrace());
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
					if (((ServiceInstance)iter.getValue()).getStatus().equals(ServiceInstanceStatus.ONLINE.getStatus())) {
						retMap.put((String)iter.getKey(), (ServiceInstance)iter.getValue());
					}
				}
			}
			
			return retMap;
		} catch (ExceptionHazelcastDataStructureNotFoundFailure e) {
			logger.error("[ServiceInstancesManager].getServiceInstancesWithoutMe : error = " + e.getStackTrace());
			return retMap;
		}
	}
	
	// TODO : add batch to check health interface of other nodes in cluster, 
	// for example call rest api  and miss 3 times then update cluster staus offline. 
}
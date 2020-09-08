package com.solitardj9.timelineService.application.serviceManager.service.impl;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.application.serviceManager.service.ServiceManager;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.NetworkInterfceManager;

@Service("serviceManager")
public class ServiceManagerImpl implements ServiceManager {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceManagerImpl.class);
	
	@Autowired
	NetworkInterfceManager networkInterfceManager;
	
	@Value("${service.consumer}")
	private String serviceConsumer;
	
	@Value("${service.consumerGroup}")
	private String serviceConsumerGroup;
	
	private String exchangeForCluster = "ex_cluster_";
	private String exchangeTypeForCluster = "fanout";
	private Boolean exchangeDurableForCluster = true;
	private Boolean exchangeAutoDeleteForCluster = false;
	
	private String queueForCluster = "queue_cluster_";
	private Boolean queueDurableForCluster = true;
	private Boolean queueExclusiveForCluster = true;
	private Boolean queueAutoDeleteForCluster = true;
	
	@PostConstruct
	public void init() {
		//
		exchangeForCluster = "ex_cluster_" + serviceConsumer;
		queueForCluster = "queue_cluster_" + serviceConsumer;
	}
	
	@Override
	public void startService() {
		// TODO Auto-generated method stub
		
		logger.info("[ServiceManager].startService : [" + new Timestamp(System.currentTimeMillis()) + "]");
		
		networkInterfceManager.createExchange(exchangeForCluster, exchangeTypeForCluster, exchangeDurableForCluster, exchangeAutoDeleteForCluster, null);
		
		networkInterfceManager.createQueue(queueForCluster, queueDurableForCluster, queueExclusiveForCluster, queueAutoDeleteForCluster, null);
	}

	@Override
	public void stopService() {
		// TODO Auto-generated method stub
		
		logger.info("[ServiceManager].stopService : [" + new Timestamp(System.currentTimeMillis()) + "]");
		
		networkInterfceManager.deleteExchange(exchangeForCluster);
		
		networkInterfceManager.deleteQueue(queueForCluster);
	}
}
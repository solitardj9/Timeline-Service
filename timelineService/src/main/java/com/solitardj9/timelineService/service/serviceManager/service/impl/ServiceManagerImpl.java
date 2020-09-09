package com.solitardj9.timelineService.service.serviceManager.service.impl;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesCallback;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.ServiceInstancesManager;
import com.solitardj9.timelineService.service.serviceInstancesManager.service.data.ServiceInstance;
import com.solitardj9.timelineService.service.serviceManager.service.ServiceManager;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.NetworkInterfceManager;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.ExchangeToPublish;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.QueueToListen;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.ToPublish;

@Service("serviceManager")
public class ServiceManagerImpl implements ServiceManager, ServiceInstancesCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceManagerImpl.class);
	
	@Autowired
	NetworkInterfceManager networkInterfceManager;
	
	@Autowired
	ServiceInstancesManager serviceInstancesManager;
	
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
	private Boolean queueExclusiveForCluster = false;
	private Boolean queueAutoDeleteForCluster = true;
	
	private ToPublish exchangeToPublish;
	private QueueToListen queueToListen;
	
	private String routingKey = "syncMessage";
	
	@PostConstruct
	public void init() {
		//
		exchangeForCluster = "ex_cluster_" + serviceConsumer;
		queueForCluster = "queue_cluster_" + serviceConsumer;
		
		exchangeToPublish = new ExchangeToPublish(exchangeForCluster, exchangeTypeForCluster, routingKey, exchangeDurableForCluster, exchangeAutoDeleteForCluster, null);
		queueToListen = new QueueToListen(queueForCluster, queueDurableForCluster, queueExclusiveForCluster, queueAutoDeleteForCluster, null);
	}
	
	@Override
	public void startService() {
		//
		logger.info("[ServiceManager].startService : [" + new Timestamp(System.currentTimeMillis()) + "]");
		
		serviceInstancesManager.setServiceInstancesCallback(this);
		
		networkInterfceManager.createExchange(((ExchangeToPublish)exchangeToPublish).getExchange(), ((ExchangeToPublish)exchangeToPublish).getType(), exchangeToPublish.isDurable(), exchangeToPublish.isAutoDelete(), exchangeToPublish.getArguments());
		
		networkInterfceManager.createQueue(queueToListen.getQueue(), queueToListen.isDurable(), queueToListen.isExclusive(), queueToListen.isAutoDelete(), queueToListen.getArguments());
		
		networkInterfceManager.createClients(exchangeToPublish, queueToListen);
		
		serviceInstancesManager.registerService(serviceConsumer);
		
		Map<String, ServiceInstance> serviceInstancesMap = serviceInstancesManager.getServiceInstances();
		for (Entry<String, ServiceInstance> iter : serviceInstancesMap.entrySet()) {
			//
			String tmpServicename = iter.getKey();
			if (!tmpServicename.equals(serviceConsumer)) {
				String otherExchangeForCluster = "ex_cluster_" + tmpServicename;
				networkInterfceManager.bindQueueWithExchange(queueForCluster, otherExchangeForCluster, routingKey);
			}
		}
	}

	@Override
	public void stopService() {
		//
		logger.info("[ServiceManager].stopService : [" + new Timestamp(System.currentTimeMillis()) + "]");
		
		networkInterfceManager.deleteExchange(exchangeForCluster);
		
		networkInterfceManager.deleteQueue(queueForCluster);
		
		networkInterfceManager.disconnectAdminClients();
		
		serviceInstancesManager.unregisterService(serviceConsumer);
	}

	@Override
	public void registeredService(String serviceName) {
		//
		if (!serviceName.equals(serviceConsumer)) {
			String otherExchangeForCluster = "ex_cluster_" + serviceName;
			networkInterfceManager.bindQueueWithExchange(queueForCluster, otherExchangeForCluster, routingKey);
		}
	}
}
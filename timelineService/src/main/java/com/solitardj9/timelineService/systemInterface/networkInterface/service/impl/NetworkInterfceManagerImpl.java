package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.systemInterface.networkInterface.service.NetworkInterfceManager;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.RabbitMQAdminClient;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientConnectionFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientDisconnectionFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeBindFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeDeclareFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeDeleteFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeUnbindFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientQueueDeclareFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientQueueDeleteFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.RabbitMQClient;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.exception.ExceptionRabbitMQClientConnectionFailure;

@Service("networkInterfceManager")
public class NetworkInterfceManagerImpl implements NetworkInterfceManager {
	//
	private static final Logger logger = LoggerFactory.getLogger(NetworkInterfceManagerImpl.class);
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.admin.connectionCount}")
	private Integer adminConnectionCount;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.client.connectionCount}")
	private Integer clientConnectionCount;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.client.recvChannel}")
	private Integer clientRecvChannel;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.client.sendChannel}")
	private Integer clientSendChannel;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.connection.host}")
	private String host;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.connection.tcpPort}")
	private Integer tcpPort;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.connection.managementPort}")
	private Integer managementPort;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.connection.id}")
	private String id;
	
	@Value("${systemInterface.networkInterface.networkInterfaceManager.rabbitMQ.connection.pw}")
	private String pw;
	
	private List<RabbitMQAdminClient> rabbitMQAdminClients;
	
	private List<RabbitMQClient> rabbitMQClients;
	
	private Random random = new Random();
	
	@PostConstruct
	public void init() {
		//
		rabbitMQAdminClients = new ArrayList<>();
		for (int i = 0 ; i < adminConnectionCount ; i++) {
			RabbitMQAdminClient rabbitMQAdminClient = new RabbitMQAdminClient(host, tcpPort, id, pw);
			try {
				rabbitMQAdminClient.connect();
			} catch (ExceptionRabbitMQAdminClientConnectionFailure e) {
				logger.error("[NetworkInterfceManager].init : error = " + e.toString());
			}
			rabbitMQAdminClients.add(rabbitMQAdminClient);
		}
	}

	@Override
	public void disconnectAdminClients() {
		//
		for (RabbitMQAdminClient rabbitMQAdminClient : rabbitMQAdminClients) {
			try {
				rabbitMQAdminClient.disconnect();
			} catch (ExceptionRabbitMQAdminClientDisconnectionFailure e) {
				logger.error("[NetworkInterfceManager].disconnectAdminClients : error = " + e.toString());
			}
		}
		rabbitMQAdminClients.clear();
	}
	
	private Integer getRandomIndex(Integer bound/*exclusive*/) {
		//
		return random.nextInt(bound);
	}

	@Override
	public void createExchange(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).createExchange(exchange, type, durable, autoDelete, arguments);
		} catch (ExceptionRabbitMQAdminClientExchangeDeclareFailure e) {
			logger.error("[NetworkInterfceManager].createExchange : error = " + e.toString());
		}
	}

	@Override
	public void deleteExchange(String exchange) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).deleteExchange(exchange);
		} catch (ExceptionRabbitMQAdminClientExchangeDeleteFailure e) {
			logger.error("[NetworkInterfceManager].createExchange : error = " + e.toString());
		}
	}

	@Override
	public void createQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).createQueue(queue, durable, exclusive, autoDelete, arguments);
		} catch (ExceptionRabbitMQAdminClientQueueDeclareFailure e) {
			logger.error("[NetworkInterfceManager].createQueue : error = " + e.toString());
		}
	}

	@Override
	public void deleteQueue(String queue) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).deleteQueue(queue);
		} catch (ExceptionRabbitMQAdminClientQueueDeleteFailure e) {
			logger.error("[NetworkInterfceManager].deleteQueue : error = " + e.toString());
		}
	}

	@Override
	public void bindQueueWithExchange(String queue, String exchange, String routingKey) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).bindQueueWithExchange(queue, exchange, routingKey);
		} catch (ExceptionRabbitMQAdminClientExchangeBindFailure e) {
			logger.error("[NetworkInterfceManager].bindQueueWithExchange : error = " + e.toString());
		}
	}

	@Override
	public void unbindQueueFromExchange(String queue, String exchange, String routingKey) {
		//
		try {
			rabbitMQAdminClients.get(getRandomIndex(rabbitMQAdminClients.size())).unbindQueueFromExchange(queue, exchange, routingKey);
		} catch (ExceptionRabbitMQAdminClientExchangeUnbindFailure e) {
			logger.error("[NetworkInterfceManager].bindQueueWithExchange : error = " + e.toString());
		}
	}
	
	@Override
	public void createClients(String exchangeToPublish, String queueToListen) {
		//
		rabbitMQClients = new ArrayList<>();
		for (int i = 0 ; i < clientConnectionCount ; i++) {
			RabbitMQClient rabbitMQClient = new RabbitMQClient(host, tcpPort, id, pw, clientRecvChannel, clientSendChannel, exchangeToPublish, queueToListen);
			try {
				rabbitMQClient.connect();
			} catch (ExceptionRabbitMQClientConnectionFailure e) {
				logger.error("[NetworkInterfceManager].createClients : error = " + e.toString());
			}
			rabbitMQClients.add(rabbitMQClient);
		}
	}
	
	
	
	
	
	
	
	
}

package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericRecvAck;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.GenericRecvMsg;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericRecvAckParam;
import com.solitardj9.timelineService.systemInterface.networkInterface.model.NetworkInterfaceParamEunm.GenericRecvMsgParam;
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
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.QueueToListen;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.RabbitMQClient;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.RabbitMQClientCallback;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.exception.ExceptionRabbitMQClientConnectionFailure;

@Service("networkInterfceManager")
public class NetworkInterfceManagerImpl implements NetworkInterfceManager, RabbitMQClientCallback {
	//
	private static final Logger logger = LoggerFactory.getLogger(NetworkInterfceManagerImpl.class);
	
	@Autowired
    ApplicationEventPublisher applicationEventPublisher;
	
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
	
	private Map<String, RabbitMQClient> rabbitMQClients;
	
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
	public void createClients(String exchangeToPublish, QueueToListen queueToListen) {
		//
		rabbitMQClients = new HashMap<>();
		for (int i = 0 ; i < clientConnectionCount ; i++) {
			RabbitMQClient rabbitMQClient = new RabbitMQClient(host, tcpPort, id, pw, clientRecvChannel, clientSendChannel, exchangeToPublish, queueToListen, this);
			try {
				rabbitMQClient.connect();
			} catch (ExceptionRabbitMQClientConnectionFailure e) {
				logger.error("[NetworkInterfceManager].createClients : error = " + e.toString());
			}
			rabbitMQClients.put(rabbitMQClient.getClientId(), rabbitMQClient);
		}
	}

	@Override
	public void onMessage(String clientId, String consumerTag, String message) {
		// TODO Auto-generated method stub
		if (applicationEventPublisher == null) {
			logger.info("[NetworkInterfceManager].onMessage : cosunmerTag = " + consumerTag + " / message = " + message);
		}
		else {
			Map<String, Object> data = new HashMap<>();
			data.put(GenericRecvMsgParam.CLIENT_ID.getParam(), clientId);
			data.put(GenericRecvMsgParam.CONSUMER_TAG.getParam(), consumerTag);
			data.put(GenericRecvMsgParam.MESSAGE.getParam(), message);
			
			applicationEventPublisher.publishEvent(new GenericRecvMsg(data));
		}
	}
	
	@EventListener
	@Async
	public void onGenericRecvAckEvent(GenericRecvAck genericRecvAck) {
		//
		logger.info("[NetworkInterfceManager].onGenericRecvAckEvent : " + genericRecvAck.toString());

		String clientId = (String)genericRecvAck.getDataValue(GenericRecvAckParam.CLIENT_ID.getParam());
		String consumerTag = (String)genericRecvAck.getDataValue(GenericRecvAckParam.CONSUMER_TAG.getParam());
		
		rabbitMQClients.get(clientId).onMessageAck(consumerTag);
	}
	
	
	
	
	
	
}
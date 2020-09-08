package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.exception.ExceptionRabbitMQClientConnectionFailure;

public class RabbitMQClient {
	//
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);
	
	private String host;
	
	private Integer port;
	
	private String id;
	
	private String pw;
	
	private Integer recvChannelCount;
	
	private Integer sendChannelCount;
	
	private Connection connection;
	
	private Map<String/*Consumer Tag*/, RabbitMQComsumer> recvRabbitMQComsumers = new HashMap<>();
	private List<Channel> sendChannels;
	
	private String exchangeToPublish;
	private String queueToListen;
	
	private Integer connectionTimeout = 3000;		// ms
	
	private Random random = new Random();
	
	public RabbitMQClient() {
		
	}
	
	public RabbitMQClient(String host, Integer port, String id, String pw, Integer recvChannelCount, Integer sendChannelCount, String exchangeToPublish, String queueToListen) {
		//
		this.host = host;
		this.port = port;
		this.id = id;
		this.pw = pw;
		this.recvChannelCount = recvChannelCount;
		this.sendChannelCount = sendChannelCount;
		this.exchangeToPublish = exchangeToPublish;
		this.queueToListen = queueToListen;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public Integer getRecvChannelCount() {
		return recvChannelCount;
	}

	public void setRecvChannelCount(Integer recvChannelCount) {
		this.recvChannelCount = recvChannelCount;
	}

	public Integer getSendChannelCount() {
		return sendChannelCount;
	}

	public void setSendChannelCount(Integer sendChannelCount) {
		this.sendChannelCount = sendChannelCount;
	}
	
	public String getExchangeToPublish() {
		return exchangeToPublish;
	}

	public void setExchangeToPublish(String exchangeToPublish) {
		this.exchangeToPublish = exchangeToPublish;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	// Auto Reconnect
	public void connect() throws ExceptionRabbitMQClientConnectionFailure {
		//
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(id);
		connectionFactory.setPassword(pw);
		
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setConnectionTimeout(connectionTimeout);
		
		try {
			connection = connectionFactory.newConnection();
			
			for (int i = 0 ; i < recvChannelCount ; i++) {
				RabbitMQComsumer rabbitMQComsumer = new RabbitMQComsumer();
				String consumerTag = rabbitMQComsumer.createRabbitMQComsumer(this, queueToListen, connection.createChannel());
				recvRabbitMQComsumers.put(consumerTag, rabbitMQComsumer);
				
				System.out.println("[RabbitMQClient].connect : consumerTag = " + consumerTag);
			}
			
//			for (int i = 0 ; i < sendChannelCount ; i++) {
//				Channel tmpChannel = connection.createChannel();
//				sendChannels.add(tmpChannel);
//			}
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
			logger.error("[RabbitMQClient].connect : error = " + e.toString());
			throw new ExceptionRabbitMQClientConnectionFailure();
		}
	}
	
//	public void disconnect() throws ExceptionRabbitMQAdminClientDisconnectionFailure {
//		//
//		try {
//			if (channel != null) {
//				channel.close();
//			}
//			if (connection != null) {
//				connection.close();
//			}
//		} catch (IOException | TimeoutException e) {
//			logger.error("[RabbitMQAdminClient].disconnect : error = " + e.toString());
//			throw new ExceptionRabbitMQAdminClientDisconnectionFailure();
//		}
//	}
	
	
	
	private Integer getRandomIndex(Integer bound/*exclusive*/) {
		//
		return random.nextInt(bound);
	}

	public void onMessage(String consumerTag, String message) {
		//
		logger.info("[RabbitMQClient].onMessage : cosunmerTag = " + consumerTag + " / message = " + message);
	}
	
}
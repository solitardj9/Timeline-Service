package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientDisconnectionFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.QueueToListen;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.ToPublish;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.exception.ExceptionRabbitMQClientConnectionFailure;

public class RabbitMQClient implements RecoveryListener {
	//
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQClient.class);
	
	private String clientId;
	
	private String host;
	
	private Integer port;
	
	private String id;
	
	private String pw;
	
	private Integer recvChannelCount;
	
	private Integer sendChannelCount;
	
	private Connection connection;
	
	private Map<String/*Consumer Tag*/, RabbitMQComsumer> recvRabbitMQComsumers = new HashMap<>();
	private Map<String/*Publisher Tag*/, RabbitMQPublisher> sendRabbitMQPublishers = new HashMap<>();
	
	private ToPublish toPublish;
	private QueueToListen queueToListen;
	
	private RabbitMQClientCallback rabbitMQClientCallback;
	
	private Integer connectionTimeout = 3000;		// ms
	
	private Random random = new Random();
	
	public RabbitMQClient() {
		
	}
	
	public RabbitMQClient(String host, Integer port, String id, String pw, Integer recvChannelCount, Integer sendChannelCount, ToPublish toPublish, QueueToListen queueToListen, RabbitMQClientCallback rabbitMQClientCallback) {
		//
		clientId = UUID.randomUUID().toString().toString();
		
		this.host = host;
		this.port = port;
		this.id = id;
		this.pw = pw;
		
		this.recvChannelCount = recvChannelCount;
		this.sendChannelCount = sendChannelCount;
		
		this.toPublish = toPublish;
		this.queueToListen = queueToListen;
		
		this.rabbitMQClientCallback = rabbitMQClientCallback;
	}
	
	public String getClientId() {
		return clientId;
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
	
	public ToPublish getToPublish() {
		return toPublish;
	}

	public void setToPublish(ToPublish toPublish) {
		this.toPublish = toPublish;
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
		
		// enable automatic recovery
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setConnectionTimeout(connectionTimeout);
		
		// disable topology recovery
		connectionFactory.setTopologyRecoveryEnabled(false);
		
		try {
			connection = connectionFactory.newConnection();
			
			((com.rabbitmq.client.Recoverable)connection).addRecoveryListener(this);
			
			makeTopology();
			

		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
			logger.error("[RabbitMQClient].connect : clientId = " + clientId + " / error = " + e.toString());
			throw new ExceptionRabbitMQClientConnectionFailure();
		}
	}
	
	private void makeTopology() throws ExceptionRabbitMQClientConnectionFailure {
		//
		logger.info("[RabbitMQClient].makeTopology : clientId = " + clientId);
		
		try {
			for (int i = 0 ; i < recvChannelCount ; i++) {
				RabbitMQComsumer rabbitMQComsumer = new RabbitMQComsumer();
				String consumerTag = rabbitMQComsumer.createRabbitMQComsumer(this, queueToListen, connection.createChannel());
				recvRabbitMQComsumers.put(consumerTag, rabbitMQComsumer);
			}
			
			for (int i = 0 ; i < sendChannelCount ; i++) {
				RabbitMQPublisher rabbitMQPublisher = new RabbitMQPublisher();
				String publisherTag = rabbitMQPublisher.createRabbitMQPublisher(this, toPublish, connection.createChannel());
				sendRabbitMQPublishers.put(publisherTag, rabbitMQPublisher);
			}
		} catch (IOException e) {
			//e.printStackTrace();
			logger.info("[RabbitMQClient].makeTopology : clientId = " + clientId + " / error = " + e.toString());
			throw new ExceptionRabbitMQClientConnectionFailure();
		}
	}
	
	private void recoveryTopology() throws ExceptionRabbitMQClientConnectionFailure {
		//
		logger.info("[RabbitMQClient].recoveryTopology is called : clientId = " + clientId);
		
		try {
			for (Entry<String, RabbitMQComsumer> iter : recvRabbitMQComsumers.entrySet()) {
				if (iter.getValue().getChannel().isOpen()) {
					iter.getValue().getChannel().abort();
				}
			}
			recvRabbitMQComsumers.clear();
			
			for (Entry<String, RabbitMQPublisher> iter : sendRabbitMQPublishers.entrySet()) {
				if (iter.getValue().getChannel().isOpen()) {
					iter.getValue().getChannel().abort();
				}
			}
			sendRabbitMQPublishers.clear();
		} catch (IOException  e) {
			//e.printStackTrace();
			logger.info("[RabbitMQClient].recoveryTopology : clientId = " + clientId + " / error = " + e.toString());
			throw new ExceptionRabbitMQClientConnectionFailure();
		}
		
		makeTopology();
	}
	
	public void disconnect() throws ExceptionRabbitMQAdminClientDisconnectionFailure {
		//
		try {
			for (Entry<String, RabbitMQComsumer> iter : recvRabbitMQComsumers.entrySet()) {
				if (!iter.getValue().getChannel().isOpen()) {
					iter.getValue().getChannel().abort();
				}
			}
			recvRabbitMQComsumers.clear();
			
			for (Entry<String, RabbitMQPublisher> iter : sendRabbitMQPublishers.entrySet()) {
				if (!iter.getValue().getChannel().isOpen()) {
					iter.getValue().getChannel().abort();
				}
			}
			sendRabbitMQPublishers.clear();
			
			if (connection != null) {
				connection.close();
			}
		} catch (IOException e) {
			logger.error("[RabbitMQAdminClient].disconnect : error = " + e.toString());
			throw new ExceptionRabbitMQAdminClientDisconnectionFailure();
		}
	}
	
	private Integer getRandomIndex(Integer bound/*exclusive*/) {
		//
		return random.nextInt(bound);
	}

	public void onMessage(String consumerTag, String message) {
		// 
		if (rabbitMQClientCallback == null) {
			logger.info("[RabbitMQClient].onMessage : clientId = " + clientId + " / cosunmerTag = " + consumerTag + " / message = " + message);
		}
		else {
			rabbitMQClientCallback.onMessage(clientId, consumerTag, message);
		}
	}
	
	public void onMessageAck(String consumerTag) {
		//
		recvRabbitMQComsumers.get(consumerTag).ack();
	}
	
	public Boolean publishMessage(String routingKey, String message) {
		//
		Set<String> keySet = sendRabbitMQPublishers.keySet();
		Integer index = getRandomIndex(sendRabbitMQPublishers.size());
		String publisherTag = (String) keySet.toArray()[index];
		
		Boolean result = false;
		try {
			result = sendRabbitMQPublishers.get(publisherTag).publish(routingKey, message);
		} catch (IOException | TimeoutException e) {
			//e.printStackTrace();
			logger.info("[RabbitMQClient].onMessage : clientId = " + clientId + " / publisherTag = " + publisherTag + " / error = " + e.toString());
			return result;
		}
		return result;
	}
	
	public Boolean publishMessage(String message) {
		//
		Set<String> keySet = sendRabbitMQPublishers.keySet();
		Integer index = getRandomIndex(sendRabbitMQPublishers.size());
		String publisherTag = (String) keySet.toArray()[index];
		
		Boolean result = false;
		try {
			result = sendRabbitMQPublishers.get(publisherTag).publish(message);
		} catch (IOException | TimeoutException e) {
			//e.printStackTrace();
			logger.info("[RabbitMQClient].onMessage : clientId = " + clientId + " / publisherTag = " + publisherTag + " / error = " + e.toString());
			return result;
		}
		
		return result;
	}

	@Override
	public void handleRecovery(Recoverable recoverable) {
		//
		logger.info("[RabbitMQClient].handleRecovery : clientId = " + clientId + " / recoverable = " + recoverable.toString());
		
		try {
			Thread.sleep(1000);
			
			recoveryTopology();
		} catch (ExceptionRabbitMQClientConnectionFailure | InterruptedException e) {
			//e.printStackTrace();
			logger.error("[RabbitMQClient].handleRecovery : error = " + e.toString());
		}
	}

	@Override
	public void handleRecoveryStarted(Recoverable recoverable) {
		// TODO Auto-generated method stub
		logger.info("[RabbitMQClient].handleRecoveryStarted : clientId = " + clientId + " / recoverable = " + recoverable.toString());
	}
	
}
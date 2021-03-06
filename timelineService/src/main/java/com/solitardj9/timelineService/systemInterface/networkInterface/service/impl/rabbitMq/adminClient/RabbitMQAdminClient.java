package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AlreadyClosedException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientConnectionFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientDisconnectionFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeBindFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeDeclareFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeDeleteFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientExchangeUnbindFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientQueueDeclareFailure;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.adminClient.exception.ExceptionRabbitMQAdminClientQueueDeleteFailure;

public class RabbitMQAdminClient {
	//
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQAdminClient.class);
	
	private String host;
	
	private Integer port;
	
	private String id;
	
	private String pw;
	
	private Connection connection;
	
	private Channel channel;
	
	private Integer connectionTimeout = 3000;		// ms
	
	public RabbitMQAdminClient() {
		
	}
	
	public RabbitMQAdminClient(String host, Integer port, String id, String pw) {
		//
		this.host = host;
		this.port = port;
		this.id = id;
		this.pw = pw;
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
	
	// Auto Reconnect
	public void connect() throws ExceptionRabbitMQAdminClientConnectionFailure {
		//
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(id);
		connectionFactory.setPassword(pw);
		
		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setConnectionTimeout(connectionTimeout);
		
		Integer requestedHeartbeat = 1;
		connectionFactory.setRequestedHeartbeat(requestedHeartbeat);
		
		try {
			connection = connectionFactory.newConnection();
			channel = connection.createChannel();
		} catch (IOException | TimeoutException e) {
			logger.error("[RabbitMQAdminClient].connect : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientConnectionFailure();
		}
	}
	
	public void disconnect() throws ExceptionRabbitMQAdminClientDisconnectionFailure {
		//
		try {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (IOException | TimeoutException e) {
			logger.error("[RabbitMQAdminClient].disconnect : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientDisconnectionFailure();
		}
	}
	
	public com.rabbitmq.client.AMQP.Exchange.DeclareOk createExchange(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments) throws ExceptionRabbitMQAdminClientExchangeDeclareFailure {
		//
		try {
			return channel.exchangeDeclare(exchange, type, durable, autoDelete, arguments);
		} catch (IOException e) {
			logger.error("[RabbitMQAdminClient].createExchange : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientExchangeDeclareFailure();
		}
	}
	
	public com.rabbitmq.client.AMQP.Exchange.DeleteOk deleteExchange(String exchange) throws ExceptionRabbitMQAdminClientExchangeDeleteFailure {
		//
		try {
			return channel.exchangeDelete(exchange);
		} catch (IOException | AlreadyClosedException e) {
			logger.error("[RabbitMQAdminClient].deleteExchange : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientExchangeDeleteFailure();
		}
	}
	
	public com.rabbitmq.client.AMQP.Queue.DeclareOk createQueue(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws ExceptionRabbitMQAdminClientQueueDeclareFailure {
		//
		try {
			return channel.queueDeclare(queue, durable, exclusive, autoDelete, arguments);
		} catch (IOException | AlreadyClosedException e) {
			logger.error("[RabbitMQAdminClient].createQueue : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientQueueDeclareFailure();
		}
	}

	public com.rabbitmq.client.AMQP.Queue.DeleteOk deleteQueue(String queue) throws ExceptionRabbitMQAdminClientQueueDeleteFailure {
		//
		try {
			return channel.queueDelete(queue);
		} catch (IOException | AlreadyClosedException e) {
			logger.error("[RabbitMQAdminClient].createQueue : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientQueueDeleteFailure();
		}
	}
	
	public com.rabbitmq.client.AMQP.Queue.BindOk bindQueueWithExchange(String queue, String exchange, String routingKey) throws ExceptionRabbitMQAdminClientExchangeBindFailure {
		//
		try {
			return channel.queueBind(queue, exchange, routingKey);
		} catch (IOException | AlreadyClosedException e) {
			logger.error("[RabbitMQAdminClient].createQueue : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientExchangeBindFailure();
		}
	}

	public com.rabbitmq.client.AMQP.Queue.UnbindOk unbindQueueFromExchange(String queue, String exchange, String routingKey) throws ExceptionRabbitMQAdminClientExchangeUnbindFailure {
		//
		try {
			return channel.queueUnbind(queue, exchange, routingKey);
		} catch (IOException | AlreadyClosedException e) {
			logger.error("[RabbitMQAdminClient].createQueue : error = " + e.getStackTrace());
			throw new ExceptionRabbitMQAdminClientExchangeUnbindFailure();
		}
	}
}
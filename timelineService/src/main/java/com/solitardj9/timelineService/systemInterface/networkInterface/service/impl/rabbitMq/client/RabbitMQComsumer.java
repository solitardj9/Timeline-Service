package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConsumerShutdownSignalCallback;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class RabbitMQComsumer implements DeliverCallback, CancelCallback, ConsumerShutdownSignalCallback {
	//
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQComsumer.class);
	
	private String consumerTag;
	
	private Channel channel;
	
	private RabbitMQClient rabbitMQClient;
	
	public String createRabbitMQComsumer(RabbitMQClient rabbitMQClient, String queue, Channel channel) {
    	//
		this.rabbitMQClient = rabbitMQClient;
    	this.channel = channel;
    	
    	try {
			this.channel.basicQos(1);
			this.consumerTag = this.channel.basicConsume(queue, false, this, this, this);
			return this.consumerTag;
		} catch (IOException e) {
			logger.error("[RabbitMQComsumer].RabbitMQComsumer : error = " + e.toString());
			return null;
		}
    }
    @Override
	public void handle(String consumerTag, Delivery message) throws IOException {
    	//
    	String strMessage = new String(message.getBody(), "UTF-8");
    	
		logger.info("[RabbitMQComsumer].handle : DeliverCallback cosunmerTag = " + consumerTag + " / message = " + strMessage);
		
		rabbitMQClient.onMessage(consumerTag, strMessage);
		
		channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
	}

	@Override
	public void handle(String consumerTag) throws IOException {
		// TODO Auto-generated method stub
		logger.info("[RabbitMQComsumer].handle : CancelCallback cosunmerTag = " + consumerTag);
	}

	@Override
	public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
		// TODO Auto-generated method stub
		logger.info("[RabbitMQComsumer].handleShutdownSignal : ShutdownSignal cosunmerTag = " + consumerTag + " / signale = " + sig.toString());
	}
}
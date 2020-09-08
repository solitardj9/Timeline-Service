package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.Queue.DeclareOk;
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
	
	private Delivery message;
	
	public Channel getChannel() {
		return this.channel;
	}
	
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public String createRabbitMQComsumer(RabbitMQClient rabbitMQClient, QueueToListen queueToListen, Channel channel) {
    	//
		this.rabbitMQClient = rabbitMQClient;
    	this.channel = channel;
    	
    	try {
    		DeclareOk declareOk = null;
    		while (true) {
    			//
    			declareOk = this.channel.queueDeclare(queueToListen.getQueue(), queueToListen.isDurable(), queueToListen.isExclusive(), queueToListen.isAutoDelete(), queueToListen.getArguments());
    			if (declareOk.getQueue().equals(queueToListen.getQueue())) {
    				break;
    			}
    			Thread.sleep(500);
    		}
    		
			this.channel.basicQos(1);
			this.consumerTag = this.channel.basicConsume(queueToListen.getQueue(), false, this, this, this);
			return this.consumerTag;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.error("[RabbitMQComsumer].RabbitMQComsumer : error = " + e.toString());
			return null;
		}
    }
	
    @Override
	public void handle(String consumerTag, Delivery message) throws IOException {
    	//
    	this.message = message;
    	
    	String strMessage = new String(message.getBody(), "UTF-8");
    	
		//logger.info("[RabbitMQComsumer].handle : DeliverCallback cosunmerTag = " + consumerTag + " / message = " + strMessage);
		
		rabbitMQClient.onMessage(consumerTag, strMessage);
	}
    
    public void ack() {
    	//
    	try {
			channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("[RabbitMQComsumer].ack : error = " + e.toString());
		}
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
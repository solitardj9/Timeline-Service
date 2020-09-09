package com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.ExchangeToPublish;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.QueueToPublish;
import com.solitardj9.timelineService.systemInterface.networkInterface.service.impl.rabbitMq.client.data.ToPublish;

public class RabbitMQPublisher {
	//
	private static final Logger logger = LoggerFactory.getLogger(RabbitMQPublisher.class);
	
	private String publisherTag;
	
    private Channel channel;
    
    private RabbitMQClient rabbitMQClient;
    
    private ToPublish toPublish;
    
    public Channel getChannel() {
		return this.channel;
	}
    
    public void setChannel(Channel channel) {
		this.channel = channel;
	}

    @SuppressWarnings("unused")
	public String createRabbitMQPublisher(RabbitMQClient rabbitMQClient, ToPublish toPublish, Channel channel) {
    	//
		this.rabbitMQClient = rabbitMQClient;
    	this.channel = channel;
    	this.toPublish = toPublish;
    	
    	try {
    		
    		while (true) {
    			//
    			if (this.toPublish instanceof ExchangeToPublish) {
    				com.rabbitmq.client.AMQP.Exchange.DeclareOk declareOk = null;
	    			try {
	    				declareOk = this.channel.exchangeDeclare(((ExchangeToPublish)toPublish).getExchange(), ((ExchangeToPublish)toPublish).getType(), toPublish.isDurable(), toPublish.isAutoDelete(), toPublish.getArguments());
	    			} catch (Exception e) {
	    				logger.error("[RabbitMQPublisher].createRabbitMQPublisher : error = " + ((ExchangeToPublish)toPublish).getExchange() + " is not yet declared");
	    				try {
							Thread.sleep(500);
						} catch (InterruptedException ie) {
							logger.error("[RabbitMQPublisher].createRabbitMQPublisher : error = " + ie.toString());
						}
	    			}
	    			break;
    			}
    			else {
    				com.rabbitmq.client.AMQP.Queue.DeclareOk declareOk = null;
    	    		while (true) {
    	    			//
    	    			declareOk = this.channel.queueDeclare(((QueueToPublish)toPublish).getQueue(), toPublish.isDurable(), ((QueueToPublish)toPublish).isExclusive(), toPublish.isAutoDelete(), toPublish.getArguments());
    	    			if (declareOk.getQueue().equals(((QueueToPublish)toPublish).getQueue())) {
    	    				break;
    	    			}
    	    			Thread.sleep(500);
    	    		}
    			}
    		}
    		
    		this.publisherTag = UUID.randomUUID().toString(); 
			return this.publisherTag;
		} catch (Exception e) {
			//e.printStackTrace();
			logger.error("[RabbitMQPublisher].createRabbitMQPublisher : error = " + e.toString());
			return null;
		}
    }
    
    // TODO : confirm listener를 어떻게 붙어야 할까나
    
    public void publish(String routingKey, String message) throws IOException, TimeoutException {
        //
    	try {
	    	if (this.toPublish instanceof ExchangeToPublish) {
	    		channel.basicPublish(((ExchangeToPublish)toPublish).getExchange(), routingKey, null, message.getBytes("UTF-8"));
	    	}
	    	else {
	    		channel.basicPublish("", ((QueueToPublish)toPublish).getQueue(), null, message.getBytes("UTF-8"));
	    	}
    	} catch (com.rabbitmq.client.AlreadyClosedException e) {
    		//
    		Connection connection = ((com.rabbitmq.client.impl.recovery.AutorecoveringChannel)channel).getConnection();
        	((com.rabbitmq.client.impl.recovery.AutorecoveringConnection) connection).getDelegate().flush();
		    
        	//e.printStackTrace();
        	logger.error("[RabbitMQPublisher].publish : error = " + e.toString());
		
		} catch (Exception e) {
			//
			Connection connection = ((com.rabbitmq.client.impl.recovery.AutorecoveringChannel)channel).getConnection();
	    	((com.rabbitmq.client.impl.recovery.AutorecoveringConnection) connection).getDelegate().flush();
	    	
		    //e.printStackTrace();
	    	logger.error("[RabbitMQPublisher].publish : error = " + e.toString());
		}
    }
    
    public void publish(String message) throws IOException, TimeoutException {
        //
    	try {
	    	if (this.toPublish instanceof ExchangeToPublish) {
	    		channel.basicPublish(((ExchangeToPublish)toPublish).getExchange(), ((ExchangeToPublish)toPublish).getRoutingKey(), null, message.getBytes("UTF-8"));
	    	}
	    	else {
	    		channel.basicPublish("", ((QueueToPublish)toPublish).getQueue(), null, message.getBytes("UTF-8"));
	    	}
    	} catch (com.rabbitmq.client.AlreadyClosedException e) {
    		//
    		Connection connection = ((com.rabbitmq.client.impl.recovery.AutorecoveringChannel)channel).getConnection();
        	((com.rabbitmq.client.impl.recovery.AutorecoveringConnection) connection).getDelegate().flush();
		    
        	//e.printStackTrace();
        	logger.error("[RabbitMQPublisher].publish : error = " + e.toString());
		
		} catch (Exception e) {
			//
			Connection connection = ((com.rabbitmq.client.impl.recovery.AutorecoveringChannel)channel).getConnection();
	    	((com.rabbitmq.client.impl.recovery.AutorecoveringConnection) connection).getDelegate().flush();
	    	
		    //e.printStackTrace();
	    	logger.error("[RabbitMQPublisher].publish : error = " + e.toString());
		}
    }
}
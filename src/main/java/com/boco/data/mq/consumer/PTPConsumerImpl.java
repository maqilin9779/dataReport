package com.boco.data.mq.consumer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class PTPConsumerImpl implements PTPConsumer {
	ConnectionFactory connectionFactory;
	
	Connection connection;
	
	Session session;
	
	MessageConsumer messageConsumer;
	
	public static String brokerURL = "failover://tcp://192.168.243.129:61616";
	
	public static final String userName = ActiveMQConnection.DEFAULT_USER;
	
	public static final String password = ActiveMQConnection.DEFAULT_PASSWORD;
	
	public void init() {
		try {
			connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerURL);
			
			connection = connectionFactory.createConnection();
			
			connection.start();
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getMessage(String queueName) {
		try {
			Queue queue = session.createQueue(queueName);
			messageConsumer = session.createConsumer(queue);
			
			while(true){
				TextMessage msg = (TextMessage)messageConsumer.receive();
				if(msg!=null){
					System.out.println("收到的消息是："+msg.getText());
				}else{
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		
	}

}

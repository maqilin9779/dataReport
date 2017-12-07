package com.boco.data.mq.provider;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class PTPProviderImpl implements PTPProvider {
	
	ConnectionFactory connectionFactory;
	
	Connection connection;
	
	Session session;
	
	MessageProducer messageProducer;
	
	public static String brokerURL = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	public static final String userName = ActiveMQConnection.DEFAULT_USER;
	
	public static final String password = ActiveMQConnection.DEFAULT_PASSWORD;

	public void init() {
		try {
			System.out.println("brokerURL="+brokerURL);
			brokerURL = "failover://tcp://192.168.243.129:61616";
			//创建连接工厂
			connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerURL);
			//创建连接
			connection = connectionFactory.createConnection();
			
			connection.start();
			//创建session
			session = connection.createSession(true,Session.SESSION_TRANSACTED);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void sendMessage(String disname) {
		try {
			Queue queue = session.createQueue(disname);
			
			messageProducer = session.createProducer(queue);
			
			for(int i = 0; i < 10 ;i ++){
				TextMessage textMessage = session.
						createTextMessage("我是xx平台需要发送短信，短信内容：content:"+i);
				System.out.println("我是xx平台需要发送短信，短信内容：content:"+i);
				messageProducer.send(textMessage);
			}
			session.commit();
		} catch (JMSException e) {
			e.printStackTrace();
		}finally{
			if(connection!=null){
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

package com.boco.data.mq.provider;

import com.boco.data.mq.consumer.PTPConsumer;
import com.boco.data.mq.consumer.PTPConsumerImpl;

public class ConsumerTest {
	
	public static void main(String[] args) {
		PTPConsumer consumer = new PTPConsumerImpl();
		consumer.init();
		consumer.getMessage("QUEUE1");
	}

}

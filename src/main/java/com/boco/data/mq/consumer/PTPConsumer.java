package com.boco.data.mq.consumer;

public interface PTPConsumer {
	
	/**
	 * 初始化
	 */
	void init();
	
	/**
	 * 获取消息
	 * @param queueName
	 */
	void getMessage(String queueName);

}

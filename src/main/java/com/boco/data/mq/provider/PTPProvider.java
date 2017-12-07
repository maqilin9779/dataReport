package com.boco.data.mq.provider;

public interface PTPProvider {
	
	/**
	 * 初始化的方法
	 */
	void init();
	
	/**
	 * 发送消息
	 * @param disname
	 */
	void sendMessage(String disname);
}

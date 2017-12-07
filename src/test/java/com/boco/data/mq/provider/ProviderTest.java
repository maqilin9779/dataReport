package com.boco.data.mq.provider;

public class ProviderTest {
	
	public static void main(String[] args) {
		PTPProvider provider = new PTPProviderImpl();
		provider.init();
		provider.sendMessage("QUEUE1");
		
	}

}

package com.boco.data.freamework.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *  获取spring注册的bean
 * @author Administrator
 *
 */
public class ApplicationContextHolder {
	//实例
	private static ApplicationContextHolder applicationContextHolder;
	//spring上下文
	private ApplicationContext ac;
	//构造器私有化
	private ApplicationContextHolder(){}
	//获取唯一实例
	public static ApplicationContextHolder getInstance(){
		if(applicationContextHolder==null){
			applicationContextHolder = new ApplicationContextHolder();
		}
		return applicationContextHolder;
	}
	//获取注册的bean
	public Object getBean(String beanId){
		if(ac==null){
			ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		}
		return ac.getBean(beanId);
	}
}

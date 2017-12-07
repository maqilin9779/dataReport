package com.boco.data.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/redis")
public class RedisTest {
	
	private Logger logger = Logger.getLogger(RedisTest.class);
	
	@Autowired
	private JedisCluster jedisCluster;
	
	@RequestMapping("/add")
	public @ResponseBody String testAdd(){
		logger.info("进入redis测试控制器");
		jedisCluster.set("name", "maqi"); 
		return "add ok";
	}

}

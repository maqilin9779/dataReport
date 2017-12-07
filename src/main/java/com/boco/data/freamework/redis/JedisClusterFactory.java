package com.boco.data.freamework.redis;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisClusterFactory implements FactoryBean<JedisCluster>,
					InitializingBean{
	
	private Logger logger = Logger.getLogger(JedisClusterFactory.class);
	
	private String prefix;
	
	private JedisCluster jedisCluster;
	
	private Resource resource;
	
	private GenericObjectPoolConfig genericObjectPoolConfig;
	
	private Set<HostAndPort> jedisClusterNode;
	
	private int connectionTimeout;
	
	private int maxRedirections;
	
	//用正则表达式匹配
	private Pattern pattern = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
	
	public JedisCluster getObject() throws Exception {
		return jedisCluster;
	}

	public Class<? extends JedisCluster> getObjectType() {
		return jedisCluster==null?JedisCluster.class:jedisCluster.getClass();
	}

	public boolean isSingleton() {
		return true;
	}
	
	//实例化之前就会被执行一次
	public void afterPropertiesSet() throws Exception {
		
		logger.info("实例化jedisCluster");
		parseHostAndPort();
		//实例化jedisCluster
		jedisCluster = new JedisCluster(
				jedisClusterNode,connectionTimeout,
				maxRedirections,genericObjectPoolConfig
				);
	}
	

	private void parseHostAndPort() throws IOException {
		Properties props = new Properties();
		props.load(resource.getInputStream());
		//遍历
		jedisClusterNode = new HashSet<HostAndPort>();
		String value = "";
		HostAndPort hostAndPort;
		for(Object key:props.keySet()){
			
			if(!String.valueOf(key).startsWith(prefix)){
				continue;
			}
			value = (String) props.get(key);
			
			if(!pattern.matcher(value).matches()){
				throw new IllegalArgumentException("ip 或 port 不合法");
			}
			String [] ipAndPort = value.split(":");
			hostAndPort = new HostAndPort(ipAndPort[0],new Integer(ipAndPort[1]));
			jedisClusterNode.add(hostAndPort);
		}
	}
	
	public static void main(String[] args) {
		Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");
		String regStr = "^.+[:]\\d{1,5}\\s*$";
		System.out.println(p.matcher("123.2:2").matches());
		System.out.println("123.2:2".matches(regStr));
		
	}
	
	

	public JedisCluster getJedisCluster() {
		return jedisCluster;
	}

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public GenericObjectPoolConfig getGenericObjectPoolConfig() {
		return genericObjectPoolConfig;
	}

	public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
		this.genericObjectPoolConfig = genericObjectPoolConfig;
	}

	public Set<HostAndPort> getJedisClusterNode() {
		return jedisClusterNode;
	}

	public void setJedisClusterNode(Set<HostAndPort> jedisClusterNode) {
		this.jedisClusterNode = jedisClusterNode;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	

}

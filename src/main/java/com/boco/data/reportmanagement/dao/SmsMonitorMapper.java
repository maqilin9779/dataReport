package com.boco.data.reportmanagement.dao;

import java.util.List;

import com.boco.data.reportmanagement.entity.SmsMonitor;

/**
 * 对短信表sms_monitor的操作
 * @author Administrator
 *
 */
public interface SmsMonitorMapper {
	
	/**
	 * 批量插入短信数据到sms_monitor
	 * @param smsMonitorList
	 * @return
	 */
	public void batchAdd(List<SmsMonitor> smsMonitorList);

}

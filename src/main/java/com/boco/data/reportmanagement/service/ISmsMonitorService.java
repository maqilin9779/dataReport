package com.boco.data.reportmanagement.service;

import java.util.List;

import com.boco.data.reportmanagement.entity.SmsMonitor;
/**
 * 对sms_monitor操作的service
 * @author Administrator
 *
 */
public interface ISmsMonitorService {
	/**
	 * 批量插入短信数据到sms_monitor
	 * @param smsMonitorList
	 * @return
	 */
	public void batchAdd(List<SmsMonitor> smsMonitorList);
}

package com.boco.data.reportmanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.data.reportmanagement.dao.SmsMonitorMapper;
import com.boco.data.reportmanagement.entity.SmsMonitor;
import com.boco.data.reportmanagement.service.ISmsMonitorService;
@Service
public class SmsMonitorService implements ISmsMonitorService{
	
	@Autowired
	private SmsMonitorMapper smsMonitorMapper;

	public void batchAdd(List<SmsMonitor> smsMonitorList) {
		smsMonitorMapper.batchAdd(smsMonitorList);
	}

}

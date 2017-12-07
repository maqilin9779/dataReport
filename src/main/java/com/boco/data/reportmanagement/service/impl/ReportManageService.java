package com.boco.data.reportmanagement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.data.reportmanagement.dao.ReportManageMapper;
import com.boco.data.reportmanagement.entity.ReportManagementMsg;
import com.boco.data.reportmanagement.service.IReportManageService;

@Service
public class ReportManageService implements IReportManageService{
	
	@Autowired
	private ReportManageMapper reportManageMapper;

	public List<ReportManagementMsg> getReportByCondition(String circle, 
			String frequency) {
			return reportManageMapper.getReportByCondition(circle, frequency);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

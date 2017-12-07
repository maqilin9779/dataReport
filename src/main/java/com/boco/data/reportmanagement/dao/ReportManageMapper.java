package com.boco.data.reportmanagement.dao;

import java.util.List;

import com.boco.data.reportmanagement.entity.ReportManagementMsg;
/**
 * 报表短信管理dao
 * @author Administrator
 *
 */
public interface ReportManageMapper{
	/**
	 * @param circle：区别获取短信配置的类型，
	 * 			例如获取每月的提醒、每周的提醒、每半年的提醒、每季度、每年等等
	 * @param frequency 具体上传的日期
	 * @return
	 */
	public List<ReportManagementMsg> getReportByCondition
							(String circle,String frequency);
}

package com.boco.data.reportmanagement.quartz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.boco.data.reportmanagement.entity.ReportManagementMsg;
import com.boco.data.reportmanagement.entity.SmsMonitor;
import com.boco.data.reportmanagement.service.IReportManageService;
import com.boco.data.reportmanagement.service.impl.SmsMonitorService;
import com.boco.data.reportmanagement.util.FormatFileName;
/**
 * EOMS报表管理，短信提醒功能
 * 该类功能：将需要发送短信的新插入sms_monitor表
 * 难点判断：哪些报表是需要发送短信提醒的。报表短信配置的周期有年、月、周、季度
 * 目前该类实现的配置周期为月的短信提醒，还有年、周、季度待完善。
 * @author Administrator
 */
public class ReportSmsDayTask {
	
	@Autowired
	private IReportManageService reportManageService;
	
	@Autowired
	private SmsMonitorService smsMonitorService;
	
	//轮询的方法
	@SuppressWarnings("deprecation")
	public void work(){
		try {
		System.out.println("====================ReportSmsDayTask=进入work===================================");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date date = new Date();
		String day = sdf.format(date);
		String dayOfMonth = df.format(date);
		//String today = day + " 00:00:00";//今天2017-11-13 00:00:00
		String month = dayOfMonth +"-01 00:00:00";//本月第一天2017-11-01 00:00:00
		//1040601 表示月
		String time ="1040601"+day.substring(8);
	    List<ReportManagementMsg> msgList = 
	    		reportManageService.getReportByCondition("1040601", time);
	    Iterator<ReportManagementMsg> it = msgList.iterator();
	    ReportManagementMsg msg = null;
	    SmsMonitor sms = null;
	    Random r = new Random();
	    List<SmsMonitor> smsMonitorList = new ArrayList<SmsMonitor>();
	    while(it.hasNext()){
	    	msg = it.next();
	    	Timestamp timestamp = msg.getUploadtime();
	    	if(timestamp==null||new Date(timestamp.getTime()).
	    						before(sf.parse(month))){
				sms = new SmsMonitor();
				sms.setId("ff00"+r.nextInt(100000));//id
				sms.setServiceId("ff8080814d4638c5014d4c6eec370363");//serviceId
				sms.setBuizid("_TKI:a01b015d.730bcb17.feffff80.13b19afdSecondExcuteHumTask");
				sms.setReceiverId("KFZX");//结束短信的人
				sms.setDispatchTime(new Date());//派发时间
				sms.setMobile(msg.getMobile());//手机号
				String fileName = msg.getFilename();
				fileName = FormatFileName.formatName("1040601", fileName);
				sms.setContent(fileName);//报表名称
				sms.setIsSendImediat("false");
				sms.setRegetData("false");
				sms.setDeleted("0");
				smsMonitorList.add(sms);
			}
	    }
	    System.out.println("smsMonitorList大小=="+smsMonitorList.size());
	    //批量插入短信表
	    if(smsMonitorList.size()>0){
	    	smsMonitorService.batchAdd(smsMonitorList);
		    smsMonitorList.clear();
	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    
	}
	
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date line = sdf.parse("2017-11-01 00:00:00");
		Date uploadTime = sdf.parse("2017-11-13 00:00:00");
		Date now = sdf.parse("2017-11-13 00:00:00");
		System.out.println(uploadTime.before(now));//上传时间在今天之前
		System.out.println(uploadTime.after(line));//这个月内上传的
		//从未上传或者上传时间在本月之前的就发短信提醒
		if(uploadTime==null || uploadTime.before(line)){
			System.out.println("---------------------");
		}
	}

}

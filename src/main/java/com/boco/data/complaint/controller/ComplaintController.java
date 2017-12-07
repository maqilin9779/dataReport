package com.boco.data.complaint.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boco.data.reportmanagement.entity.ReportManagementMsg;
import com.boco.data.reportmanagement.entity.SmsMonitor;
import com.boco.data.reportmanagement.service.IReportManageService;
import com.boco.data.reportmanagement.service.impl.SmsMonitorService;
import com.boco.data.reportmanagement.util.FormatFileName;

@Controller
public class ComplaintController {
	
	@Autowired
	private IReportManageService reportManageService;
	
	@Autowired
	private SmsMonitorService smsMonitorService;
	
	//轮询的方法
	@SuppressWarnings("unchecked")
	@RequestMapping("/test")
	public void work() throws ParseException{

		try {
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
	    System.out.println(msgList);
	    Iterator<ReportManagementMsg> it = msgList.iterator();
	    ReportManagementMsg msg = null;
	    SmsMonitor sms = null;
	    Random r = new Random(1);
	    List<SmsMonitor> smsMonitorList = new ArrayList<SmsMonitor>();
	    while(it.hasNext()){
	    	msg = it.next();
	    	Timestamp timestamp = msg.getUploadtime();
	    	if(timestamp==null||new Date(timestamp.getTime()).
	    						before(sf.parse(month))){
				System.out.println("---------应该发短信------------");
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
	    //批量插入短信表
	    smsMonitorService.batchAdd(smsMonitorList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

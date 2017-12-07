package com.boco.data.complaint.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;
import com.boco.data.complaint.uitl.DataReportUtil;
import com.boco.data.freamework.util.ApplicationContextHolder;
import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;
/**
 * 业务执行的类
 * @author Administrator
 *
 */

public class GroupComplaintDataReport {
	//开始执行的方法
	public void work(){
		System.out.println("groupComplaintDataReport======");
		IDownLoadSheetAccessiorsDAO id = (IDownLoadSheetAccessiorsDAO) 
		ApplicationContextHolder.getInstance().getBean("IDownLoadSheetAccessiorsDAO");
//		设置路径名称 
		String xmlPath = "/config/groupComplaint-datareport.xml";
		//获取省份编码
		String provinceCode = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty("provinceCode"));
		//获取配置文件中的sql语句
		String sql = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".sql"));
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
		Calendar c = Calendar.getInstance();
		Date date=new Date();
		c.setTime(date);
		c.add(c.DATE,-1);
		date = c.getTime();
		String day = df.format(date);
		String beginTime = day + " 00:00:00";
		String endTime = day + " 23:59:59";
		//替换sql中的时间
		sql = sql.replaceAll("@beginTime@",beginTime);
		sql = sql.replaceAll("@endTime@",endTime);
		System.out.println("GroupComplaintDataReport.sql="+sql);
		//调用sql查询出list集合
		List list;
		try {
			list = id.getSheetAccessoriesList(sql);
		
	  //定义生成csv文件的第一行字段
	  LinkedHashMap rowMapper = new LinkedHashMap();
      rowMapper.put("1", "chargeName");
      rowMapper.put("2", "chargePhone");
      rowMapper.put("3", "province_name");
      rowMapper.put("4", "city_name");
      rowMapper.put("5", "orderId");
      rowMapper.put("6", "orderTheme");
      rowMapper.put("7", "customerId");
      rowMapper.put("8", "customerName");
      rowMapper.put("9", "customerLev");
      rowMapper.put("10", "customerLevId");
      rowMapper.put("11", "customerType");
      rowMapper.put("12", "customerTypeId");
      rowMapper.put("13", "busType");
      rowMapper.put("14", "busTypeId");
      rowMapper.put("15", "isLargeComplain");
      rowMapper.put("16", "complainDetail");
      rowMapper.put("17", "complainNbr");
      rowMapper.put("18", "proId");
      rowMapper.put("19", "circuitName");
      rowMapper.put("20", "assuranceLev");
      rowMapper.put("21", "assuranceLevId");
      rowMapper.put("22", "isAlarmMonitor");
      rowMapper.put("23", "alarmId");
      rowMapper.put("24", "isAlarmOrder");
      rowMapper.put("25", "alarmOrderId");
      rowMapper.put("26", "complainStartTime");
      rowMapper.put("27", "AlarmStartTime");
      rowMapper.put("28", "orderStartTime");
      rowMapper.put("29", "limitTime");
      rowMapper.put("30", "orderEndTime");
      rowMapper.put("31", "complainResult");
      rowMapper.put("32", "complainResultId");
      rowMapper.put("33", "orderState");
      rowMapper.put("34", "orderStateId");
      rowMapper.put("35", "isContactCustomer");
      rowMapper.put("36", "contactCustomerFirstTime");
      rowMapper.put("37", "pretreatmentTime");
      rowMapper.put("38", "preanalysisReason");
      rowMapper.put("39", "isSucPreanalysis");
      rowMapper.put("40", "taskTransferTime");
      rowMapper.put("41", "maintenanceCompany");
      rowMapper.put("42", "maintenanceStartTime");
      rowMapper.put("43", "maintenanceDetail");
      rowMapper.put("44", "maintenanceResult");
      rowMapper.put("45", "isvisite");
      rowMapper.put("46", "arriveSceneTime");
      rowMapper.put("47", "removeAlarmTime");
      rowMapper.put("48", "recoFaultTime");
      rowMapper.put("49", "faulteChargeableCityName");
      rowMapper.put("50", "isoverTime");
      rowMapper.put("51", "overTimeResion");
      rowMapper.put("52", "problemResion");
      rowMapper.put("53", "problemResionId");
      rowMapper.put("54", "isConfirmResult");
      rowMapper.put("55", "qcResult");
      rowMapper.put("56", "qcDetail");
      rowMapper.put("57", "faultType");
      rowMapper.put("58", "faultTypeId");
      rowMapper.put("59", "accessWay");
      rowMapper.put("60", "enterpriseId");
      rowMapper.put("61", "serviceId");
      rowMapper.put("62", "isVolumeFault");
      rowMapper.put("63", "terminalType");
      rowMapper.put("64", "calledNumberCityName");
      rowMapper.put("65", "ecsiType");
      rowMapper.put("66", "accgateIp");
      rowMapper.put("67", "accgateId");
      rowMapper.put("68", "mpiagSendMegTime");
      rowMapper.put("69", "isSusResponse");
      rowMapper.put("70", "mpiagResReportTime");
      rowMapper.put("71", "isSucResReport");
      rowMapper.put("72", "apnName");
      rowMapper.put("73", "tunnelType");
      rowMapper.put("74", "vpnInstantiation");
      rowMapper.put("75", "ceIp");
      rowMapper.put("76", "customerIp");
	  //生成固定格式的csv文件
	  File csvFile = DataReportUtil.createCsvFile(list, rowMapper,xmlPath,"TSOrder",provinceCode);
	  //获取配置文件中的ftp配置
	  String url = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".url"));
	  int port =Integer.parseInt(StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".port")));
	  String username = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".username"));
	  String password = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".password"));
	  String path = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".path"));
	  //以100M为标准分割文件并上传至ftp指定位置
	  DataReportUtil.splitFile(csvFile, url, port, username, password, path);
	  } catch (Exception e) {
		 e.printStackTrace();
	  }
    }
}

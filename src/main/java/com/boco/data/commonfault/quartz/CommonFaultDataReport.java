package com.boco.data.commonfault.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.boco.data.base.service.IDownLoadSheetAccessoriesService;
import com.boco.data.complaint.uitl.DataReportUtil;
import com.boco.data.freamework.util.ApplicationContextHolder;
import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;

/**
 * 故障工单每天上报集团
 * @author Administrator
 *
 */
public class CommonFaultDataReport{
	
	public void work() throws Exception {
		IDownLoadSheetAccessoriesService id = (IDownLoadSheetAccessoriesService) 
		ApplicationContextHolder.getInstance().getBean("IDownLoadSheetAccessoriesService");
		//设置路径名称
		String xmlPath = "/config/commonFault-datareport.xml";
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
		System.out.println("CommonFaultDataReport.sql="+sql);
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
      rowMapper.put("5", "faultOrderId");
      rowMapper.put("6", "orderTheme");
      rowMapper.put("7", "customerId");
      rowMapper.put("8", "customerName");
      rowMapper.put("9", "customerLev");
      rowMapper.put("10", "customerLevId");
      rowMapper.put("11", "customerType");
      rowMapper.put("12", "customerTypeId");
      rowMapper.put("13", "busType");
      rowMapper.put("14", "busTypeId");
//      rowMapper.put("15", "isLargeComplain");
//      rowMapper.put("16", "complainDetail");
//      rowMapper.put("17", "complainNbr");
      rowMapper.put("15", "proId");
      rowMapper.put("16", "circuitName");
      rowMapper.put("17", "assuranceLev");
      rowMapper.put("18", "assuranceLevId");
//      rowMapper.put("22", "isAlarmMonitor");
      rowMapper.put("19", "alarmId");
      rowMapper.put("20", "alarmNet");
      rowMapper.put("21", "alarmEquipmentId");
      rowMapper.put("22", "alarmEquipmentCompany");
      rowMapper.put("23", "alarmEquipmentType");
      rowMapper.put("24", "alarmName");
      rowMapper.put("25", "alarmType");
      rowMapper.put("26", "alarmDetail");
//      rowMapper.put("25", "alarmOrderId");
//      rowMapper.put("26", "complainStartTime");
      rowMapper.put("27", "alarmStartTime");
      rowMapper.put("28", "orderStartTime");
      rowMapper.put("29", "limitTime");
      rowMapper.put("30", "orderEndTime");
      rowMapper.put("31", "faultResult");
      rowMapper.put("32", "faultResultId");
      rowMapper.put("33", "orderState");
      rowMapper.put("34", "orderStateId");
      rowMapper.put("35", "isContactCustomer");
      rowMapper.put("36", "contactCustomerFirstTime");
      rowMapper.put("37", "pretreatmentTime");
      rowMapper.put("38", "preanalysisReason");
      rowMapper.put("39", "preanalysisReasonId");
      rowMapper.put("40", "isSucPreanalysis");
      rowMapper.put("41", "taskTransferTime");
      rowMapper.put("42", "maintenanceCompany");
      rowMapper.put("43", "maintenanceStartTime");
      rowMapper.put("44", "maintenanceDetail");
      rowMapper.put("45", "maintenanceResult");
      rowMapper.put("46", "maintenanceResultId");
      rowMapper.put("47", "isvisite");
      rowMapper.put("48", "arriveSceneTime");
      rowMapper.put("49", "removeAlarmTime");
      rowMapper.put("50", "recoFaultTime");
      rowMapper.put("51", "faulteChargeableCityName");
      rowMapper.put("52", "isoverTime");
      rowMapper.put("53", "overTimeResion");
      rowMapper.put("54", "problemResion");
      rowMapper.put("55", "isConfirmResult");
      rowMapper.put("56", "qcResult");
      rowMapper.put("57", "qcDetail");
      rowMapper.put("58", "faultType");
      rowMapper.put("59", "faultTypeId");
      rowMapper.put("60", "accessWay");
      rowMapper.put("61", "enterpriseId");
      rowMapper.put("62", "serviceId");
      rowMapper.put("63", "isVolumeFault");
      rowMapper.put("64", "terminalType");
      rowMapper.put("65", "calledNumberCityName");
      rowMapper.put("66", "ecsiType");
      rowMapper.put("67", "accgateIp");
      rowMapper.put("68", "accgateId");
      rowMapper.put("69", "mpiagSendMegTime");
      rowMapper.put("70", "isSusResponse");
      rowMapper.put("71", "mpiagResReportTime");
      rowMapper.put("72", "isSucResReport");
      rowMapper.put("73", "apnName");
      rowMapper.put("74", "tunnelType");
      rowMapper.put("75", "vpnInstantiation");
      rowMapper.put("76", "ceIp");
      rowMapper.put("77", "customerIp");
////    获取配置文件中的省份代码
//      String provinceCode = StaticMethod.nullObject2String(XmlManage.getFile("/config/commonFault-datareport.xml").getProperty("fujian.provinceCode"));
      //生成固定格式的csv文件
	  File csvFile = DataReportUtil.createCsvFile(list, rowMapper,xmlPath,"GZOrder",provinceCode);
	  //获取配置文件中的ftp配置
	  String url = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".url"));
	  int port =Integer.parseInt(StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".port")));
	  String username = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".username"));
	  String password = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".password"));
	  String path = StaticMethod.nullObject2String(XmlManage.getFile(xmlPath).getProperty(provinceCode+".path"));
	  //以100M为标准分割文件并上传至ftp指定位置
	  DataReportUtil.splitFile(csvFile, url, port, username, password, path);
	  System.out.println("/////////////////////////上传完毕");
	  } catch (Exception e) {		
			e.printStackTrace();
		}
	}
}
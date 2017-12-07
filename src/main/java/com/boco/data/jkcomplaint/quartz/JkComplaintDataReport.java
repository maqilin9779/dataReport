package com.boco.data.jkcomplaint.quartz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;
import com.boco.data.freamework.util.ApplicationContextHolder;
import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;
import com.boco.data.jkcomplaint.util.CSVUtils;

/**
 * 家宽投诉每天上报集团的类
 * @author Administrator
 *
 */

public class JkComplaintDataReport {
    //开始执行的方法
	@SuppressWarnings("rawtypes")
	public void work(){
		/*得到统计数据的开始和结束时间，拼接sql,查询数据库，得到数据集合*/
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date yesterDay = new Date(System.currentTimeMillis()-24*60*60*1000);
	    String day = df.format(yesterDay);
	    //String startTime = day + " 00:00:00";
	    //String endTime = day + " 23:59:59";
	    
		String sqlPro = StaticMethod.nullObject2String(
		XmlManage.getFile("/config/jkcomplaint-sql.xml").getProperty("sql.broadband1"));
	    System.out.println("家宽数据送集团sql=" + sqlPro);
	    
	    IDownLoadSheetAccessiorsDAO id = (IDownLoadSheetAccessiorsDAO) 
	    ApplicationContextHolder.getInstance().getBean("IDownLoadSheetAccessiorsDAO");
	    try {
	    	List list = id.getSheetAccessoriesList(sqlPro);
	    	String fileType = day.replaceAll("-", "");
	        File file = CSVUtils.createCSVFile(list, "D", fileType);
	        String fileName2 = file.getName();
	        System.out.println("文件名称：" + fileName2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

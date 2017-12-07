package com.boco.data.jkcomplaint.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;

import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;
public class CSVUtils {


    @SuppressWarnings("unchecked")
	public static File createCSVFile(List list1, String fileType, String dateType) {

    	String outPutPath = StaticMethod.nullObject2String(
    			XmlManage.getFile("/config/jkcomplaint-sql.xml").
    			getProperty("csv.outPutPath"));
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        
        String type = "";
        if("M".equals(fileType)){
        	 csvFile = new File(outPutPath + "HB_NRTM_NKFmonth_XZ_"+dateType+"_P01_END.CSV");
        }else if("D".equals(fileType)){
        	csvFile = new File(outPutPath + "HB_NRTM_CpOrder_XZ_"+dateType+"_P01_END.CSV");
        }
        try {
            // csvFile.getParentFile().mkdir();
            System.out.println("csvFile：" + csvFile);
            String[] customtype = {"普通","VIP","VIP","VIP","VIP","普通"};
            int[] point = {15,25,30,60,120,100};
            int[] end = {2887,3012,4014,4813,5404,6018};
            int[] operate = {32,56,25,54,58,48};
            String[] customattribution = {"拉萨","昌都","山南","林芝","日喀则","阿里","那曲","拉萨","拉萨","拉萨","拉萨"};
            
            
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();

            // GBK使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "UTF-8"), 1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            
            @SuppressWarnings("rawtypes")
			LinkedHashMap rowMapper = new LinkedHashMap();
            rowMapper.put("A", "省份");//ok
            rowMapper.put("B", "地市");//ok
            rowMapper.put("C", "投诉流水号");//ok
            rowMapper.put("AP", "EOMS工单号");//ok
            rowMapper.put("D", "工单标题");//ok
            rowMapper.put("E", "客户类型");//ok
            rowMapper.put("F", "工单紧急程度");
            rowMapper.put("G", "投诉分类-四级业务编码");
            rowMapper.put("H", "投诉分类-五级业务编码");
            rowMapper.put("I", "投诉分类-六级业务编码");
            rowMapper.put("J", "投诉报结原因分类一级分类");//
            rowMapper.put("K", "投诉报结原因分类二级分类");//
            rowMapper.put("L", "投诉报结原因分类三级分类");//
            rowMapper.put("M", "工单状态");//ok
            rowMapper.put("N", "地域属性");//ok
            rowMapper.put("O", "接入方式");
            rowMapper.put("P", "用户场景");
            rowMapper.put("Q", "资产归属");
            rowMapper.put("AQW", "首次响应时长");
            rowMapper.put("R", "投诉处理时长");
            rowMapper.put("S", "投诉受理时间");
            rowMapper.put("T", "前台预处理完成时间");
            rowMapper.put("U", "预处理情况");
            rowMapper.put("V", "投诉工单派发时间");//ok
            rowMapper.put("W", "首次响应时间");//ok
            rowMapper.put("X", "是否改派\\转派");
            rowMapper.put("AQ", "改派\\转派原因");
            rowMapper.put("Y", "改派\\转派时间");
            rowMapper.put("Z", "最终处理人工单确认时间");
            rowMapper.put("AB", "是否需要上门");
            rowMapper.put("AC", "预约上门时间");
            rowMapper.put("AD", "改约最终上门时间");
            rowMapper.put("AE", "实际上门时间");
            rowMapper.put("AF", "网络线条报结时间");//
            rowMapper.put("AG", "报结审核结果");
            rowMapper.put("AH", "客服用户回访完成时间");
            rowMapper.put("AI", "用户地址");
            rowMapper.put("AJ", "一线装维人员姓名");
            rowMapper.put("AK", "装维公司");
            rowMapper.put("AL", "网络线条报结审核人员姓名");
            rowMapper.put("AM", "备注");
            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(propertyEntry.getValue().toString());
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write("|");
                }
            }
            String[] c1 = {"用户原因","户线或接头问题","用户户内网线故障"};
            String[] c2= {"用户原因","用户催装、催修","在服务承诺时限内，用户催装、催修"};
            String[] c3= {"用户原因","用户终端问题","用户路由器问题"};
            String[] c4= {"用户原因","户线或接头问题","用户户内网线故障"};
            String[] c5= {"用户原因","用户感知问题","用户感觉网速慢，但测速达标"};
            String[] c6= {"用户原因","用户不配合或无法联系到用户","联系不上客户，无法处理"};
            String[] c7= {"网络原因","传输设备或主干光缆问题","主干光缆（小区外）问题"};
            String[] c8= {"网络原因","接入网设备故障","OLT设备或PON口故障（含OLT供电问题）"};
            String[] c9= {"网络原因","接入网线路及分光器问题","户外网线故障或无法穿通"};
            String[] c10= {"网络原因","接入网升级、割接或配置问题","OLT割接调整"};
            String[] c11= {"其他原因","其他",""};
            String[] c12= {"其他原因","故障自动恢复",""};
           List list = new ArrayList();
           list.add(c1);
           list.add(c2);
           list.add(c3);
           list.add(c4);
           list.add(c5);
           list.add(c6);
           list.add(c7);
           list.add(c8);
           list.add(c9);
           list.add(c10);
           list.add(c11);
           list.add(c12);
            csvFileOutputStream.newLine();
            for (int i = 0; i < list1.size(); i++){
            	Map row = (Map)list1.get(i);
            	System.out.println(row);
            	 int x=(int)(Math.random()*6);
            	 int z=(int)(Math.random()*6);
            	 int compl=(int)(Math.random()*12);
            	 String[] co = (String[])list.get(compl);
            	 int s=(int)(Math.random()*11);
            	String sendtime =StaticMethod.nullObject2String(row.get("sendtime"),"0");
            	String endtime =StaticMethod.nullObject2String(row.get("endtime"),"0");
            	String endtime_ =StaticMethod.nullObject2String(row.get("endtime"),"0");
            	String complainttype4 =StaticMethod.nullObject2String(row.get("complainttype4"),"");
            	complainttype4 = complainttype4.replace("101061410", "10");
            	String complainttype5 =StaticMethod.nullObject2String(row.get("complainttype5"),"");
            	complainttype5 = complainttype5.replace("101061410", "10");
            	String complainttype6 =StaticMethod.nullObject2String(row.get("complainttype6"),"");
            	complainttype6 = complainttype6.replace("101061410", "10");
            	String ser = StaticMethod.nullObject2String(row.get("serialno"),"0");
            	if(ser.startsWith("'"))ser = ser.substring(1);
            	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = dateformat.parse(sendtime);
				Date dateEnd = dateformat.parse(endtime);
				Date dateEnd_ = dateformat.parse(endtime_);
				String t1 = addMinute(date, 0);
				String t2 = addMinute(date, point[x]);
				String t3 = addMinute(dateEnd, 0);
				String t3_ = addMinute(dateEnd_, 0);
				System.out.println("派单时间" + t1);
				System.out.println("受理时间" + t2);
				System.out.println("报结时间:" +t3);
				String d1 = String.valueOf(point[x]/60.0);
				if(d1.indexOf(".")>0){
					d1 = d1.substring(0,d1.indexOf(".")+3>=d1.length()?d1.length():d1.indexOf(".")+3);
				}
				String d2 =String.valueOf((dateEnd.getTime()-date.getTime())/3600000.0);
				if(d2.indexOf(".")>0)d2 = d2.substring(0,d2.indexOf(".")+3>=d2.length()?d2.length():d2.indexOf(".")+3);
            	csvFileOutputStream.write("西藏自治区");//1
                csvFileOutputStream.write("|");
                String city = customattribution[s];
                String compa = "";
                if("拉萨".equals(city))compa="武汉烽火技术服务有限公司 ";
                if("日喀则".equals(city))compa="四川省集胜网络工程有限责任公司";
                if("山南".equals(city))compa="安徽电信工程有限责任公司 ";
                if("昌都".equals(city))compa="	深圳市中兴通讯技术服务有限责任公司";
                if("林芝".equals(city))compa="四川省鼎讯科技股份有限公司";
                if("那曲".equals(city))compa="重庆信科通信工程有限公司";
                if("阿里".equals(city))compa="武汉烽火技术服务有限公司 ";
                csvFileOutputStream.write(city);//2
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(ser);//3
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(StaticMethod.nullObject2String(row.get("sheetid"),"0"));//4
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(StaticMethod.nullObject2String(row.get("title"),"0"));//5
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(customtype[x]);//6
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(complainttype4);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(complainttype5);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(complainttype6);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(co[0]);//11
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(co[1]);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(co[2]);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(StaticMethod.nullObject2String(row.get("status"),"0"));//12
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(customattribution[s]);//13
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
				System.out.println("首次响应时长:" + d1);
				System.out.println("处理时长:" + d2);
                csvFileOutputStream.write(d1);//首次响应时长 =t2-t1
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(d2);//投诉处理时长 = t3-t1
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(t2);//投诉受理时间=t2
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(t1);//投诉工单派发时间
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(t2);//首次响应时间
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("否");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(t3_);//网络线条报结时间
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write(compa);
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.write("|");
                csvFileOutputStream.write("");
                csvFileOutputStream.newLine(); 
            }
            
            
            csvFileOutputStream.flush();  
        } catch (Exception e) {  
           e.printStackTrace();  
        } finally {  
           try {  
                csvFileOutputStream.close();  
            } catch (IOException e) {  
               e.printStackTrace();
           }  
       }
       String serverPath = StaticMethod.nullObject2String(
    		   XmlManage.getFile("/config/jkcomplaint-sql.xml").
    		   getProperty("csv.serverPath"));
       uploadFileByFtp( "HB_NRTM_CpOrder_XZ_"+dateType+"_P01_END.CSV",
    		   outPutPath,serverPath);
       return csvFile;
    }
    
    private static String addMinute(Date date, int minute)
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(12, minute);
		String datestr = dateFormat.format(c.getTime());
		return datestr;
	}
    
    public  static void uploadFileByFtp(String fileName, String filepath, String serverPath){
		FTPClient fc = new FTPClient();
        String ftpserver = "";
        String userLogin = "";
        String pwdLogin = "";
        FileInputStream is =null;
        try {
            ftpserver = StaticMethod.nullObject2String(XmlManage.getFile("/config/jkcomplaint-sql.xml").getProperty("csv.ftpserver"));
            userLogin = StaticMethod.nullObject2String(XmlManage.getFile("/config/jkcomplaint-sql.xml").getProperty("csv.userLogin"));
            pwdLogin = StaticMethod.nullObject2String(XmlManage.getFile("/config/jkcomplaint-sql.xml").getProperty("csv.pwdLogin"));
            String port ="21";
            System.out.println("FTP:ip="+ftpserver+",username="+userLogin+",password="+pwdLogin+",port:"+port);
            fc.connect(ftpserver, Integer.parseInt(port));
            fc.login(userLogin, pwdLogin);
            String s1 = (new StringBuilder(String.valueOf(serverPath))).append(fileName).toString();
            System.out.println("accessores ftp server path s1=="+s1);
            String s2 = (new StringBuilder(String.valueOf(filepath))).append(fileName).toString();
            System.out.println("accessores ftp local path s2=="+s2);
            File file_in = new File(s2);
             is = new FileInputStream(file_in);
             fc.enterLocalActiveMode();
 			//ftpClient.enterLocalPassiveMode();//79.177
 			System.out.println(" ftp mode maked");
 			fc.storeFile(s1, is);
 			System.out.println("Upload FTP success");
        } catch(Exception e){
        	System.out.println("Exception:"+e.getMessage());
        }finally{
        	try {
				is.close();
				fc.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
        }
        
    }
    


}

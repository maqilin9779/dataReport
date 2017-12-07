package com.boco.data.complaint.uitl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.jdom.Element;

import com.boco.data.base.dao.ITawSystemDictTypeDao;
import com.boco.data.freamework.util.ApplicationContextHolder;
import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;
public class DataReportUtil {
	
	/**
	 * 
	 * @param list  		传入的数据集合
	 * @param rowMapper		写入文件的第一行LinkedHashMap集合
	 * @param filePath		配置文件地址，xml文件的相对路径，"/"代表web-inf/classess
	 * @param jobType		工单类型
	 * @param provinceCode	省份代码
	 * @return
	 */
	public static File createCsvFile(List list,LinkedHashMap rowMapper,String filePath,String jobType,String provinceCode){
		//定义生成的文件
		File csvFile = null;
		//定义字符输出流
        BufferedWriter csvFileOutputStream = null;
        //获取当前日历
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar   cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);
        String currentDateString = format.format(cal.getTime());
        try {
        	//获取配置文件中配置的文件在本地的生成地址
        	String csvAdd = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty("csvFile"));
        	//定义文件名格式并创建
            csvFile = new File( csvAdd,"JK_NRTM_" + jobType + "_" +provinceCode+"_"+currentDateString+"_P01_END" + ".csv");
            System.out.println( "开始生成文件:"+csvAdd);
            // csvFile.getParentFile().mkdir();
            File parent = csvFile.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            csvFile.createNewFile();
            // utf-8读取文件
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(csvFile), "utf-8"), 1024);

            for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(propertyEntry.getValue().toString());
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write("|");
                }
            }
            //换行
            csvFileOutputStream.newLine();	
            System.out.println( "表头写入完毕");
            //写入内容数据
            //获取配置文件中配置的省公司负责人及其电话,环节名称组成的字符串，和各环节名称
            String chargeName = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".chargeName"));
            String chargePhone = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".chargePhone"));
            String provinceName = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".provinceName"));
            String processNames = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".processLink"));
            String checkingHumTask = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".qualityChecking"));
            String holdHumTask = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".holdTask"));
            String dateFormate = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".dateFormate"));
            String dictName = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".dictName"));
            
            //获取需要转化的数据库字典值
            String dicts = StaticMethod.nullObject2String(XmlManage.getFile(filePath).getProperty(provinceCode+".dicts"));
            //获取到的时间格式字段，字典字段
            List dateList =  StaticMethod.getArrayList(dateFormate, ",");
            List dictList =  StaticMethod.getArrayList(dictName, ",");
            List dictsList = StaticMethod.getArrayList(dicts, ",");
            for(int i = 0;i < list.size();i++){
            	HashMap row = (HashMap)list.get(i); 
            	for (Iterator propertyIterator = rowMapper.entrySet().iterator(); propertyIterator.hasNext();) {
            		
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                    String value = StaticMethod.nullObject2String(row.get(propertyEntry.getValue().toString())," ");
                    //获取工单状态和任务状态
                    String status = StaticMethod.nullObject2String(row.get("status")," ");
                    String taskStatus = StaticMethod.nullObject2String(row.get("taskstatus")," ");
                    //添加特殊处理
                    String name = propertyEntry.getValue().toString();
                    if(name.equals("chargeName")){
                    	value = chargeName;
                    }else if(name.equals("chargePhone")){
                    	value = chargePhone;
                    }else if(name.equals("province_name")){
                    	value = provinceName;
                    }else if(exist(dateList,name)){
                    	value = dateRep(value);
                    }
                    else if(name.equals("orderState")){
                    	value = decideStatus(value, status, taskStatus, processNames, checkingHumTask, holdHumTask);
                    	value = getDictName(filePath, name, value);	 	
                    }else if(name.equals("orderStateId")){
                    	value = decideStatus(value, status, taskStatus, processNames, checkingHumTask, holdHumTask);
                    }else if(exist(dictList,name)){
                    	value = getDictName(filePath, name, value);
                    }else if(exist(dictsList,name)){
                    	value = id2Name(value);
                    }
                    csvFileOutputStream.write(rep(value));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write("|");
                    }
                }
//            	写完一行数据进行换行
                csvFileOutputStream.newLine();
                
//            	csvFileOutputStream.write(chargeName);
//            	csvFileOutputStream.write(rep(chargePhone)+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("province_name")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("city_name")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("faultOrderId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("orderTheme")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerName")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerLev")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerLevId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerTypeId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("busType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("busTypeId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isLargeComplain")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("complainDetail")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("complainNbr")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("proId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("circuitName")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("assuranceLev")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("assuranceLevId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isAlarmMonitor")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("alarmId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isAlarmOrder")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("alarmOrderId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("complainStartTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("AlarmStartTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("orderStartTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("limitTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("orderEndTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("complainResult")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("complainResultId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("orderState")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("orderStateId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isContactCustomer")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("contactCustomerFirstTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("pretreatmentTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("preanalysisReason")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isSucPreanalysis")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("taskTransferTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("maintenanceCompany")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("maintenanceStartTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("maintenanceDetail")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("maintenanceResult")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isvisite")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("arriveSceneTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("removeAlarmTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("recoFaultTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("faulteChargeableCityName")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isoverTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("overTimeResion")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("problemResion")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("problemResionId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isConfirmResult")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("qcResult")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("qcDetail")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("faultType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("faultTypeId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("accessWay")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("enterpriseId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("serviceId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isVolumeFault")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("terminalType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("calledNumberCityName")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("ecsiType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("accgateIp")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("accgateId")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("mpiagSendMegTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isSusResponse")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("mpiagResReportTime")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("isSucResReport")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("apnName")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("tunnelType")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("vpnInstantiation")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("ceIp")," "))+"|");
//            	csvFileOutputStream.write(rep(StaticMethod.nullObject2String(row.get("customerIp")," ")));
//            	csvFileOutputStream.newLine();		
            }
            	
            //清空缓冲区数据，不清空再次使用会报空指针
      	  System.out.println("---------文件生成完毕，位置为"+csvFile.getAbsolutePath());
          csvFileOutputStream.flush();
        }catch(Exception e){
        	e.printStackTrace();  
        }finally {  
           try {  
                csvFileOutputStream.close();  
            } catch (Exception e) {  
               e.printStackTrace();
           }  
       }
		return csvFile;
	}
	/**
	 * 字符串“|”替换成“-”方法
	 * 字符串双引号“"” 转义
	 */
	private static String rep(String string){
		//获取字段中“"”是否存在
		int ind = string.indexOf("\"");
		int ind2 = string.indexOf("”");
		if(ind!=-1||ind2!=-1){
			string = "\""+string.replace("\"", "\"\"")+"\"";
			string = string.replace("”", "\"\"");
			string = string.replace("“", "\"\"");
		}
		string = string.replace("\n", "");
		string = string.replace("\r", "");
		string = string.replace("\t", "");
		string = string.replace(",", " ");
		return string.replace("|","-");
	}
	/**
	 * 日历格式"yyyy-MM-dd HH:mm:ss"字符串转换为"yyyyMMddHHmmss"格式
	 * @param string
	 * @return
	 */
	private static String dateRep(String string){
		string = string.trim();
		if(!string.equals("")){
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date date = format1.parse(string);
			string = format2.format(date); 
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
			return string;
	}
	/**
	 * 以100M为基准进行分割
	 * @param file	分割的文件
	 * @throws FileNotFoundException 
	 */
	public static void splitFile(File file,String url,int port,String username, String password, String path ) throws FileNotFoundException {
		InputStream fstr = new FileInputStream(file);
		try{		
		
		String fileName = file.getName();
		uploadFile(url, port, username, password, path, fileName, fstr);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				fstr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
//		FileInputStream fs = null;
//		FileOutputStream fo = null;
//		try {
//			BocoLog.info(new DataReportUtil(), "分割文件开始");
//			//获取文件名称
//			String fileName = file.getName();
//			//获取不带后缀的文件名
//			String fileOtherName = fileName.substring(0,fileName.lastIndexOf("."));
//            fs = new FileInputStream(file);
//            // 定义缓冲区
//            byte[] b = new byte[1024*1024];
//            
//            int len = 0;
//            Integer count = new Integer(0);
//            //获取文件的大小
//            double length = (double)file.length();
//            int number = (int)Math.ceil((length/(100*1024*1024)));
//            /**
//             * 切割文件时，记录 切割文件的名称和切割的子文件个数
//             * 这个信息为了简单描述，使用键值对的方式，用到了properties对象
//             */
////            Properties pro = new Properties();
//            // 定义输出的文件夹路径
//            File dir = new File("/csvFile");
//            // 判断文件夹是否存在，不存在则创建
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//            BocoLog.info(new DataReportUtil(), "分割文件位置为："+dir.getAbsolutePath());
//            //定义数字显示格式
//            Format f1 = new DecimalFormat("00");
//            // 切割文件
//            while ((len = fs.read(b)) != -1) {
//            	int count1 = count.intValue()+1;
//            	int count2 = (int)(Math.ceil((double)count1/100));
//            	if(count1==number){//如果是最后的一段文件，文件名后添加_END
//            		count = new Integer(count2);
//                    fo = new FileOutputStream(new File(dir, fileOtherName+""+(f1.format(count)) + "_END.csv"));               
//                    fo.write(b, 0, len);
//                    InputStream fst = new FileInputStream(new File(dir, fileOtherName+""+(f1.format(count)) + "_END.csv"));
//                	uploadFile(url, port, username, password, path, fileOtherName+""+(f1.format(count)) + "_END.csv", fst);
//            	}else{
//            		count = new Integer(count2);
//                    fo = new FileOutputStream(new File(dir, fileOtherName+""+(f1.format(count)) + ".csv"));               
//                    fo.write(b, 0, len);
//                    
//                    InputStream fstr = new FileInputStream(new File(dir, fileOtherName+""+(f1.format(count)) + ".csv"));
//                    uploadFile(url, port, username, password, path, fileOtherName+""+(f1.format(count)) + ".csv", fstr);
//            	}
//                
//            }
//            // 将被切割的文件信息保存到properties中
////            pro.setProperty("partCount", count + "");
////            pro.setProperty("fileName", file.getName());
////            fo = new FileOutputStream(new File(dir, fileOtherName+""+(f1.format(count)) + ".properties"));
////            // 写入properties文件
////            pro.store(fo, "save file info");
////            fo.close();
//              
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally{
//        	try {
//        		System.out.println("---------文件上传完毕");
//				fo.close();
//				fs.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	
//        }

	}
	


	/** 
	 * Description: 向FTP服务器上传文件 
	 * @Version1.0
	 * @param url FTP服务器hostname 
	 * @param port FTP服务器端口 
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param path FTP服务器保存目录 
	 * @param filename 上传到FTP服务器上的文件名 
	 * @param input 输入流 
	 * @return 成功返回true，否则返回false 
	 */  
	private static boolean uploadFile(String url,int port,String username, String password, String path, String filename, InputStream input) {  
	    boolean success = false;  
	   FTPClient ftp = new FTPClient();  
	    try {  
	        int reply;  
	        ftp.connect(url, port);//连接FTP服务器  
	        ftp.login(username, password);//登录
	        reply = ftp.getReplyCode();  
	       if (!FTPReply.isPositiveCompletion(reply)) {  
	           ftp.disconnect();  
	            return success;  
	        }  
	       //设置文件上传路径，如不设置，默认上传至根目录
	       ftp.changeWorkingDirectory(path);  
	       ftp.enterLocalPassiveMode();
	       boolean res = ftp.storeFile(filename, input);             
	       input.close();  
	       ftp.logout();  
	       success = true; 
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    } finally{
	        if (ftp.isConnected()) {  
	           try {  
	                ftp.disconnect();  
	            } catch (IOException ioe) {  
	            }  
	        }  
	    }
	    return success;  
	}  
	/**
	 * 通过对应字段获取配置文件中的内容进行展示
	 * @param filePath		配置文件地址，xml文件的相对路径，"/"代表web-inf/classess
	 * @param attr			对应的字段名称
	 * @param dictId		对应字段的id值
	 * @return
	 */
	public static String getDictName(String filePath,String attr,String dictId){
		//定义要获取到的字典值
		String dictName = " ";
		//获取根节点
		Element xmlp = XmlManage.getFile(filePath).getElement();
		//获取根节点后的所有子节点
		  List allChildren = xmlp.getChildren("dict");
		  for(int i=0;i<allChildren.size();i++) {
			  //遍历查找子节点对应的属性值
			  String idName = ((Element)allChildren.get(i)).getAttribute("id").getValue();
			  if(idName.equals(attr)){
				  List item = ((Element)allChildren.get(i)).getChildren();
				  for(int j=0;j<item.size();j++){
					  //边路查找字典，并获得对应字典值
					  String name = ((Element)item.get(j)).getAttribute("id").getValue();
					  if(name.equals(dictId)){
						  dictName = ((Element)item.get(j)).getAttribute("name").getValue();
						  if(dictName.equals("")){
							  dictName = " ";
						  }
						  break;//找到字典值后跳出循环
					  }
				  }
			  } 
			  } 
		return dictName;
	}
	/**
	 * 判断工单状态
	 * @param value				目前任务环节名称，也是最终返回值
	 * @param status			工单状态码
	 * @param taskStatus		任务状态码
	 * @param handle			T1.T2.T3处理环节数组字符串中间以","隔开
	 * @param CheckingHumTask	质检环节名称
	 * @param HoldHumTask		待归档环节名称
	 * @return
	 */
	private static String decideStatus(String value,String status,String taskStatus,String processNames,String checkingHumTask,String holdHumTask){
		//环节字段转list
		List processList =  StaticMethod.getArrayList(processNames, ",");
		if(status.equals("3")){
    		value = "1";
    	}else if(status.equals("1")){
    		value = "6";
    	}else if(status.equals("0")){//运行中         
    		
    			if(exist(processList,value)){
    				if(taskStatus.equals("2")){
    					value = "2";
    					return value;
    				}
    				value = "3";
    			}else if(value.trim().equals(checkingHumTask.trim())){
    				value = "4";
    			}else if(value.trim().equals(holdHumTask.trim())){
    				value = "5";
    			}else{
    				value = "7";
    			}
    	}else{
    		value = "7";
    	}
		return value;
	}
	/**
	 * 	判断list中数据去前后空格后是否有与name相同的数据
	 * @param list	传入的list集合
	 * @param name	一个字符串
	 * @return
	 */
	private static boolean exist(List list,String name){
		boolean bo = false;
		for(int i = 0;i<list.size();i++){
			if(list.get(i).toString().trim().equals(name)){
				bo = true;break;
			}
		}
		return bo;
	}
	
	@SuppressWarnings("finally")
	private static String id2Name(String id){
		String name = "";
		ITawSystemDictTypeDao dictDao = (ITawSystemDictTypeDao) 
				ApplicationContextHolder.getInstance().getBean("ItawSystemDictTypeDao");
		try {
			name = dictDao.id2Name(id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			return StaticMethod.nullObject2String(name,"");
		}
	}
}

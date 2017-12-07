package com.boco.data.reportmanagement.util;

import java.util.Map;

import com.boco.data.base.dao.ITawSystemDictTypeDao;
import com.boco.data.base.dao.jdbc.TawSystemDictTypeDao;
import com.boco.data.freamework.cache.MapCacheManager;
import com.boco.data.freamework.util.ApplicationContextHolder;
import com.boco.data.freamework.util.StaticMethod;
import com.boco.data.freamework.util.XmlManage;

/**
 * 对报表名称进行装饰
 * @author Administrator
 *
 */
public class FormatFileName {
	
	/**
	 * @param type 报表类型 :1040601表示月度报表
	 * @param dictId 报表名称字典值
	 * @return
	 * @throws Exception 
	 */
	public static String formatName(String type,String dictId) throws Exception{
		String result = "";
		if("1040601".equals(type) && !"".equals(dictId)){//月度报表
			String str = StaticMethod.nullObject2String(
					XmlManage.getFile("/config/reportmanagement.xml").
					getProperty("csv.month"));
			//从缓存中获取数据
			MapCacheManager mapCacheManager = 
					(MapCacheManager)ApplicationContextHolder.getInstance().
					getBean("mapCacheManager");
			Map<String,String> mapCache = mapCacheManager.getMapCache();
			System.out.println("mapCache=="+mapCache);
			String fileName = mapCache.get(dictId);
			System.out.println("从缓存中取到的fileName============="+fileName);
			//从数据库取
			if(fileName==null ||"".equals(fileName)){
				ITawSystemDictTypeDao ItawSystemDictTypeDao = 
						(ITawSystemDictTypeDao)ApplicationContextHolder.getInstance().
						getBean("ItawSystemDictTypeDao");
				fileName = ItawSystemDictTypeDao.id2Name(dictId);
			}
			result = str+",报表名称："+fileName+".[西藏移动电子运维系统]";
		}
		return result;
	}

}

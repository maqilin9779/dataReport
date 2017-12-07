package com.boco.data.freamework.cache;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;
import com.boco.data.freamework.util.ApplicationContextHolder;
/**
 * 缓存管理类
 * @author Administrator
 *
 */
public class MapCacheManager {
	
	//日志记录
	private static  Logger logger = Logger.getLogger(MapCacheManager.class);
	
	//缓存更新时间
	private volatile Long updateTime = 0L;
	
	//更新标识     true正在更新，false没有更新
	private volatile boolean updateFlag = false;
	
	//缓存实例static只有一个实例对象
	private static MapCacheManager mapCacheManager;
	
	//缓存容器
	private static Map<String,String> cacheMap = 
					new ConcurrentHashMap<String,String>();
	
	//构造器（构造方法）
	private MapCacheManager(){
		updateTime = System.currentTimeMillis();
		//this.LoadCache();//初始化缓存数据
	}
	
	//提供获取缓存管理实例的方法
	public static MapCacheManager getInstance(){
		if(null==mapCacheManager){
			synchronized (MapCacheManager.class) {
				mapCacheManager = new MapCacheManager();
			}
		}
		mapCacheManager.LoadCache();
		return mapCacheManager;
	}
	
	 //初始化缓存的数据
	 private void LoadCache(){
		 try {
	        this.updateFlag = true;// 正在更新  
	        IDownLoadSheetAccessiorsDAO jdbc = (IDownLoadSheetAccessiorsDAO) 
	   ApplicationContextHolder.getInstance().getBean("IDownLoadSheetAccessiorsDAO");
	        //查询报表名称
	        String sql = "select dictid,dictname from taw_system_dicttype "
	        		+ "where parentdictid='10402'";
	        
	        @SuppressWarnings("unchecked")
			List<Map<String,String>> dictList = (List<Map<String,String>>)
	        		jdbc.getSheetAccessoriesList(sql);
	       String key = "";
	       String value_ = "";
	       String value = "";
	       for(Map<String,String> dictMap:dictList){
	    	   //entry:{dictname=财务报表-多维度统计报表, dictid=1040202}
	    	   for(Entry<String,String> entry:dictMap.entrySet()){
	    		   key = entry.getKey();
	    		   if("dictid".equals(key)){//value_ = "1040202"
	    			   value_ = entry.getValue();
	    		   }
	    		   if("dictname".equals(key)){//value="财务报表-多维度统计报表"
	    			   value = entry.getValue();
	    		   }
	    	   }
	    	   if(!"".equals(value_)){
	    		   this.addData(value_, value);
	    		   value_ = "";
	    		   value = "";
	    	   }
	    	  
	       }
	    System.out.println("缓存加载完成，"+this.getCacheSize());
        this.updateFlag = false;// 更新已完成 
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	 
	 private boolean IsTimeOut(long currentTime) {
	       return ((currentTime - this.updateTime) > 5*24*60*60*1000);// 超过时限，5天超时
	 }
	 
	 /**
	  * 获取缓存
	  * @return
	  */
	@SuppressWarnings("static-access")
	public Map<String,String> getMapCache() {
        long currentTime = System.currentTimeMillis();  
        /*if (this.updateFlag) {// 前缓存正在更新  
            return null;  
        }*/
        if (this.IsTimeOut(currentTime)) {//缓存超出时限，需重新加载  
            synchronized (this) {
                this.ReLoadCache();
                this.updateTime = currentTime;
            }  
        }  
        return this.cacheMap;  
    }
	
	//向缓存中添加数据
	public void addData(String key,String value){
		 if(cacheMap!=null){
				cacheMap.put(key, value);
		 }
	}
	 
	 	/** 
	     * 获取缓存项大小 
	     * @return 
	     */  
	    private int getCacheSize() {  
	        return cacheMap.size();  
	    }
	    
	    /** 
	     * 获取更新时间 
	     * @return 
	     */  
	    private long getUpdateTime() {  
	        return this.updateTime;  
	    }
	    
	    /** 
	     * 获取更新标志 
	     * @return 
	     */  
	    private boolean getUpdateFlag() {
	        return this.updateFlag;  
	    }  
	  
	    /** 
	     * 重新装载 
	     */  
	    private void ReLoadCache() {  
	        this.cacheMap.clear();  
	        this.LoadCache();  
	    }
	
}

package com.boco.data.base.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import com.boco.data.freamework.util.StaticMethod;


public class ExtendBatchPreparedStatementSetter implements BatchPreparedStatementSetter{
	final List paramsList;  
	final List keyList;
    
    public ExtendBatchPreparedStatementSetter(List resultList,List columnList){  
    	paramsList = resultList; 
    	keyList=columnList;
    }  
    public int getBatchSize() {  
        return paramsList.size();  
    }  
  
    public void setValues(PreparedStatement ps, int i) throws SQLException {
    	Map resultMap=(Map)paramsList.get(i);    	
    	Iterator iterator=keyList.iterator();
    	int index=1;
    	while(iterator.hasNext()){
    	  String key=StaticMethod.nullObject2String(iterator.next());
    	  ps.setObject(index++, resultMap.get(key));
    	} 
    }
}

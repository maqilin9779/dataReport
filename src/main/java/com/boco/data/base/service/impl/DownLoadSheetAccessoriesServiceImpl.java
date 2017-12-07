package com.boco.data.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;
import com.boco.data.base.service.IDownLoadSheetAccessoriesService;

@Service("IDownLoadSheetAccessoriesService")
public class DownLoadSheetAccessoriesServiceImpl implements
		IDownLoadSheetAccessoriesService {
	
	@Autowired
	private IDownLoadSheetAccessiorsDAO dao;
	
	public IDownLoadSheetAccessiorsDAO getDao() {
		return dao;
	}

	public void setDao(IDownLoadSheetAccessiorsDAO dao) {
		this.dao = dao;
	}

	public List getSheetAccessoriesList(String sql) throws Exception {
		
		return this.dao.getSheetAccessoriesList(sql);
	}
	public void updateTasks(String sql) throws Exception {		
		this.dao.updateTasks(sql);
	}
	
	public boolean executeProcedure(String procedureSql) throws Exception {		
		return this.dao.executeProcedure(procedureSql);
	}
	
    public List executeSql(String sql, Object para[])throws Exception{
	    return dao.executeSql(sql, para);
    }
    
    public void batchExcuteSql(List resultList,Object obj,String batchType)throws Exception{
		
	}
    
    public void batchUpdateSql(List resultList,Object obj,String updateColumn) throws Exception{
		
	}
}

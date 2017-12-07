package com.boco.data.base.service;

import java.util.List;

import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;


public interface IDownLoadSheetAccessoriesService {
	
	public IDownLoadSheetAccessiorsDAO getDao();



	public void setDao(IDownLoadSheetAccessiorsDAO dao);
	/**
	 * 根据sql语句获取到所有的附件集合
	 * @param sql
	 * @return
	 * @throws Exception
	 */
    public List getSheetAccessoriesList(String sql)throws Exception;
	/**
	 * 根据sql语句update相关的task对象
	 * @param sql
	 * @return
	 * @throws Exception
	 */
    public void updateTasks(String sql)throws Exception;  
    
    /**
	 * 根据sql语句update相关的task对象
	 * @return
	 * @throws Exception
	 */
    public boolean executeProcedure(String procedureSql)throws Exception;

	public List executeSql(String countSql, Object[] objects) throws Exception;
	
	public void batchExcuteSql(List resultList,Object obj,String batchType)throws Exception;
	
	public void batchUpdateSql(List resultList,Object obj,String updateColumn) throws Exception;
}

package com.boco.data.base.dao;

/**
 * 对字典操作的dao
 * @author Administrator
 *
 */
public interface ITawSystemDictTypeDao {
	
	/**
	 * 根据字典id转name
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String id2Name(final String id) throws Exception;
	
}

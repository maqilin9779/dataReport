package com.boco.data.base.dao.jdbc;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.boco.data.base.dao.ExtendBatchPreparedStatementSetter;
import com.boco.data.base.dao.IDownLoadSheetAccessiorsDAO;


public class DownLoadSheetAccessiorsDAOImpl extends JdbcDaoSupport
implements IDownLoadSheetAccessiorsDAO
{
    public DownLoadSheetAccessiorsDAOImpl(){
    }

    public List getSheetAccessoriesList(String sql)
        throws Exception{
        List list = getJdbcTemplate().queryForList(sql);
        return list;
    }

    public void updateTasks(String sql)
        throws Exception
    {
        getJdbcTemplate().execute(sql);       
    }
	public boolean executeProcedure(String procedureSql) throws Exception {
		Connection conn = this.getConnection();
		CallableStatement   cstmt  = conn.prepareCall(procedureSql);
		boolean ifSuccess = cstmt.execute();
		return ifSuccess;
	}
	public List executeSql(String sql, Object[] para) throws Exception {
	    List list = getJdbcTemplate().queryForList(sql, para);
	    return list;
	  }
	public void batchExcuteSql(String batchSql,List resultList,List columnList)throws Exception{
		ExtendBatchPreparedStatementSetter preSetter=new ExtendBatchPreparedStatementSetter(resultList,columnList);
		int[] ii = this.getJdbcTemplate().batchUpdate(batchSql, preSetter);  
		System.out.println("batchExcuete"+ii.length);
	}
}

package com.boco.data.base.dao.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.boco.data.base.dao.ITawSystemDictTypeDao;

public class TawSystemDictTypeDao extends JdbcDaoSupport
	implements ITawSystemDictTypeDao{

	public String id2Name(String id) throws Exception {
		List<Map<String, Object>> list = this.getJdbcTemplate().
		queryForList("SELECT DICTNAME FROM TAW_SYSTEM_DICTTYPE WHERE DICTID='"+id+"'");
		if(list!=null && list.size()>0){
			return (String)(list.get(0).get("DICTNAME"));
		}
		return null;
	}

}

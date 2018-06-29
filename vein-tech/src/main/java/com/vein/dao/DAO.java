package com.vein.dao;

import java.util.List;

public interface DAO {
	
	/**
	 * 保存对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	
	public Object selectOne(String sql,Object obj) throws Exception;
	
	
	public Object select(String sql) throws Exception;
	
	/**
	 * 保存对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int insert(String str, Object obj) throws Exception;
	
	
	
	/**
	 * 修改对象
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int update(String str, Object obj) throws Exception;
	
	/**
	 * 删除对象 
	 * @param str
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int delete(String str, Object obj) throws Exception;

	
	
	
	/**
	 * 查找对象数组
	 * @param sql
	 * @return
	 */
	public List<?> findList(String sql);
	
	/**
	 * 条件查找对象数组
	 * @param sql
	 * @param obj
	 * @return
	 */
	public List<?> findList(String sql,Object obj);
	
}
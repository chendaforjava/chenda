package com.vein.daoImple;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.vein.dao.DAO;

@Repository
public class DAOImple implements DAO{
	
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public Object selectOne(String sql, Object obj) throws Exception {
		return sqlSessionTemplate.selectOne(sql, obj);
	}
	
	public Object select(String sql) throws Exception{
		return sqlSessionTemplate.selectOne(sql);
	}
	
	@Override
	public int insert(String str, Object obj) throws Exception {
		return sqlSessionTemplate.insert(str, obj);
		
	}

	@Override
	public int update(String str, Object obj) throws Exception {
		return sqlSessionTemplate.update(str, obj);
	}

	@Override
	public int delete(String str, Object obj) throws Exception {
		return sqlSessionTemplate.delete(str, obj);
	}

	@Override
	public List<?> findList(String sql) {
		return sqlSessionTemplate.selectList(sql);
	}

	@Override
	public List<?> findList(String sql, Object obj) {
		return sqlSessionTemplate.selectList(sql, obj);
	}

}

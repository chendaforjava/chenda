package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.vein.bean.Corporation;
import com.vein.bean.Manager;
import com.vein.dao.DAO;
import com.vein.util.CoporationAttr;

@Service
public class LoginService {
	
	@Autowired
	DAO dao;
	
	
	public List<Corporation> getCorporationInfo() throws Exception{
		List<Corporation> corporationList = (ArrayList<Corporation>)dao.findList("LoginMapper.getAllCorporation");
		return corporationList;
	}
	
	
	public Manager getManagerById(String userName){
		String[] strArray = userName.split("#");
		userName = strArray[0];
		String coporationId = strArray[1];
		Manager manager = null;
		try {
			Map<String,String> parameters = new HashMap<String,String>();
			parameters.put("userName", userName);
			parameters.put("coporationId", coporationId);
			manager = (Manager)dao.selectOne("LoginMapper.findManager", parameters);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return manager;
	}
	
	
	public Corporation getCorporationByName(String corName){
		try {
			Corporation cor = (Corporation)dao.selectOne("LoginMapper.getCorporationByName", corName);
			return cor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
}

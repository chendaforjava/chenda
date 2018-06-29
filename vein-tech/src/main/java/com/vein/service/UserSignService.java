package com.vein.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vein.bean.Device;
import com.vein.bean.Sign;
import com.vein.bean.User;
import com.vein.dao.DAO;

@Service
public class UserSignService {
	
	@Autowired
	DAO dao;
	
	
	public Device deviceExist(Map<String, Object> parameter){
		//判断当前设备是否存在
		try {
			Device device = (Device)dao.selectOne("UserSignMapper.findDeviceByInfo", parameter);
			if(device!=null){
				return device;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public User findSignUser(Map<String, Object> parameter){
		User user = null;
		try {
			user = (User)dao.selectOne("UserSignMapper.findSignUser", parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	public boolean saveSign(Sign sign){
		try {
			dao.insert("UserSignMapper.insertUserSign", sign);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean saveScheduleSign(Sign sign){
		try {
			dao.insert("UserSignMapper.insertUserSignSchedule", sign);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public List<User> findDeptUser(Map<String,String> params){
		List<User> userList = (ArrayList<User>)dao.findList("UserSignMapper.findDeptUser", params);
		return userList;
	}
	
	
	
	public List<User> allUserList(Map<String,Object> params){
		List<User> userList = (ArrayList<User>)dao.findList("UserSignMapper.getUserInfo", params);
		return userList;
	}
	
	public List<User> getAllSignUSerList(Map<String,Object> params){
		List<User> userList = (ArrayList<User>)dao.findList("UserSignMapper.getAllSignUSerList", params);
		return userList;
	}
	
	
	
}

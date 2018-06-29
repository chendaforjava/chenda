package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vein.bean.Schedule;
import com.vein.bean.ScheduleUser;
import com.vein.bean.User;
import com.vein.dao.DAO;

@Service
@SuppressWarnings("unchecked") 
public class MeetingService {
	
	
	@Autowired
	DAO dao;
	
	public List<User> getDeptUsers(Map<String,String> params){
		List<User> userList = (ArrayList<User>)dao.findList("MeetingMapper.selectDeptUsers", params);
		return userList;
	}
	
	
	public List<Schedule> loadSchedule(String corId){
		
		List<Schedule> scheduleList = (ArrayList<Schedule>)dao.findList("MeetingMapper.loadSchudels", corId);
		return scheduleList;
	}
	
	public List<User> getVeinFeatureUsers(Map<String,Object> params){
		List<User> userList = (ArrayList<User>)dao.findList("MeetingMapper.selectUserInfoById", params);
		return userList;
	}
	
	
	
	public void saveSchedule(Map<String,Object> params) throws Exception{
		dao.insert("MeetingMapper.saveSchedule", params);
	}
	
	
	public void saveScheduleUser(Map<String,Object> map) throws Exception{
		dao.insert("MeetingMapper.saveScheduleUser", map);
	}
	
	
	public List<User> getNoFeatureUsers(Map<String,Object> params){
		return (ArrayList<User>)dao.findList("MeetingMapper.getNoFeatureUsers", params);
	}
	
	
	public Map<String,String> delSchedule(Map<String,String> params){
		try {
			dao.delete("MeetingMapper.deleteSchedule", params);
			params.clear();
			params.put("accessType", "1");
		} catch (Exception e) {
			params.clear();
			params.put("accessType", "2");
			e.printStackTrace();
		}
		return params;
	}
	
	
	
	public Map<String,Object> getScheduleUser(Map<String,String> parameter){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			String scheduleId = (String) parameter.get("scheduleId");
			List<User> userList = (ArrayList<User>)dao.findList("MeetingMapper.selectScheduleUser", scheduleId);
			List<User> userListSign = (ArrayList<User>)dao.findList("MeetingMapper.selectScheduleUserSign",scheduleId);
			returnMap.put("accessType", "1");
			returnMap.put("userList", userList);
			returnMap.put("userListSign", userListSign);
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	public Map<String,Object> rsyncMeeting(Map<String,String> parameter){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			List<Schedule> scheduleList   = (ArrayList<Schedule>)dao.findList("MeetingMapper.rsyncMeeting", parameter);
			returnMap.put("scheduleList", scheduleList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	
}

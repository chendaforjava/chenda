package com.vein.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Schedule;
import com.vein.bean.ScheduleUser;
import com.vein.bean.User;
import com.vein.service.MeetingService;
import com.vein.util.IDGenerater;

@Controller
@RequestMapping("/meeting")
public class MeetingAction {
	
	
	@Autowired
	MeetingService meetingService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	@ResponseBody
	@RequestMapping("/getDeptUsers")
	public Map<String,Object> getDeptUsers(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		parameters.put("corId", getCorId() );
		try {
			List<User> userList = meetingService.getDeptUsers(parameters);
			returnMap.put("userList", userList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/loadSchedule")
	public Map<String,Object> loadSchedule(){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			List<Schedule> scheduleList = meetingService.loadSchedule(getCorId());
			returnMap.put("accessType","1");
			returnMap.put("scheduleList",scheduleList);
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/addSchedule")
	public Map<String,Object> addSchedule(@RequestBody Map<String, String> parameters){
		Map<String,Object> map = new HashMap<String,Object>();
		List<String> uIdList = new ArrayList<String>();
		try {
			String corId = getCorId();
			String jobNumberText = (String) parameters.get("jobNumberText");
			jobNumberText = jobNumberText.replace("[", "").replace("]", "").replace("\"", "");
			String[] jobNumberArray = jobNumberText.split("\\,");
			List<String> jobNumberListTmp = Arrays.asList(jobNumberArray);
			List<String> jobNumberList = new ArrayList<String>(jobNumberListTmp);
			map.put("corId", corId);
			map.put("list", jobNumberList);
			List<User> userList = meetingService.getVeinFeatureUsers(map);
			
			for(User u : userList){
				uIdList.add(u.getuId());
				//jobNumberList.remove(u.getUjobNum());
				int index = jobNumberList.indexOf(u.getUjobNum());
				jobNumberList.remove(index);
			}
			
			
			if(jobNumberList.size()>0){
				map.put("list", jobNumberList);
				userList = meetingService.getNoFeatureUsers(map);
				map.put("accessType", "7");
				map.put("userList", userList);
			}else{
				//所选的人都已经录取了指静脉
				//1.入库日程表记录
				String scheduleId = IDGenerater.getUUID();
				map.remove("jobNumberList");
				map.put("scheduleId", scheduleId);
				map.put("scheduleName", parameters.get("scheduleName"));
				map.put("participantNum", String.valueOf(jobNumberArray.length));
				map.put("address", parameters.get("address"));
				map.put("startDate", sdf.parse(parameters.get("startTime")));
				map.put("endDate", sdf.parse(parameters.get("endTime")));
				meetingService.saveSchedule(map);
				//添加日程人员表记录
				for(String s :uIdList){
					map.clear();
					map.put("uId", s);
					map.put("scheduleId", scheduleId);
					meetingService.saveScheduleUser(map);
				}
				map.clear();
				map.put("accessType", "1");
			}
		} catch (ParseException e) {
			map.put("accessType", "2");
			e.printStackTrace();
		} catch (Exception e) {
			map.put("accessType", "2");
			e.printStackTrace();
		}
		return map;
	}
	
	
	
	
	@ResponseBody
	@RequestMapping("/delSchedule")
	public Map<String,String> delSchedule(@RequestBody Map<String, String> parameters){
		return meetingService.delSchedule(parameters);
	}
	
	
	@ResponseBody
	@RequestMapping("/getScheduleUser")
	public Map<String,Object> getScheduleUser(@RequestBody Map<String, String> parameters){
		return meetingService.getScheduleUser(parameters);
	}
	
	
	@ResponseBody
	@RequestMapping("/syncMeeting")
	public Map<String,Object> synchronizeMeeting(@RequestBody Map<String, String> parameters){
		return meetingService.rsyncMeeting(parameters);
	}
	
	
	
	
	public String getCorId(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		return (String)session.getAttribute("corporation");
	}
	
	
}

package com.vein.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Device;
import com.vein.bean.Schedule;
import com.vein.bean.Sign;
import com.vein.bean.User;
import com.vein.listener.BeanListener;
import com.vein.service.SignWallService;
import com.vein.service.UserSignService;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.IDGenerater;
import com.vein.util.JsonUtil;
import com.vein.util.StringFormatUtil;

@Controller
@RequestMapping("/signWall")
public class SignWallAction {
	
	@Autowired
	SignWallService signWallService;
	
	@Autowired
	UserSignService userSignService;
	
	@ResponseBody
	@RequestMapping("/loadSchedule")
	public Map<String,Object> loadSchedule(){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		try {
			List<Schedule> scheduleList = signWallService.loadSchedule(corId);
			returnMap.put("scheduleList", scheduleList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/veinLoadSchedule")
	public Map<String,Object> veinLoadSchedule(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String corId = parameters.get("corId");
		try {
			List<Schedule> scheduleList = signWallService.loadSchedule(corId);
			returnMap.put("scheduleList", scheduleList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/getScheduleUsers")
	public Map<String,Object>  getScheduleUsers(@RequestBody Map<String, Object> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			returnMap = signWallService.getScheduleUsers(parameters);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		System.out.println(JsonUtil.obj2json(returnMap));
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/userSign")
	public Map<String,Object> userSign(@RequestBody Map<String, Object> parameter){
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Map<String,User> userMap = new HashMap<String,User>();
		List<User> userList = new ArrayList<User>();
		
		List<User> userListTemp = (ArrayList<User>)signWallService.getScheduleUser(parameter).get("userList");
		
		for(User u : userListTemp){
			userMap.put(u.getuId(), u);
		}
		try {
			String deviceId = (String)parameter.get("deviceId");
			deviceId = deviceId+(String)parameter.get("corId");
			parameter.put("deviceId", deviceId);
			
			Device device = userSignService.deviceExist(parameter);
			
			if(device!=null){
				returnMap = verityUser(parameter);
				String accessType = (String)returnMap.get("accessType");
				if(accessType!=null && accessType.equals("1")){
					//说明验证成功
					String jobNum = (String)returnMap.get("identityCode");
					parameter.put("jobNum", jobNum);
					User user = userSignService.findSignUser(parameter);
					if(user!=null){
						if(userMap.get(user.getuId())!=null){
							parameter.put("uId", user.getuId());
							updateSignTable(parameter);
							userList.add(user);
							returnMap.put("userList", userList);
						}else{
							returnMap.clear();
							returnMap.put("accessType", "6");
						}
					}else{
						returnMap.clear();
						returnMap.put("accessType", "6");
					}
				}
			}else{
				returnMap.put("accessType", "3");
			}
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	public Map<String,Object> verityUser(Map<String, Object> parameter){
		Map<String,Object> clientParameters = new HashMap<String,Object>();
		String feature = StringFormatUtil.formatString((String)parameter.get("feature"));
		
		clientParameters.put("business_id", parameter.get("corId"));
		clientParameters.put("device_id", parameter.get("deviceId"));
		clientParameters.put("feature", feature);
		clientParameters.put("featureType", Constent.featureType);
		clientParameters.put("featureVersion", Constent.featureVersion);
		clientParameters.put("tranction", Constent.tranction);
		
		String url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/recognize/recognizeperson";
		
		String cloudString = JsonUtil.obj2json(clientParameters);
		String s = CloudClass.HttpClientFromCloud(url, cloudString);
		
		clientParameters.clear();
		clientParameters = JsonUtil.json2Map(s);
		return clientParameters;
	}
	
	
	public boolean updateSignTable(Map<String, Object> parameter){
		try {
			Sign sign = new Sign();
			sign.addPropertyChangeListener(new BeanListener());
			sign.setSignId(IDGenerater.getUUID());
			sign.setSignTime(new Date());
			sign.setuId((String)parameter.get("uId"));
			sign.setSignTime(new Date());
			sign.setScheduleId((String)parameter.get("scheduleId"));
			boolean flag = userSignService.saveScheduleSign(sign);
			return flag;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
	}
}

package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vein.bean.Device;
import com.vein.bean.Schedule;
import com.vein.bean.User;
import com.vein.dao.DAO;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.JsonUtil;
import com.vein.util.StringFormatUtil;

@Service
@Transactional
public class SignWallService {
	
	
	@Autowired
	DAO dao;
	
	public List<Schedule> loadSchedule(String corId){
		return (ArrayList<Schedule>)dao.findList("SignWallMapper.loadSchedules", corId);
	}
	
	
	public Map<String,Object> getScheduleUsers(Map<String,Object> params){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<User> userList = (ArrayList<User>)dao.findList("SignWallMapper.getScheduleUsers", params);
		List<User> signedUser = (ArrayList<User>)dao.findList("SignWallMapper.signedUser", params);
		returnMap.put("userList", userList);
		returnMap.put("signedUser", signedUser);
		return returnMap;
	}
	
	public Map<String,Object> getScheduleUser(Map<String,Object> params){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<User> userList = (ArrayList<User>)dao.findList("SignWallMapper.getScheduleUsers", params);
		returnMap.put("userList", userList);
		return returnMap;
	}
	
/*	public void userSign(Map<String,Object> parameter){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String deviceId = (String)parameter.get("deviceId");
		deviceId = deviceId+(String)parameter.get("corId");
		String accessType = null;
		parameter.put("deviceId", deviceId);
		try {
			Device device = (Device)dao.selectOne("UserSignMapper.findDeviceByInfo", parameter);
			if(device!=null){
				returnMap = verityUser(parameter);
				accessType = (String)returnMap.get("accessType");
				if(accessType!=null && accessType.equals("1")){
					//说明验证成功
					String jobNum = (String)returnMap.get("identityCode");
					parameter.put("jobNum", jobNum);
					User user = userSignService.findSignUser(parameter);
					if(user!=null){
						parameter.put("uId", user.getuId());
						updateSignTable(parameter);
						userList.add(user);
					}
					returnMap.put("userList", userList);
				}
			}
			else{
				returnMap.put("accessType", "3");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
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
	
	
}

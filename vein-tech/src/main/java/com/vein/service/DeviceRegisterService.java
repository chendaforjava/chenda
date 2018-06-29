package com.vein.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vein.bean.Corporation;
import com.vein.bean.Device;
import com.vein.dao.DAO;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.JsonUtil;

@Service
@Transactional
public class DeviceRegisterService {
	
	@Autowired
	DAO dao;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public Map<String,Object> deviceRegister(Map<String,String> parameters) throws Exception{
		
		String corId = parameters.get("corId");
		String deviceId = parameters.get("deviceId");
		
		parameters.put("deviceSerial", deviceId);
		deviceId = deviceId+corId;
		parameters.put("deviceId", deviceId);
		
		//是否是新设备
		Device device = (Device)dao.selectOne("DeviceLoginMapper.selectDeviceById",parameters);
		Map<String,Object> returnMap = null;
		if(device==null){
			try {
				Corporation corporation = (Corporation)dao.selectOne("LoginMapper.findCorporationById", corId);
				parameters.put("corName", corporation.getCorName());
				//新添加一个设备
				returnMap = saveDevice2Cloud(parameters);
				if(returnMap!=null){
					dao.insert("DeviceLoginMapper.saveDevice", parameters);
					return returnMap;
				}else{
					return null;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			returnMap = new HashMap<String,Object>();
			returnMap.put("accessType", "1");
		}
		
		return returnMap;
	}
	
	public Map<String,Object> saveDevice2Cloud(Map<String,String> params){
		Map<String,String> cloudMap = new HashMap<String,String>();
		Date date = new Date();
		String device_start_time = sdf.format(date);
		cloudMap.put("device_id", params.get("deviceId"));
		cloudMap.put("device_model", params.get("deviceModel"));
		cloudMap.put("business_name", params.get("corName"));
		cloudMap.put("device_serial", params.get("deviceSerial"));
		cloudMap.put("device_name", " ");
		cloudMap.put("bio_type", params.get("bioType"));
		cloudMap.put("device_start_time", device_start_time);
		cloudMap.put("device_expired_time", "2020-01-01 00:00:00");
		String cloudString = JsonUtil.obj2json(cloudMap);
		String url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/device/addDevice";
		String s = CloudClass.HttpClientFromCloud(url, cloudString);
		if(s==null){
			return null;
		}else{
			return JsonUtil.json2Map(s);
		}
	}
	
	
	public Corporation findCorById(String corId){
		try {
			return (Corporation)dao.selectOne("DeviceLoginMapper.findCorById", corId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}

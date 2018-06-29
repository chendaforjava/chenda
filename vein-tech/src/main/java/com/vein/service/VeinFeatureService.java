package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.vein.bean.Dept;
import com.vein.bean.Device;
import com.vein.bean.User;
import com.vein.bean.VeinFinger;
import com.vein.dao.DAO;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.IDGenerater;
import com.vein.util.JsonUtil;
import com.vein.util.StringFormatUtil;

@Service
public class VeinFeatureService {
	
	@Autowired
	DAO dao;
	
	public User getUserById(Map<String,String> params){
		try {
			User user = (User)dao.selectOne("VeinFeatureMapper.selectUserById", params);
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public VeinFinger getVeinFingerByType(Map<String,String> params){
		try {
			return (VeinFinger)dao.selectOne("VeinFeatureMapper.selectVeinFingerByType", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public int saveFeature(Map<String,String> map){
		try {
			return dao.insert("VeinFeatureMapper.saveFeature", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public List<User> getallUsers(String corId){
		List<User> userList = (ArrayList<User>)dao.findList("VeinFeatureMapper.findAllUsersByCorId",corId);
		return userList;
	}
	
	
	public String getDeviceId(Map<String,String> params){
		Device device = null;
		String device_id = null;
		if(params.get("deviceId")==null){
			String corId = params.get("businessId");
			List<Device> deviceList = (ArrayList<Device>)dao.findList("DeviceLoginMapper.getAllDeviceByCorId", corId);
			if(deviceList.size()==0){
				return null;
			}else{
				device = deviceList.get(0);
			}
		}
		if(params.get("deviceId")!=null){
			device_id = params.get("deviceId");
		}else{
			device_id = device.getDeviceId();
		}
		
		return device_id;
		
	}
	
	
	public Map<String,Object> updateVeinFeature(Map<String,String> params){
		Map<String,String> cloudMap = new HashMap<String,String>();
		Device device = null;
		String device_id = null;
		if(params.get("deviceId")==null){
			String corId = params.get("businessId");
			List<Device> deviceList = (ArrayList<Device>)dao.findList("DeviceLoginMapper.getAllDeviceByCorId", corId);
			if(deviceList.size()==0){
				return null;
			}else{
				device = deviceList.get(0);
			}
		}
		
		if(params.get("deviceId")!=null){
			device_id = params.get("deviceId");
		}else{
			device_id = device.getDeviceId();
		}
		
		if(params.get("phoneNum")!=null){
			cloudMap.put("phone_num", params.get("phoneNum"));
		}
		String business_id = params.get("businessId");
		String feature = StringFormatUtil.formatString((String)params.get("feature"));
		String feature_type = Constent.featureType;
		String name = params.get("name");
		String identity_code = params.get("identityCode");
		String identity_type = Constent.identityType;
		String feature_version = Constent.featureVersion;
		String sex = params.get("sex");
		if(sex.equals("男")){
			sex="0";
		}else{
			sex="1";
		}
		
		cloudMap.put("device_id", device_id);
		cloudMap.put("business_id", business_id);
		cloudMap.put("feature", feature);
		cloudMap.put("name", name);
		cloudMap.put("feature_type", feature_type);
		cloudMap.put("identity_code", identity_code);
		cloudMap.put("identity_type", identity_type);
		cloudMap.put("feature_version", feature_version);
		cloudMap.put("sex", sex);
		
		String operationType = params.get("operationType");
		String url = null;
		if(operationType!=null){
			cloudMap.put("operationType", operationType);
			cloudMap.put("bioFeatureId", params.get("bioFeatureId"));
			url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/bioFeature/bioFeatureManager";
		}else{
			url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/person/addPerson";
		}
		String cloudString = JsonUtil.obj2json(cloudMap);
		
		String s = CloudClass.HttpClientFromCloud(url, cloudString);
		if(s==null){
			return null;
		}else{
			return JsonUtil.json2Map(s);
		}
	}
	
	
	public List<User> getUserByName(Map<String,String> parameter){
		List<User> userList = (ArrayList<User>)dao.findList("VeinFeatureMapper.findUserByName", parameter);
		return userList;
	}
	
	
	public List<Dept> getAllDept(String corId){
		return (ArrayList<Dept>)dao.findList("VeinFeatureMapper.selectAllDept", corId);
		
	}
	
	
	public void saveDetailUser(Map<String,String> params){
		String uId = IDGenerater.getUUID();
		params.put("uId", uId);
		params.remove("feature");
		params.remove("imageStr");
		try {
			dao.insert("VeinFeatureMapper.saveDetilUser", params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}

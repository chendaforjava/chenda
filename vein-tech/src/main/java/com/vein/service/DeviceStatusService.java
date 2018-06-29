package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vein.bean.Dept;
import com.vein.bean.Device;
import com.vein.dao.DAO;
import com.vein.dao.RedisCacheDao;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.JsonUtil;

@Service
public class DeviceStatusService {
	
	@Autowired
	RedisCacheDao redisCacheDao;
	
	@Autowired
	DAO dao;
	
	
	public Map<String,Object> saveDeviceStatus(Map<String, String> parameters){
		String deviceId = parameters.get("deviceId");
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			Device device = (Device)dao.selectOne("DeviceStatusMapper.selectDeviceById", deviceId);
			if (device != null) {
				redisCacheDao.saveObj2Set(deviceId);
				returnMap.put("accessType", "1");
			} else {
				returnMap.put("accessType", "3");
			}
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	public List<Device> getDeviceStatus(String corId){
		List<Device> deviceList = (ArrayList<Device>)dao.findList("DeviceStatusMapper.selectBusDevice", corId);
		if (deviceList.size() > 0) {
			for (Device device : deviceList) {
				String deviceId = device.getDeviceId();
				// 通过deviceId查找redis缓存
				String redisDeviceId = (String) redisCacheDao.getObjFromSet(deviceId);
				if (redisDeviceId != null && redisDeviceId.length() > 0) {
					// 说明该设备处于在线状态
					device.setStatus("0");
				} else {
					// 代表已离线
					device.setStatus("1");
				}
			}
		}
		return deviceList;
	}
	
	
	public void changeDeviceName(Map<String,String> parameters) throws Exception{
		dao.update("DeviceStatusMapper.updateDeviceName", parameters);
	}
	
	
	public Map<String,Object> delCloudDevice(Map<String,String> parameters){
		String cloudString = JsonUtil.obj2json(parameters);
		String url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/device/deletedevice";
		String s = CloudClass.HttpClientFromCloud(url, cloudString);
		if(s==null){
			return null;
		}else{
			return JsonUtil.json2Map(s);
		}
	}
	
	public void delDevice(Map<String,String> parameters) throws Exception{
		dao.delete("DeviceStatusMapper.delDevice", parameters);
	}
	
	
	public List<Dept> getAllDept(String corId){
		List<Dept> deptList = (ArrayList<Dept>)dao.findList("DeviceStatusMapper.findDeptByCorId", corId);
		return deptList;
	}
	
	
	
	
	
	
	
	
	
}

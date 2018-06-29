package com.vein.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Dept;
import com.vein.bean.Device;
import com.vein.service.DeviceStatusService;

@Controller
@RequestMapping("/device")
public class DeviceStatusAction {

	@Autowired
	DeviceStatusService deviceStatusService;

	@ResponseBody
	@RequestMapping("/updateDeviceOnline")
	public Map<String, Object> updateDeviceOnline(@RequestBody Map<String, String> parameters) {
		return deviceStatusService.saveDeviceStatus(parameters);
	}

	// 前端页面查看设备状况
	@ResponseBody
	@RequestMapping("/getDeviceStatus")
	public Map<String, Object> deviceStatus() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String) session.getAttribute("corporation");
		try {
			List<Device> deviceList = deviceStatusService.getDeviceStatus(corId);
			returnMap.put("deviceList", deviceList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/changeDeviceName")
	public Map<String,String> changeDeviceName(@RequestBody Map<String, String> parameters){
		Map<String,String> returnMap = new HashMap<String,String>();
		try {
			deviceStatusService.changeDeviceName(parameters);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping("/delDevice")
	public Map<String,Object> delDevice(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		try {
			returnMap = deviceStatusService.delCloudDevice(parameters);
			String msg = (String)returnMap.get("msg");
			if(returnMap!=null && msg!=null && msg.equals("删除设备成功")){
				deviceStatusService.delDevice(parameters);
				returnMap.put("accessType", "1");
			}
		} catch (Exception e) {
			returnMap.clear();
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/getAllDept")
	public Map<String,Object> getAllDept(){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String) session.getAttribute("corporation");
		try {
			List<Dept> deptList = deviceStatusService.getAllDept(corId);
			if(deptList!=null && deptList.size()>0){
				returnMap.put("deptList",deptList);
				returnMap.put("accessType", "1");
			}else{
				returnMap.put("accessType", "3");
			}
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
}

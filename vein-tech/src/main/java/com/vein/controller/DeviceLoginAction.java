package com.vein.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Corporation;
import com.vein.service.DeviceRegisterService;

@Controller
@RequestMapping("/vein")
public class DeviceLoginAction {

	@Autowired
	DeviceRegisterService deviceRegisterService;
	
	
	@ResponseBody
	@RequestMapping("/login")
	public Map<String,Object> login(@RequestBody Map<String, String> parameters){
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		Subject currentUser = SecurityUtils.getSubject();
		String userName = parameters.get("userName");
		String passWord = parameters.get("passWord");
		String corId = parameters.get("corId");
		userName = userName+"#"+corId;
		UsernamePasswordToken token = new UsernamePasswordToken(userName, passWord);
		try {
			currentUser.login(token);
			//登录成功注册设备
			Map<String,Object> tempMap = deviceRegisterService.deviceRegister(parameters);
			if(tempMap==null){
				returnMap.put("accessType", "2");
			}else{
				Corporation cor  = deviceRegisterService.findCorById(corId);
				tempMap.put("corName", cor.getCorName());
				return tempMap;
			}
		} catch (AuthenticationException e) {
			e.printStackTrace();
			returnMap.put("accessType", "4");
		} catch(Exception e){
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
}

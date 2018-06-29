package com.vein.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Device;
import com.vein.bean.Sign;
import com.vein.bean.User;
import com.vein.listener.BeanListener;
import com.vein.service.UserSignService;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.IDGenerater;
import com.vein.util.JsonUtil;
import com.vein.util.ParameterUtil;
import com.vein.util.StringFormatUtil;

@Controller
@RequestMapping("/sign")
public class UserSignAction {
	
	@Autowired
	UserSignService userSignService;
	
	@RequestMapping(value = { "/userSign" }, produces = { "application/json" })
	public void userSign(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> parameter = ParameterUtil.ParameterGet(request);
		String deviceId = (String)parameter.get("deviceId");
		deviceId = deviceId+(String)parameter.get("corId");
		parameter.put("deviceId", deviceId);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		List<User> userList = new ArrayList<User>();
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
					parameter.put("uId", user.getuId());
					updateSignTable(parameter);
					userList.add(user);
				}
				
				if(userList.size()==0){
					returnMap.clear();
					returnMap.put("accessType", "6");
				}else{
					returnMap.put("userList", userList);
				}
			}
		}else{
			returnMap.put("accessType", "3");
		}
		response.setCharacterEncoding("utf-8");
		Writer writer;
		try {
			writer = response.getWriter();
			writer.write(JsonUtil.obj2json(returnMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@ResponseBody
	@RequestMapping("/getUserList")
	public Map<String,Object> getUserList(@RequestBody Map<String, String> parameter){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		parameter.put("corId", corId);
		try {
			List<User> userList = userSignService.findDeptUser(parameter);
			returnMap.put("userList", userList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/getSignUserInfo")
	public Map<String, Object> getSignUserInfo(@RequestBody Map<String, Object> parameter) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		parameter.put("corId", corId);
		List<Date> dateList = getSignRange();
		Date startDate = dateList.get(0);
		Date endDate  = dateList.get(1);
		parameter.put("startDate", startDate);
		parameter.put("endDate", endDate);
		// 该部门下所有的人员信息
		List<User> allUserList = (ArrayList<User>) userSignService.allUserList(parameter);
		// 已签到的人员信息
		List<User> signUserList = (ArrayList<User>) userSignService.getAllSignUSerList(parameter);
		returnMap.put("allUserList", allUserList);
		returnMap.put("signUserList", signUserList);
		returnMap.put("allUserSize", allUserList.size());
		returnMap.put("signUserSize", signUserList.size());
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/getCorId")
	public Map<String,String> getCorId(){
		Map<String, String> returnMap = new HashMap<String, String>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		returnMap.put("corId", corId);
		return returnMap;
	}
	
	
	
	
	public List<Date> getSignRange(){
		List<Date> dateList = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		Date startDate = null;
		Date endDate = null;
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		if (minute < 30) {
			minute = 29;
			second = 59;
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			startDate = calendar.getTime();
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			endDate = calendar.getTime();
		} else {
			minute = 59;
			second = 59;
			calendar.set(Calendar.MINUTE, 30);
			calendar.set(Calendar.SECOND, 0);
			startDate = calendar.getTime();
			calendar.set(Calendar.MINUTE, minute);
			calendar.set(Calendar.SECOND, second);
			endDate = calendar.getTime();
		}
		dateList.add(startDate);
		dateList.add(endDate);
		return dateList;
	}
	
	
	
	
	public boolean updateSignTable(Map<String, Object> parameter){
		try {
			Sign sign = new Sign();
			sign.addPropertyChangeListener(new BeanListener());
			sign.setSignId(IDGenerater.getUUID());
			sign.setSignTime(new Date());
			sign.setuId((String)parameter.get("uId"));
			sign.setSignTime(new Date());
			boolean flag = userSignService.saveSign(sign);
			return flag;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
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
	
}

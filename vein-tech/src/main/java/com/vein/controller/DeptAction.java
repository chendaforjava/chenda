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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Corporation;
import com.vein.bean.Dept;
import com.vein.service.DeptService;

@Controller
@RequestMapping("/dept")
public class DeptAction {
	
	
	@Autowired
	DeptService deptService;
	
	@ResponseBody
	@RequestMapping("/getManagerInfo")
	public Map<String,Object> getManagerInfo(){
		Map<String,Object> parameter = new HashMap<String,Object>();
		try {
			String corporation = getCurrentSubjectInfo().get("corId");
			String userName = getCurrentSubjectInfo().get("userName");
			Corporation corparation = (Corporation)deptService.getManagerInfo(corporation, userName);
			parameter.put("corporation", corparation);
			parameter.put("accessType", "1");
		} catch (Exception e) {
			parameter.put("accessType", "2");
			e.printStackTrace();
		}
		return parameter;
	}
	
	
	@ResponseBody
	@RequestMapping("/getAllDept")
	public Map<String,Object> getAllDept(){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String) session.getAttribute("corporation");
		try {
			List<Dept>  deptList = deptService.getAllDept(corId);
			returnMap.put("deptList", deptList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/getDeptByPage")
	public Map<String,Object> getAllDept(@RequestBody Map<String, Object> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String) session.getAttribute("corporation");
		parameters.put("corId", corId);
		try {
			returnMap = deptService.getDeptByPage(parameters);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/saveDept")
	public Map<String,Object> saveDept(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String corId = getCurrentSubjectInfo().get("corId");
		int result = deptService.saveDept(parameters.get("deptName"), corId);
		parameters.put("corId", corId);
		Dept dept = deptService.inquireDept(parameters).get(0);
		if(result==1){
			returnMap.put("accessType", "1");
			returnMap.put("dept", dept);
		}else{
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/delDept")
	public Map<String,String> delDept(@RequestBody Map<String, String> parameters){
		try {
			String corId = getCurrentSubjectInfo().get("corId");
			parameters.put("corId", corId);
			Dept dept = deptService.inquireDept(parameters).get(0);
			parameters.put("deptId", dept.getDeptId());
			deptService.delDept(parameters);
			parameters.clear();
			parameters.put("accessType", "1");
		} catch (Exception e) {
			parameters.put("accessType", "2");
			e.printStackTrace();
		}
		return parameters;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/inquireDept")
	public Map<String,Object> inquireDept(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String corId = getCurrentSubjectInfo().get("corId");
		parameters.put("corId", corId);
		List<Dept> deptList = deptService.inquireDept(parameters);
		if(deptList!=null){
			returnMap.put("accessType","1");
			returnMap.put("deptList", deptList);
		}else{
			returnMap.put("accessType","2");
		}
		return returnMap;
	}
	
	
	
	@RequestMapping("/modifyDeptName")
	public String modifyDeptName(@RequestParam("deptNewName") String deptNewName,@RequestParam("deptId") String deptId){
		Map<String,String> param = new HashMap<String,String>();
		param.put("deptName", deptNewName);
		param.put("corId",getCurrentSubjectInfo().get("corId"));
		List<Dept> deptList = deptService.inquireDeptByName(param);
		if(deptList==null || deptList.size()==0){
			param.put("deptId", deptId);
			try {
				deptService.modifyDeptName(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/departmentManage.html";
	}
	
	
	
	public Map<String,String> getCurrentSubjectInfo(){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		String userName = (String)session.getAttribute("userName");
		String passWord = (String)session.getAttribute("passWord");
		Map<String,String> subjectInfo = new HashMap<String,String>();
		subjectInfo.put("corId", corId);
		subjectInfo.put("userName", userName);
		subjectInfo.put("passWord", passWord);
		return subjectInfo;
	}
	
}

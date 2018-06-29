package com.vein.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vein.bean.Corporation;
import com.vein.bean.Dept;
import com.vein.bean.User;
import com.vein.dao.DAO;
import com.vein.util.IDGenerater;

@Service
public class DeptService {
	
	@Autowired
	DAO dao;
	
	@Autowired
	UserManageService userManageService;
	
	public Corporation getManagerInfo(String corporationId,String userName) throws Exception{
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("corporationId", corporationId);
		parameter.put("userName", userName);
		Corporation corporation = (Corporation)dao.selectOne("DeptMapper.findManagerInfo", parameter);
		return corporation;
	}
	
	
	public List<Dept> getAllDept(String corId){
		return (ArrayList<Dept>)dao.findList("DeptMapper.getAllDept", corId);
	}
	
	
	public Map<String,Object> getDeptByPage(Map<String,Object> parameters){
		try {
			
			String deptName = (String)parameters.get("deptName");
			if(deptName!=null){
				deptName.replace("%", "\\%").replace("_", "\\_");
				parameters.put("deptName", deptName);
			}
			//总的部门数
			int count = (Integer)dao.selectOne("DeptMapper.getAllDeptNum",parameters);
			Object pageTemp = parameters.get("page");
			int page = 0;
			if(pageTemp instanceof String){
				 page = Integer.valueOf((String)parameters.get("page"));
			}else if(pageTemp instanceof Integer){
				page = (Integer)pageTemp;
			}
			int start = 1 + (6 * (page - 1)); 
			int end = page*6;
			
			
			
			
			parameters.put("start", start);
			parameters.put("end", end);
			
			//返回的总页数
			int p1 = count%6;
			int p2 = count/6;
			if(p1!=0){
				page = p2+1;
			}else{
				page = p2;
			}
			
			
			List<Dept> deptList  =  (ArrayList<Dept>)dao.findList("DeptMapper.getDeptByPage", parameters);
			parameters.clear();
			parameters.put("totalCount", page);
			parameters.put("deptList", deptList);
			
		} catch (Exception e) {
			parameters.clear();
			e.printStackTrace();
		}
		return parameters;
		
	}
	
	
	
	public int saveDept(String deptName,String corId){
		String deptId = IDGenerater.getUUID();
		Map<String,String> parameter = new HashMap<String,String>();
		parameter.put("deptName", deptName);
		parameter.put("corId", corId);
		parameter.put("deptId", deptId);
		try {
			dao.insert("DeptMapper.saveDept", parameter);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor={Exception.class, RuntimeException.class})
	public int delDept(Map<String,String> parameters){
		try {
			int i = dao.delete("DeptMapper.delDept",parameters);
			List<User> userList = inquireDeptUser(parameters);
			if(i==1){
				for(User u :userList){
					parameters.put("identity_code", u.getUjobNum());
					userManageService.delUserFromCloud(parameters);
				}
				return delDeptUsers(parameters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	public int delDeptUsers(Map<String,String> parameters){
		try {
			return dao.delete("DeptMapper.delDeptUsers",parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	
	
	public List<Dept> inquireDept(Map<String,String> parameter){
		try {
			String deptName = parameter.get("deptName");
			deptName = deptName.replace("%", "\\%").replace("_", "\\_");
			parameter.put("deptName", deptName);
			return (ArrayList<Dept>)dao.findList("DeptMapper.inquireDept", parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<Dept> inquireDeptByName(Map<String,String> parameter){
		try {
			return (ArrayList<Dept>)dao.findList("DeptMapper.inquireDeptByName", parameter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public List<User> inquireDeptUser(Map<String,String> parameter){
		List<User> userList = (ArrayList)dao.findList("DeptMapper.inquireDeptUser", parameter);
		return userList;
	}
	
	
	public void modifyDeptName(Map<String,String> parameter) throws Exception{
		dao.update("DeptMapper.modifyDeptName", parameter);
	}
	
}

package com.vein.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vein.bean.User;
import com.vein.dao.DAO;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.FtpUtil;
import com.vein.util.IDGenerater;
import com.vein.util.JsonUtil;

@Service
public class UserManageService {
	
	@Autowired
	DAO dao;
	
	
	public Map<String,Object> getUserPage(String pageNum,String corId,String deptId,String userName) throws Exception{
		int page = Integer.valueOf(pageNum);
		int start = 1 + (7 * (page - 1)); 
		int end = page*7;
		
		Map<String,Object> parameter = new HashMap<String,Object>();
		parameter.put("deptId", deptId);
		parameter.put("corId", corId);
		if(userName!=null){
			userName.replace("%", "\\%").replace("_", "\\_");
		}
		parameter.put("userName", userName);
		parameter.put("start", start);
		parameter.put("end", end);
		//查询该条件下共有多少人
		int count = (Integer)dao.selectOne("UserMapper.countUser",parameter);
		//返回的总页数
		
		int p1 = count%7;
		int p2 = count/7;
		if(p1!=0){
			page = p2+1;
		}else{
			page = p2;
		}
		//根据指定下标查找人数
		List<User> userList = (ArrayList<User>)dao.findList("UserMapper.findePageUser", parameter);
		parameter.clear();
		parameter.put("totalCount", page);
		parameter.put("sysuserList", userList);
		return parameter;
		
	}
	
	
	public int verityUserId(Map<String,String> parameters) throws Exception{
		int userNum = (Integer)dao.selectOne("UserMapper.verityUserId",parameters);
		return userNum;
	}
	
	
	
	public void saveUser(Map<String,Object> parameters) throws Exception{
		String uId = IDGenerater.getUUID();
		parameters.put("uId", uId);
		dao.insert("UserMapper.saveUser", parameters);
	}
	
	
	
	public void changePwd(Map<String,String> parameters)throws Exception{
		dao.update("UserMapper.changePwd", parameters);
	}
	
	
	
	public void modifyUser(HttpServletRequest request, HttpServletResponse response,String corId){
		// 1.创建文件上传工厂类
		DiskFileItemFactory fac = new DiskFileItemFactory();
		// 2.创建文件上传核心类对象
		ServletFileUpload upload = new ServletFileUpload(fac);
		Map<String, Object> parameter = new HashMap<String, Object>();
		// 【一、设置单个文件最大30M】
		upload.setFileSizeMax(30 * 1024 * 1024);// 30M
		// 【二、设置总文件大小：50M】
		upload.setSizeMax(50 * 1024 * 1024); // 50M
		
		if (upload.isMultipartContent(request)){
			try {
				List<FileItem> list = upload.parseRequest(request);
				// 遍历，得到每一个上传项
				for (FileItem item : list) {
					if (item.isFormField()) {
						// 普通表单x
						String fieldName = item.getFieldName();// 获取元素名称
						String value = item.getString("UTF-8"); // 获取元素值
						if (value == null) {
							value = "";
						}
						parameter.put(fieldName, value);
						System.out.println(fieldName + ":" + value);
					} else {
						// 文件上传表单
						// 判断是否传递的是图片文件
						String name = item.getName(); // 上传的文件名称
						String[] strs = name.split("\\.");
						String picType = strs[strs.length - 1];
						if ("jpg".equals(picType) || "gif".equals(picType) || "png".equals(picType)
								|| "jpeg".equals(picType)) {
							name = corId +"_"+parameter.get("userId") + "." + picType; // 保存到服务器的图片名称
							parameter.put("uPhotoUrl", name);
							
							InputStream in = item.getInputStream();
							
							item.delete();

							
							//上传到ftp服务器
							FtpUtil.uploadFile(Constent.FPTIP, Constent.FPTPORT,"vein", "veindone","/var/ftp/image", "vein", name, in);
						} 
					}

				}
				parameter.put("corId", corId);
				dao.update("UserMapper.modifyUser", parameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public Map<String,Object> delUserFromCloud(Map<String,String> params){
		Map<String,Object> cloudMap = new HashMap<String,Object>();
		cloudMap.put("business_id", params.get("corId"));
		cloudMap.put("identity_code", params.get("jobNum"));
		String url = "http://"+Constent.IP+":"+Constent.Port+"/cloud_platform/person/deletePerson";
		String cloudString = JsonUtil.obj2json(cloudMap);
		String s = CloudClass.HttpClientFromCloud(url, cloudString);
		cloudMap.clear();
		cloudMap = JsonUtil.json2Map(s);
		return cloudMap;
	}
	
	
	public void delUser(Map<String,String> param) throws Exception{
		dao.delete("UserMapper.deluser", param);
	}
	
	
	
	public List<User> blurryUsers(Map<String,String> params){
		String jobNumber = params.get("jobNumber");
		
		if(jobNumber!=null){
			jobNumber.replace("%", "\\%").replace("_", "\\_");
		}
		params.put("jobNumber", jobNumber);
		List<User> userList  = (ArrayList<User>)dao.findList("UserMapper.blurryUsers", params);
		
		return userList;
	}
}

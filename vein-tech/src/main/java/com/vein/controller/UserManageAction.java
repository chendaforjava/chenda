package com.vein.controller;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.vein.bean.User;
import com.vein.service.UserManageService;
import com.vein.util.Constent;
import com.vein.util.FtpUtil;

@Controller
@RequestMapping("/user")
public class UserManageAction {
	
	@Autowired
	UserManageService userManageService;
	
	
	@ResponseBody
	@RequestMapping("/sysuserlist")
	public Map<String,Object> getUserPage(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String pageNum = parameters.get("page");
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		String deptId = parameters.get("deptId");
		String userName = parameters.get("userName");
		if(deptId.equals("0")){
			deptId = "";
		}
		try {
			returnMap = userManageService.getUserPage(pageNum, corId, deptId, userName);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/verityUserId")
	public Map<String,String> verityUserId(@RequestBody Map<String, String> parameters){
		Map<String,String> returnMap = new HashMap<String,String>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		parameters.put("corId",corId);
		try {
			int userNum = userManageService.verityUserId(parameters);
			returnMap.put("userNum", String.valueOf(userNum));
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	
	// 添加人员
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public ModelAndView addUser(HttpServletRequest request, HttpServletResponse response) {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		Map<String, Object> parameter = new HashMap<String, Object>();
		// 1.创建文件上传工厂类
		DiskFileItemFactory fac = new DiskFileItemFactory();
		// 2.创建文件上传核心类对象
		ServletFileUpload upload = new ServletFileUpload(fac);
		// 【一、设置单个文件最大30M】
		upload.setFileSizeMax(30 * 1024 * 1024);// 30M
		// 【二、设置总文件大小：50M】
		upload.setSizeMax(50 * 1024 * 1024); // 50M
		// 判断，当前表单是否为文件上传表单
		if (upload.isMultipartContent(request)) {
			// 3.把请求数据转换为FileItem对象的集合
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
						// bumen,name,sex,userId,phone
						System.out.println(fieldName + ":" + value);
					} else {
						// 文件上传表单
						// 判断是否传递的是图片文件
						String name = item.getName(); // 上传的文件名称
						String[] strs = name.split("\\.");
						String picType = strs[strs.length - 1];
						if ("jpg".equals(picType) || "gif".equals(picType) || "png".equals(picType)
								|| "jpeg".equals(picType)) {
							name = corId +"_"+parameter.get("ujobNum") + "." + picType; // 保存到服务器的图片名称
							parameter.put("uPhotoUrl", name);
							
							InputStream in = item.getInputStream();
							item.delete();

							
							//上传到ftp服务器
							FtpUtil.uploadFile(Constent.FPTIP, Constent.FPTPORT,"vein", "veindone","/var/ftp/image", "vein", name, in);
						} else {
							// 重定位到用户添加页面

						}
					}

				}
				parameter.put("corId", corId);
				if(parameter.get("uPhotoUrl")==null){
					parameter.put("uPhotoUrl", "");
				}
				userManageService.saveUser(parameter);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return new ModelAndView("redirect:/userManage.html");
	}
	
	
	@RequestMapping(value = "/modifyuser", method = RequestMethod.POST)
	public ModelAndView modifyUser(HttpServletRequest request, HttpServletResponse response){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		try {
			userManageService.modifyUser(request,response,corId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/userManage.html");
	}
	
	
	@ResponseBody
	@RequestMapping("/changePwd")
	public Map<String,String> changePwd(@RequestBody Map<String, String> parameters){
		Map<String,String> returnMap = new HashMap<String,String>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		String userName = (String)session.getAttribute("userName");
		String oldPassWord = (String)session.getAttribute("passWord");
		
		if(!parameters.get("password").equals(oldPassWord)){
			returnMap.put("accessType", "4");
			return returnMap;
		}else{
			//更新密码
			parameters.put("corId", corId);
			parameters.put("userName", userName);
			try {
				userManageService.changePwd(parameters);
				returnMap.put("accessType", "1");
			} catch (Exception e) {
				returnMap.put("accessType", "2");
				e.printStackTrace();
			}
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/delUser")
	public Map<String,String> delUser(@RequestBody Map<String, String> parameters){
		Map<String,String> returnMap = new HashMap<String,String>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		parameters.put("corId", corId);
		try {
			Map<String,Object> cloudMap = userManageService.delUserFromCloud(parameters);
			if(cloudMap!=null || ((String)cloudMap.get("accessType")).equals("1")){
				userManageService.delUser(parameters);
				returnMap.put("accessType", "1");
			}else{
				returnMap.put("accessType", "8");
			}
		} catch (Exception e) {
			returnMap.put("accessType", "2");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	
	@ResponseBody
	@RequestMapping("/blurryUsers")
	public Map<String,Object> blurryUsers(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		try {
			List<User> userList = userManageService.blurryUsers(parameters);
			for(User u : userList){
				if(u.getVeinFingerList().size()==1){
					if(u.getVeinFingerList().get(0).getVeinId()==null){
						u.setuVeinNum("0");
					}else{
						u.setuVeinNum("1");
					}
				}else{
					u.setuVeinNum(String.valueOf(u.getVeinFingerList().size()));
				}
			}
			returnMap.put("accessType", "1");
			returnMap.put("userList", userList);
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
}

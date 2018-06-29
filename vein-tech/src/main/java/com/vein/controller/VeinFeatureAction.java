package com.vein.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vein.bean.Dept;
import com.vein.bean.User;
import com.vein.bean.VeinFinger;
import com.vein.service.VeinFeatureService;
import com.vein.util.CloudClass;
import com.vein.util.Constent;
import com.vein.util.FtpUtil;
import com.vein.util.JsonUtil;
import com.vein.util.StringFormatUtil;

@Controller
@RequestMapping("/feature")
public class VeinFeatureAction {
	
	
	private static Logger logger = LoggerFactory.getLogger(VeinFeatureAction.class);
	
	
	@Autowired
	VeinFeatureService veinFeatureService;
	
	
	@ResponseBody
	@RequestMapping("/inquireUserID")
	public Map<String,Object> inquireUserId(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<>();
		User user = veinFeatureService.getUserById(parameters);
		
		List<Dept> deptList = veinFeatureService.getAllDept(parameters.get("corId"));
		if(user!=null){
			returnMap.put("accessType", "9");
			returnMap.put("message", "工号重复");
		}else{
			returnMap.put("accessType", "1");
			returnMap.put("message", "该工号可以使用");
		}
		
		if(deptList!=null && deptList.size()>0){
			returnMap.put("deptList", deptList);
		}
		
		return returnMap;
	}
	
	@ResponseBody
	@RequestMapping("/idPickFeature")
	@Transactional
	public Map<String,Object> idPickFeature(@RequestBody Map<String, String> parameters){
		logger.info("省份证注册传递的请求参数为："+JsonUtil.obj2json(parameters));
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		User user = veinFeatureService.getUserById(parameters);
		if(user!=null){
			returnMap.put("accessType", "2");
			returnMap.put("message", "该人员已经存在！");
			return returnMap;
		}
		
		String imageStr = parameters.get("imageStr");
		String deviceId = parameters.get("deviceId");
		String jobNumber = parameters.get("jobNumber");
		String businessId = parameters.get("corId");
		deviceId = deviceId+businessId;
		parameters.put("deviceId", deviceId);
		String name = parameters.get("userName");
		parameters.put("jobNumber", jobNumber);
		parameters.put("businessId", businessId);
		parameters.put("identityCode", jobNumber);
		parameters.put("name", name);
		
		Map<String,Object> verityMap = verityUser(parameters);
		if("1".equals(verityMap.get("accessType")) && !jobNumber.equals(verityMap.get("identityCode"))){
			returnMap.put("accessType", "10");
			returnMap.put("message", "同一个人的指静脉不能注册多个人员！");
			return returnMap;
		}
		
		
		Map<String,Object> map  =  veinFeatureService.updateVeinFeature(parameters);
		if(map==null){
			returnMap.put("accessType", "2");
		}else{
			if(map.get("accessType").equals("1")){
				try {
					//入库操作
					//byte[] veinByte = string2Hex(imageStr);
					byte[] veinByte = Base64.decodeBase64(imageStr);
					 for(int i=0;i<veinByte.length;++i){  
			                if(veinByte[i]<0)  {//调整异常数据  
			                	veinByte[i]+=256;  
			                }  
			            }  
					InputStream in = new ByteArrayInputStream(veinByte);
					
					String imageName =  businessId +"_"+jobNumber + "." + parameters.get("imageFormat");
					
					parameters.put("uPhotoUrl", imageName);
					
					//将图片上传到ftp服务器
					FtpUtil.uploadFile(Constent.FPTIP, Constent.FPTPORT,"vein", "veindone","/var/ftp/image", "vein", imageName, in);
					
					veinFeatureService.saveDetailUser(parameters);
					
					
					parameters.put("bioFeatureId", (String)map.get("bioFeatureId"));
					
					parameters.put("identityCode",jobNumber);
					
					veinFeatureService.saveFeature(parameters);
					
					returnMap.put("accessType", "1");
				} catch (Exception e) {
					returnMap.put("accessType", "2");
					e.printStackTrace();
				}
			}else{
				return map;
			}
		}
		return returnMap;
	}
	
	
	
	@ResponseBody
	@RequestMapping("/pickFeature")
	public Map<String,Object> pickFeature(@RequestBody Map<String, String> parameters){
		
		logger.info("设备端用户采集指静脉传递的参数为："+JsonUtil.obj2json(parameters));
		
		Map<String,Object> returnMap = new HashMap<String,Object>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		User user = veinFeatureService.getUserById(parameters);
		String deviceId = parameters.get("deviceId");
		String corId = parameters.get("corId");
		deviceId = deviceId+corId;
		parameters.put("deviceId", deviceId);
		Map<String,Object> verityMap = verityUser(parameters);
		if(user!=null && "1".equals(verityMap.get("accessType")) && !user.getUjobNum().equals(verityMap.get("identityCode"))){
			returnMap.put("accessType", "10");
			returnMap.put("message", "同一个人的指静脉不能注册多个人员！");
			return returnMap;
		}
		if(user!=null){
			parameters.put("deviceId", deviceId);
			parameters.put("name", user.getuName());
			parameters.put("sex", user.getuSex());
			parameters.put("businessId", user.getCorId());
			parameters.put("identityCode", user.getUjobNum());
			parameters.put("phoneNum", user.getuPhone());
			parameters.put("uId", user.getuId());
			//查看当前用户是否已经有了该类型的模板
			VeinFinger veinFinger = veinFeatureService.getVeinFingerByType(parameters);
			if(veinFinger!=null){
				//说明之前已经录入过，更新这条指静脉数据即可
				parameters.put("operationType", "2");
				parameters.put("bioFeatureId", veinFinger.getVeinId());
				returnMap  = veinFeatureService.updateVeinFeature(parameters);
			}else{
				//新增一条指静脉数据
				map  = veinFeatureService.updateVeinFeature(parameters);
				//保存业务端的数据
				if(map==null){
					returnMap.put("accessType", "5");
				}else{
					if(map.get("accessType").equals("1")){
						returnMap = map;
						returnMap.put("accessType", "1");
						String bioFeatureId = (String)map.get("bioFeatureId");
						parameters.put("bioFeatureId",bioFeatureId);
						veinFeatureService.saveFeature(parameters);
					}else{
						returnMap.put("accessType", "2");
					}
				}
			}
			
		}else{
			returnMap.put("accessType", "3");
			returnMap.put("message", "不存在该人员！");
		}
		
		return returnMap;
	}
	
	
	public Map<String,Object> verityUser(Map<String, String> parameter){
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
	
	
	
	
	@ResponseBody
	@RequestMapping("/pageVeinVollent")
	public Map<String,Object> pageVeinVollent(@RequestBody Map<String, String> parameters){
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String businessId = (String)session.getAttribute("corporation");
		parameters.put("businessId", businessId);
		parameters.put("corId", businessId);
		String deviceId = veinFeatureService.getDeviceId(parameters);
		parameters.put("deviceId", deviceId);
		Map<String,Object> returnMap = new HashMap<String,Object>();
		User user = veinFeatureService.getUserById(parameters);
		String webVeinFeature = parameters.get("feature");
		byte[] veinByte = string2Hex(webVeinFeature);
		String feature = Base64.encodeBase64String(veinByte);
		parameters.put("feature", feature);
		Map<String,Object> verityMap = verityUser(parameters);
		
		if(user!=null && "1".equals(verityMap.get("accessType")) && !user.getUjobNum().equals((String)verityMap.get("identityCode"))){
			returnMap.put("accessType", "10");
			returnMap.put("message", "同一个人的指静脉不能注册多个人员！");
			return returnMap;
		}
		
		if(user!=null){
			parameters.put("sex", user.getuSex());
			parameters.put("name", user.getuName());
			parameters.put("identityCode", user.getUjobNum());
			parameters.put("phoneNum", user.getuPhone());
			parameters.put("uId", user.getuId());
			VeinFinger veinFinger = veinFeatureService.getVeinFingerByType(parameters);
			if(veinFinger!=null){
				//说明之前已经录入过，更新这条指静脉数据即可
				parameters.put("operationType", "2");
				parameters.put("bioFeatureId", veinFinger.getVeinId());
				returnMap = veinFeatureService.updateVeinFeature(parameters);
			}else{
				//新增一条指静脉数据
				Map<String,Object> map  = veinFeatureService.updateVeinFeature(parameters);
				//保存业务端的数据
				if(map==null){
					returnMap.put("accessType", "5");
				}else{
					if(map.get("accessType").equals("1")){
						returnMap = map;
						returnMap.put("accessType", "1");
						String bioFeatureId = (String)map.get("bioFeatureId");
						parameters.put("bioFeatureId",bioFeatureId);
						parameters.put("uId", user.getuId());
						parameters.remove("feature");
						veinFeatureService.saveFeature(parameters);
					}else{
						returnMap.put("accessType", "2");
					}
				}
			}
		}else{
			returnMap.put("accessType", "3");
		}
  		return returnMap;
	}
	
	
	
	@ResponseBody
	@RequestMapping("getUserList")
	public Map<String,Object> getUserList(){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		try {
			List<User> userList = veinFeatureService.getallUsers(corId);
			if(userList!=null && userList.size()>0){
				returnMap.put("userList", userList);
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
	
	@ResponseBody
	@RequestMapping("/findUserByName")
	public Map<String,Object> findUserByName(@RequestBody Map<String, String> parameters){
		Map<String,Object> returnMap = new HashMap<String,Object>();
		String name = parameters.get("name");
		name = name.replace("%", "\\%").replace("_", "\\_");
		parameters.put("name", name);
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		String corId = (String)session.getAttribute("corporation");
		parameters.put("corId", corId);
		try {
			List<User> userList = veinFeatureService.getUserByName(parameters);
			returnMap.put("userList", userList);
			returnMap.put("accessType", "1");
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("accessType", "2");
		}
		return returnMap;
	}
	
	private byte[] string2Hex(String hexString){
		 if (hexString == null || hexString.equals("")) {   
		        return null;   
		    }
		 hexString = hexString.toUpperCase();
		 int length = hexString.length() / 2;
		 char[] hexChars = hexString.toCharArray();
		 byte[] d = new byte[length];
		 for (int i = 0; i < length; i++) {   
		        int pos = i * 2;   
		        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
		 }
		 return d;
	}
	
	
	private byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}
}

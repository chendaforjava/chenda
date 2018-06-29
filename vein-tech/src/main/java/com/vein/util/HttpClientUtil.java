package com.vein.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class HttpClientUtil {
	
	private static Logger logger =LoggerFactory.getLogger(HttpClientUtil.class);  
	
	 public static String httpPost(Map<String,String> parameters,String url) throws Exception {
		 HttpPost httpPost = new HttpPost(url);
		 CloseableHttpClient client = HttpClients.createDefault();
		 String respContent = null;
		 
		 ObjectMapper mapper = new ObjectMapper();  
		 
		 String jsonfromMap =  mapper.writeValueAsString(parameters);
		
		 logger.info("请求验证云的请求参数为:"+jsonfromMap);
		 
		 StringEntity entity = new StringEntity(jsonfromMap.toString(),"utf-8");
		 entity.setContentEncoding("UTF-8");
		 entity.setContentType("application/json"); 
		 httpPost.setEntity(entity);
		 
		 HttpResponse resp = client.execute(httpPost);
		 
		 
		 if(resp.getStatusLine().getStatusCode() == 200) {
             HttpEntity he = resp.getEntity();
             respContent = EntityUtils.toString(he,"UTF-8");
         }
         return respContent;
	 }
	 
	 
	 public static void main(String[] args)throws Exception {
		 Map<String,String> parameters = new HashMap<String,String>();
		 parameters.put("name", "chenda");
		 String result = httpPost(parameters,"https://www.baidu.com");
		 System.out.println(result);
	}
	 
	 
}

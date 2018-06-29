package com.vein.util;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CloudClass {
	
	private static Logger logger = LoggerFactory.getLogger(CloudClass.class);
	public static String HttpClientFromCloud(String url,String params){
		CloseableHttpClient httpclient = HttpClients.createDefault();  
	    HttpPost httpPost = new HttpPost(url);// 创建httpPost     
	    httpPost.setHeader("Accept", "application/json");   
	    httpPost.setHeader("Content-Type", "application/json");  
	    String charSet = "UTF-8";
	    System.out.println(params);
	    StringEntity entity = new StringEntity(params, charSet);  
	    httpPost.setEntity(entity);          
	    CloseableHttpResponse response = null;
	    try {
			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK){
				 HttpEntity responseEntity = response.getEntity();
				 String jsonString = EntityUtils.toString(responseEntity);
				 return jsonString;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.info("远程调用云平台出错！");
		} catch (IOException e) {
			logger.info("IO异常");
			e.printStackTrace();
		}finally{
			if(httpclient!=null){
				try {
					httpclient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	    return null;
	}
	
}

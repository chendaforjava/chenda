package com.vein.util;

import java.io.BufferedReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
public class ParameterUtil {
	
	public static Map<String,Object> ParameterGet(HttpServletRequest request){
		
		String result = null;
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = request.getReader();
			while((result = reader.readLine()) != null){
				sb = sb.append(result); 
			}
			
			result = sb.toString();
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> map=mapper.readValue(result, Map.class);
			return map;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}

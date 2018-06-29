package com.vein.util;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
	private static ObjectMapper mapper = new ObjectMapper();
	
	
	public static String obj2json(Object obj){
		String str = "";
		try {
			str = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static Map<String,Object> json2Map(String s){
		try {
			Map<String,Object> m = mapper.readValue(s, Map.class);
			return m;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

package com.vein.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CoporationAttr {
	public static Map<String,String> coporationAttrMap = new HashMap<String,String>();
	
	
	public static Set<String> getKeySet(){
		return coporationAttrMap.keySet();
	}
	
	public static Collection<String> getValues(){
		return coporationAttrMap.values();
	}
	
	public static Set<Entry<String,String>> getEntrySet(){
		return coporationAttrMap.entrySet();
	}
	
	public static void setEntry(Map<String,String> map){
		coporationAttrMap.clear();
		for(Map.Entry<String, String> m :map.entrySet()){
			coporationAttrMap.put(m.getKey(), m.getValue());
		}
	}
	
}

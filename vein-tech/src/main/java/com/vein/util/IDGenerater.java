package com.vein.util;

import java.util.Random;
import java.util.UUID;

/*
 * 生成13位的id
 */
public class IDGenerater {
	
	public static String getUUID() {
		String s1 = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		return s1 = s1+RandonString();
	}
	
	public static String RandonString(){
		String s = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String s2 = "";
		int length = s.length();
		
		Random random = new Random();
		for(int i=0;i<5;i++){
			int temp = random.nextInt(length);
			s2 = s2+s.charAt(temp);
		}
		
		return s2;
	}
}



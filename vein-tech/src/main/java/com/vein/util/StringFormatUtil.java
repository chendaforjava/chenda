package com.vein.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatUtil {
	
	
	public static String formatString(String s){
		Pattern p = Pattern.compile("\n");
		Matcher m = p.matcher(s);
		s = m.replaceAll("");
		return s;
	}
	
}

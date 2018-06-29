package com.vein.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class SortUtil implements Comparator<String>{
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	
	@Override
	public int compare(String time1, String time2) {
		Date date1;
		Date date2;
		int flag=0;
		try {
			date1 = sdf.parse(time1);
			date2 = sdf.parse(time2);
			flag = date1.compareTo(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}

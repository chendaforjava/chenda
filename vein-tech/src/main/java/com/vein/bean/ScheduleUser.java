package com.vein.bean;

public class ScheduleUser {
	
	private String scheduleId;
	
	private String uId;

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		this.uId = uId;
	}

	public ScheduleUser(String scheduleId, String uId) {
		super();
		this.scheduleId = scheduleId;
		this.uId = uId;
	}

	public ScheduleUser() {
		super();
	}
}

package com.vein.bean;

import java.util.Date;
import java.util.List;

public class Schedule {
	
	private String scheduleId;
	
	private String corId;
	
	private String scheduleName;
	
	private String participantNum;
	
	private String address;
	
	private Date startTime;
	
	private Date endTime ;
	
	
	private List<ScheduleUser> scheduleUserList ;
	
	
	

	public List<ScheduleUser> getScheduleUserList() {
		return scheduleUserList;
	}

	public void setScheduleUserList(List<ScheduleUser> scheduleUserList) {
		this.scheduleUserList = scheduleUserList;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getCorId() {
		return corId;
	}

	public void setCorId(String corId) {
		this.corId = corId;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getParticipantNum() {
		return participantNum;
	}

	public void setParticipantNum(String participantNum) {
		this.participantNum = participantNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Schedule() {
		super();
	}

	public Schedule(String scheduleId, String corId, String scheduleName, String participantNum, String address,
			Date startTime, Date endTime,List<ScheduleUser> scheduleUserList) {
		super();
		this.scheduleId = scheduleId;
		this.corId = corId;
		this.scheduleName = scheduleName;
		this.participantNum = participantNum;
		this.address = address;
		this.startTime = startTime;
		this.endTime = endTime;
		this.scheduleUserList = scheduleUserList;
	}
}

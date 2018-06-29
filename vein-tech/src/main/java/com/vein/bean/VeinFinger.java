package com.vein.bean;

import java.util.Date;

public class VeinFinger {
	private String veinId;
	private String uId;
	private String veinPosition;
	private String veinCount;
	private Date veinCollectTime;
	public String getVeinId() {
		return veinId;
	}
	public void setVeinId(String veinId) {
		this.veinId = veinId;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getVeinPosition() {
		return veinPosition;
	}
	public void setVeinPosition(String veinPosition) {
		this.veinPosition = veinPosition;
	}
	public String getVeinCount() {
		return veinCount;
	}
	public void setVeinCount(String veinCount) {
		this.veinCount = veinCount;
	}
	public Date getVeinCollectTime() {
		return veinCollectTime;
	}
	public void setVeinCollectTime(Date veinCollectTime) {
		this.veinCollectTime = veinCollectTime;
	}
	public VeinFinger(String veinId, String uId, String veinPosition, String veinCount, Date veinCollectTime) {
		super();
		this.veinId = veinId;
		this.uId = uId;
		this.veinPosition = veinPosition;
		this.veinCount = veinCount;
		this.veinCollectTime = veinCollectTime;
	}
	public VeinFinger() {
		super();
		// TODO Auto-generated constructor stub
	}
}

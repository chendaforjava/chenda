package com.vein.bean;

import java.io.Serializable;

public class Manager implements Serializable{
	private String magerId;
	private String corId;
	private String mid;
	private String password;
	public String getMagerId() {
		return magerId;
	}
	public void setMagerId(String magerId) {
		this.magerId = magerId;
	}
	public String getCorId() {
		return corId;
	}
	public void setCorId(String corId) {
		this.corId = corId;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Manager() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Manager(String magerId, String corId, String mid, String password) {
		super();
		this.magerId = magerId;
		this.corId = corId;
		this.mid = mid;
		this.password = password;
	}
}

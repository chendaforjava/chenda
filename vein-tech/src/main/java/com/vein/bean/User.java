package com.vein.bean;

import java.util.List;

public class User {
	private String uId;
	private String uName;
	private String uSex;
	private String uPhone;
	private String corId;
	private String uAddr;
	private String uProvince;
	private String uCity;
	private String deptId;
	private String uVeinNum;
	private String uPhotoUrl;
	private String ujobNum;
	
	private List<VeinFinger> veinFingerList ;
	
	private List<Sign> signList;
	
	
	
	public List<Sign> getSignList() {
		return signList;
	}
	public void setSignList(List<Sign> signList) {
		this.signList = signList;
	}
	public List<VeinFinger> getVeinFingerList() {
		return veinFingerList;
	}
	public void setVeinFingerList(List<VeinFinger> veinFingerList) {
		this.veinFingerList = veinFingerList;
	}
	public String getUjobNum() {
		return ujobNum;
	}
	public void setUjobNum(String ujobNum) {
		this.ujobNum = ujobNum;
	}
	public String getuVeinNum() {
		return uVeinNum;
	}
	public void setuVeinNum(String uVeinNum) {
		this.uVeinNum = uVeinNum;
	}
	public String getuPhotoUrl() {
		return uPhotoUrl;
	}
	public void setuPhotoUrl(String uPhotoUrl) {
		this.uPhotoUrl = uPhotoUrl;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getuSex() {
		return uSex;
	}
	public void setuSex(String uSex) {
		this.uSex = uSex;
	}
	public String getuPhone() {
		return uPhone;
	}
	public void setuPhone(String uPhone) {
		this.uPhone = uPhone;
	}
	public String getCorId() {
		return corId;
	}
	public void setCorId(String corId) {
		this.corId = corId;
	}
	public String getuAddr() {
		return uAddr;
	}
	public void setuAddr(String uAddr) {
		this.uAddr = uAddr;
	}
	public String getuProvince() {
		return uProvince;
	}
	public void setuProvince(String uProvince) {
		this.uProvince = uProvince;
	}
	public String getuCity() {
		return uCity;
	}
	public void setuCity(String uCity) {
		this.uCity = uCity;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public User(String uId, String uName, String uSex, String uPhone, String corId, String uAddr, String uProvince,
			String uCity, String deptId,String uVeinNum,String uPhotoUrl,String ujobNum,List<VeinFinger> veinFingerList,
			List<Sign> signList) {
		super();
		this.uId = uId;
		this.uName = uName;
		this.uSex = uSex;
		this.uPhone = uPhone;
		this.corId = corId;
		this.uAddr = uAddr;
		this.uProvince = uProvince;
		this.uCity = uCity;
		this.deptId = deptId;
		this.uVeinNum = uVeinNum;
		this.uPhotoUrl = uPhotoUrl;
		this.ujobNum = ujobNum;
		this.veinFingerList = veinFingerList;
		this.signList = signList;
	}
	public User() {
		super();
	}
	
}

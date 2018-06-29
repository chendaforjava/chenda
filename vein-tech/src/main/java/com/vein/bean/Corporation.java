package com.vein.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Corporation implements Serializable{
	private String corId;
	private String corName;
	private String corAddr;
	private String corTel;
	private String corMail;
	
	private List<Manager> managerList = new ArrayList<Manager>();
	
	private List<Dept> deptList = new ArrayList<Dept>();
	
	public List<Dept> getDeptList() {
		return deptList;
	}
	public void setDeptList(List<Dept> deptList) {
		this.deptList = deptList;
	}
	public List<Manager> getManagerList() {
		return managerList;
	}
	public void setManagerList(List<Manager> managerList) {
		this.managerList = managerList;
	}
	public String getCorId() {
		return corId;
	}
	public void setCorId(String corId) {
		this.corId = corId;
	}
	public String getCorName() {
		return corName;
	}
	public void setCorName(String corName) {
		this.corName = corName;
	}
	public String getCorAddr() {
		return corAddr;
	}
	public void setCorAddr(String corAddr) {
		this.corAddr = corAddr;
	}
	public String getCorTel() {
		return corTel;
	}
	public void setCorTel(String corTel) {
		this.corTel = corTel;
	}
	public String getCorMail() {
		return corMail;
	}
	public void setCorMail(String corMail) {
		this.corMail = corMail;
	}
	public Corporation() {
		super();
	}
	public Corporation(String corId, String corName, String corAddr, String corTel, String corMail,
			List<Manager> managerList,List<Dept> deptList) {
		super();
		this.corId = corId;
		this.corName = corName;
		this.corAddr = corAddr;
		this.corTel = corTel;
		this.corMail = corMail;
		this.managerList = managerList;
		this.deptList = deptList;
	}

}

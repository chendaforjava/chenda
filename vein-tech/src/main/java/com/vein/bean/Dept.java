package com.vein.bean;

public class Dept {
	private String deptId;
	private String deptName;
	private String corId;
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCorId() {
		return corId;
	}
	public void setCorId(String corId) {
		this.corId = corId;
	}
	public Dept() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Dept(String deptId, String deptName, String corId) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
		this.corId = corId;
	}
	
}

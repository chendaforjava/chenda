package com.vein.bean;

public class Device {
	
	private String deviceId;
	private String deviceName;
	private String corId;
	private String deviceModel;
	private String bioType;
	private String status;
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getBioType() {
		return bioType;
	}
	public void setBioType(String bioType) {
		this.bioType = bioType;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getCorId() {
		return corId;
	}
	public void setCorId(String corId) {
		this.corId = corId;
	}
	public Device() {
		super();
	}
	public Device(String deviceId, String deviceName, String corId, String deviceModel, String bioType,String status) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.corId = corId;
		this.deviceModel = deviceModel;
		this.bioType = bioType;
		this.status = status;
	}
}

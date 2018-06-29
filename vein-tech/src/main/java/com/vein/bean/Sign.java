package com.vein.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;

public class Sign {
	private String signId;
	
	private String uId;
	
	private String scheduleId;
	
	private Date signTime;
	
	private Date signOut;
	
	
	
	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}

	public String getuId() {
		return uId;
	}

	public void setuId(String uId) {
		String olduId = this.uId;
		this.uId = uId;
		changes.firePropertyChange("userId", olduId,this.uId);
	}

	public Date getSignTime() {
		return signTime;
	}

	public void setSignTime(Date signTime) {
		this.signTime = signTime;
	}

	public Date getSignOut() {
		return signOut;
	}

	public void setSignOut(Date signOut) {
		this.signOut = signOut;
	}

	public Sign(String signId, String uId, Date signTime, Date signOut,String scheduleId) {
		super();
		this.signId = signId;
		this.uId = uId;
		this.signTime = signTime;
		this.signOut = signOut;
		this.scheduleId = scheduleId;
	}

	public Sign() {
		super();
	}
	
	// 用来管理监听器的类
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);

	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		changes.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		changes.removePropertyChangeListener(listener);
	}
	
	
}

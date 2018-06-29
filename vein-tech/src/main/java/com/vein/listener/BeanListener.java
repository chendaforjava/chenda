package com.vein.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.vein.bean.User;
import com.vein.dao.DAO;
import com.vein.util.JDBCUtil;
import com.vein.util.WSClientManager;


public class BeanListener implements PropertyChangeListener{
	
	@Autowired
	DAO dao;
	
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String uId = (String)evt.getNewValue();
		User user = null;
		try {
			user = JDBCUtil.query(uId);
			List<WebSocketSession> sessionList = WSClientManager.getSession().get(user.getCorId());
			if(sessionList!=null && sessionList.size()>0){
				WebSocketMessage<String> message = new  TextMessage(user.getUjobNum());
				for(WebSocketSession s : sessionList){
					s.sendMessage(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}

package com.vein.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


@Component
public class WSClientManager {
	
	public static Map<String,List<WebSocketSession>> session = new HashMap<String,List<WebSocketSession>>(); 
	
	
	public static Map<String, List<WebSocketSession>> getSession() {
		return session;
	}

	public static void setSession(Map<String, List<WebSocketSession>> session) {
		WSClientManager.session = session;
	}

}

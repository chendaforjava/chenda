package com.vein.handler;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.vein.util.WSClientManager;


public class WSHandler implements WebSocketHandler {
	private static Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	@Autowired
	private WSClientManager wsClientManager;
	
	
	public WSHandler() {
		System.out.println("对象创建完成");
	}
	
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.debug("connect to the websocket success......");
		String uri = session.getUri().toString();
		String corId = uri.split("=")[1];
		List<WebSocketSession> sessionList =  wsClientManager.getSession().get(corId);
		if(sessionList==null){
			sessionList = new ArrayList<WebSocketSession>();
			sessionList.add(session);
			wsClientManager.session.put(corId, sessionList);
		}else{
			sessionList.add(session);
		}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		TextMessage returnMessage = new TextMessage(message.getPayload().toString()+"<br/>");
		logger.debug(session.getHandshakeHeaders().getFirst("Cookie"));
		//borderCast(session,returnMessage);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		String uri = session.getUri().toString();
		String corId = uri.split("=")[1];
		if (session.isOpen()) {
			session.close();
			List<WebSocketSession> sessionList = wsClientManager.session.get(corId);
			sessionList.remove(session);
		}
		logger.debug("websocket connection closed......");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		logger.debug("websocket connection closed......");
		String uri = session.getUri().toString();
		String corId = uri.split("=")[1];
		//borderCast(session,new TextMessage(session.getId()+"已经下线"));
		session.close();
		List<WebSocketSession> sessionList = wsClientManager.session.get(corId);
		sessionList.remove(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}
	
	
	public void borderCast(WebSocketSession session,TextMessage message){
		String uri = session.getUri().toString();
		String corId = uri.split("=")[1];
		List<WebSocketSession> sessionList = wsClientManager.session.get(corId);
		for(WebSocketSession s : sessionList ){
			try {
				s.sendMessage(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

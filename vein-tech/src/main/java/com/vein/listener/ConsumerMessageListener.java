package com.vein.listener;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Component;

@Component
public class ConsumerMessageListener implements MessageListener{

	@Override
	public void onMessage(Message msg) {
		
	}

}

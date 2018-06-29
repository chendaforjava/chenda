package com.vein.socketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ThreadSocket implements Runnable{
	private ServerSocket serverSocket;
	private Socket socket;
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(8888);
			while(true){
				socket = serverSocket.accept();
				new VeinSocketService(socket);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

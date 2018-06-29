package com.vein.socketService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.vein.util.SocketUtil;

public class VeinSocketService implements Runnable {
	private Socket socket;
	private InputStream is;
	private InputStreamReader isr;
	private BufferedReader br;
	public VeinSocketService(Socket socket) {
		SocketUtil.set.add(socket);
		this.socket = socket;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			is = socket.getInputStream();
			isr =new InputStreamReader(is);
			br =new BufferedReader(isr);
			String info =null;
			while((info=br.readLine())!=null){
				System.out.println("message from client："+info);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				is.close();
				isr.close();
				br.close();
				SocketUtil.set.remove(socket);
				System.out.println("对象已移除！！！！");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
}

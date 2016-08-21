package com.tankwar.net;

import java.io.DataOutputStream;
import java.net.Socket;

/**
 * �ͻ��˵��������������
 * @author yubo
 *
 */
public class TankClientUtils {
	
	private int udpPort = 9999;
	
	public TankClientUtils() {
	}
	
	public void connectServer(String ip, int port) {
		try {
			Socket s = new Socket(ip, port);
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeInt(udpPort);
			System.out.println("Client connected to server!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}

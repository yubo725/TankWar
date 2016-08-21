package com.tankwar.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TankServer {
	
	public static final int TCP_PORT = 8899;
	private List<ClientInfo> list = new ArrayList<ClientInfo>();
	
	public void start() {
		try {
			ServerSocket ss = new ServerSocket(TCP_PORT);
			System.out.println("Tank server started! listening...");
			while(true) {
				Socket s = ss.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream());
				int udpPort = dis.readInt();
				list.add(new ClientInfo(s.getInetAddress().getHostAddress(), udpPort));
				System.out.println("a client connected to server, ip: " + s.getInetAddress() + ":" + s.getPort());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ClientInfo {
		String ip;
		int udpPort;
		
		public ClientInfo(String ip, int udpPort) {
			this.ip = ip;
			this.udpPort = udpPort;
			System.out.println("add a ClientInfo: " + toString());
		}

		@Override
		public String toString() {
			return "ClientInfo [ip=" + ip + ", udpPort=" + udpPort + "]";
		}
		
	}
	
	public static void main(String[] args) {
		new TankServer().start();
	}

}

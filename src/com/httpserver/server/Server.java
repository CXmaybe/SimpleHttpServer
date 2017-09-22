package com.httpserver.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	private ServerSocket server;
	public static final String CRLF = "\r\n";
	public static final String BLACK = " ";
	private boolean isShutDown = false;

	// 启动方法
	public void start() {
		this.start(8888);
	}

	// 指定端口的启动方法
	public void start(int port) {
		try {
			server = new ServerSocket(port);
			this.receive();
		} catch (IOException e) {
			// e.printStackTrace();
			this.stop();
		}
	}

	// 接收客户端
	private void receive() {
		try {
			while (!this.isShutDown) {
				new Thread(new Dispatcher(server.accept())).start();
			}
		} catch (IOException e) {
			// e.printStackTrace();
			stop();
		}
	}

	// 停止服务器
	public void stop() {
		this.isShutDown = true;
		try {
			this.server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}

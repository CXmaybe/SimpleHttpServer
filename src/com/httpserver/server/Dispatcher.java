package com.httpserver.server;

import java.io.IOException;
import java.net.Socket;

import com.httpserver.servlet.Servlet;

/*
 * Dispatcher分发器---每当收到请求就创建一个线程去处理请求
 * 
 */
public class Dispatcher implements Runnable {
	//保存socjet
	private Socket socket;
	//请求对象
	private Request req;
	//响应对象
	private Response resp;
	//响应码
	private int code;

	/*
	 * 构造器---获取socket
	 * 初始化请求和响应
	 * 将code赋值为200
	 * 
	 */
	public Dispatcher(Socket socket) {
		this.socket = socket;
		try {
			req = new Request(socket.getInputStream());
			resp = new Response(socket.getOutputStream());
			code = 200;
		} catch (IOException e) {
			// e.printStackTrace();
			//出现对错误将code设置500---服务器错误
			code = 500;
		}
	}

	public void run() {
		try {
			//获得请求的url,通过解析web.xml文件对应的,获得对应的servlet去处理.
			Servlet servlet = WebApp.getServlet(req.getUrl());
			if (null == servlet) {
				//servlet为null  将code设置为404
				this.code = 404;
			} else {
				//调用servlet的service方法
				servlet.service(req, resp);
			}
			//将对应的状态码发向前端页面
			resp.pushToClient(code);
		} catch (Exception e) {
			// e.printStackTrace();
			this.code = 500;
			//出现错误将code设置为500
		}
		try {
			//将错误code发往前端
			resp.pushToClient(code);
			//socket关闭
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

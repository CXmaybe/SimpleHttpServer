package com.httpserver.servlet;

import com.httpserver.server.Request;
import com.httpserver.server.Response;

public abstract class Servlet {
	//抽象为整个父类,供其他的子类继承
	
	public void service(Request req, Response resp) throws Exception {
		doGet(req, resp);
		doPost(req, resp);
	}
	
	public abstract void doGet(Request req, Response resp) throws Exception;
	public abstract void doPost(Request req, Response resp) throws Exception;
	
}

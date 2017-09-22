package com.httpserver.server;

import java.util.HashMap;
import java.util.Map;

public class ServletContext {
	//为每一个servlet取个别名
	//login-----LoginServlet---存完整路径通过反射区获取类的实例
	private Map<String, String> servlet;
	//url-----login
	private Map<String, String> mapping;
	
	public ServletContext() {
		servlet = new HashMap<>();
		mapping = new HashMap<>();
	}
	
	
	public Map<String, String> getServlet() {
		return servlet;
	}
	public void setServlet(Map<String, String> servlet) {
		this.servlet = servlet;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
}

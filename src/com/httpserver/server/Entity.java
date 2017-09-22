package com.httpserver.server;

/*
 * <servlet>
		<servlet-name>login</servlet-name>
		<servlet-class>com.chenxu.server.demo3.LoginServlet</servlet-class>
	</servlet>
 * 
 * 实体类-----对应web.xml中配置文件
 * 
 */
public class Entity {
	private String name;
	private String clz;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClz() {
		return clz;
	}
	public void setClz(String clz) {
		this.clz = clz;
	}
}

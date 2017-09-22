package com.httpserver.server;

import java.util.ArrayList;
import java.util.List;

/*
 *  <servlet-mapping>
 		<servlet-name>login</servlet-name>
 		<url-pattern>/login</url-pattern>
 	</servlet-mapping>
 	<servlet-mapping>
 		<servlet-name>login</servlet-name>
 		<url-pattern>/log</url-pattern>
 	</servlet-mapping>
 	一对多的映射关系
 	实体类-----保存web.xmlH中的内容
 	
 */
public class Mapping {
	private String name;
	private List<String> urlPattern;
	
	public Mapping() {
		this.urlPattern = new ArrayList<>();
	}

	public List<String> getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(List<String> urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

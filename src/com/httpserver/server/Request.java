package com.httpserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * 封装请求
 * 
 */
public class Request {
	//保存请求方法
	private String method;
	
	//请求资源
	private String url;
	
	//请求参数--同一个值可能会有多个属性
	private Map<String, List<String>> parameterMapValues;
	
	//回车+换行
	public static final String CRLF = "\r\n";
	//保存流
	private InputStream is;
	//请求信息
	private String requestInfo;
	
	//初始化相应的对象
	public Request() {
		this.method = "";
		this.url = "";
		this.parameterMapValues = new HashMap<>();
		this.requestInfo = "";
	}
	
	//重载的构造器
	public Request(InputStream is) {
		this();
		//如果去掉这句话-----会报ArrayOutOfBouns的错误.
		this.is = is;
		try {
			byte[] data = new byte[20480];
			int len = this.is.read(data);
			//构造请求头信息
			requestInfo = new String(data, 0, len);
		} catch(IOException e) {
			return ;
		}
		
		//分析头信息
		parseRequestInfo();
	}
	
	private void parseRequestInfo() {
		if(null == requestInfo && (requestInfo = requestInfo.trim()).equals("")) {
			return ;
		}
		
		/*
		 * 如果为get方式:请求方式    请求路径     请求参数(get可能存在)
		 * 
		 * 如果为post方式:请求参数可能在正文中
		 * 
		 */
		
		String paramString = ""; //接收请求参数
		
		//1.获取请求方式
		String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
		int idx = requestInfo.indexOf("/");
		this.method = firstLine.substring(0, idx).trim();
		String urlStr = firstLine.substring(idx, firstLine.indexOf("HTTP/")).trim();
		if(this.method.equalsIgnoreCase("post")) {
			this.url = urlStr;
			paramString = requestInfo.substring(requestInfo.lastIndexOf(CRLF)).trim();
		} else if(this.method.equalsIgnoreCase("get")) {
			if(urlStr.contains("?")) {
				String[] urlArray = urlStr.split("\\?");
				this.url = urlArray[0];
				paramString = urlArray[1];
			} else {
				this.url = urlStr;
			}
		}
		
		//不存在请求参数
		if("".equals(paramString)) {
			return ;
		}
		
		//2.如果存在则将请求参数封装在Map中
		parseParams(paramString);
	}
	
	private void parseParams(String paramString) {
		StringTokenizer token = new StringTokenizer(paramString, "&");
		while(token.hasMoreTokens()) {
			String keyValue = token.nextToken();
			String[] keyValues = keyValue.split("=");
			if(keyValues.length == 1) {
				keyValues = Arrays.copyOf(keyValues, 2);
				keyValues[1] = null;
			}
			
			String key = keyValues[0].trim();
			String value = keyValues[1] == null ? null : decode(keyValues[1].trim(), "gbk");
			
			//转换成Map
			if(!parameterMapValues.containsKey(key)) {
				parameterMapValues.put(key, new ArrayList<String>());
			}
			List<String> values = parameterMapValues.get(key);
			values.add(value);
		}
	}
	
	//解码方式--解决中文乱码问题
	private String decode(String value, String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//根据页面的name获取对应的多个值
	public String[] getParameterValues(String name) {
		List<String> values = null;
		if((values = parameterMapValues.get(name)) == null) {
			return null;
		} else {
			return values.toArray(new String[0]);
		}
	}
	
	//根据页面的name获取对应的单个值
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		if(null == values) {
			return "null";
		} 
		return values[0];
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}

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
 * ��װ����
 * 
 */
public class Request {
	//�������󷽷�
	private String method;
	
	//������Դ
	private String url;
	
	//�������--ͬһ��ֵ���ܻ��ж������
	private Map<String, List<String>> parameterMapValues;
	
	//�س�+����
	public static final String CRLF = "\r\n";
	//������
	private InputStream is;
	//������Ϣ
	private String requestInfo;
	
	//��ʼ����Ӧ�Ķ���
	public Request() {
		this.method = "";
		this.url = "";
		this.parameterMapValues = new HashMap<>();
		this.requestInfo = "";
	}
	
	//���صĹ�����
	public Request(InputStream is) {
		this();
		//���ȥ����仰-----�ᱨArrayOutOfBouns�Ĵ���.
		this.is = is;
		try {
			byte[] data = new byte[20480];
			int len = this.is.read(data);
			//��������ͷ��Ϣ
			requestInfo = new String(data, 0, len);
		} catch(IOException e) {
			return ;
		}
		
		//����ͷ��Ϣ
		parseRequestInfo();
	}
	
	private void parseRequestInfo() {
		if(null == requestInfo && (requestInfo = requestInfo.trim()).equals("")) {
			return ;
		}
		
		/*
		 * ���Ϊget��ʽ:����ʽ    ����·��     �������(get���ܴ���)
		 * 
		 * ���Ϊpost��ʽ:�������������������
		 * 
		 */
		
		String paramString = ""; //�����������
		
		//1.��ȡ����ʽ
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
		
		//�������������
		if("".equals(paramString)) {
			return ;
		}
		
		//2.������������������װ��Map��
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
			
			//ת����Map
			if(!parameterMapValues.containsKey(key)) {
				parameterMapValues.put(key, new ArrayList<String>());
			}
			List<String> values = parameterMapValues.get(key);
			values.add(value);
		}
	}
	
	//���뷽ʽ--���������������
	private String decode(String value, String code) {
		try {
			return java.net.URLDecoder.decode(value, code);
		} catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//����ҳ���name��ȡ��Ӧ�Ķ��ֵ
	public String[] getParameterValues(String name) {
		List<String> values = null;
		if((values = parameterMapValues.get(name)) == null) {
			return null;
		} else {
			return values.toArray(new String[0]);
		}
	}
	
	//����ҳ���name��ȡ��Ӧ�ĵ���ֵ
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

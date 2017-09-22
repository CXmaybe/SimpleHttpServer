package com.httpserver.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

public class Response {
	// 两个常量
	public static final String CRLF = "\r\n";
	public static final String BLACK = " ";

	// 存储头信息
	private StringBuilder headInfo;
	
	//构建正文
	private StringBuilder content;
	
	//流的处理
	private BufferedWriter bw;

	// 字段长度
	private int len;
	
	
	public Response() {
		this.headInfo = new StringBuilder();
		this.content = new StringBuilder();
		len = 0;
	}
	
	public Response(OutputStream os) {
		this();
		bw = new BufferedWriter(new OutputStreamWriter(os));
	}
	
	public Response(Socket client) {
		this();
		try {
			bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			headInfo = null;
		}
	}
	
	//提供输出流
	public Response println(String info) {
		content.append(info).append(CRLF);
		len += (info + CRLF).getBytes().length; //少加入CRLF的字节数
		return this;
	}
	
	public Response print(String info) {
		content.append(info);
		len += info.getBytes().length;
		return this;
	}

	private void createHeadInfo(int code) {
		//http版本,状态码,描述
		headInfo.append("HTTP/1.1").append(BLACK).append(code).append(BLACK);
		switch(code) {
			case 200:
				headInfo.append("OK");
				break;
			case 404:
				headInfo.append("NOT FOUND");
				break;
			case 500:
				headInfo.append("Internal Server Error");
				break;
		}
		headInfo.append(CRLF);
		//响应头(response head)
		headInfo.append("Server:chenxu Server/1.0").append(CRLF);
		headInfo.append("Date:").append(new Date()).append(CRLF);
		headInfo.append("Content-Type:text/html;charset=uft-8").append(CRLF);
		//正文长度
		headInfo.append("Content-Length:").append(len).append(CRLF);
		headInfo.append(CRLF);
	}
	
	//将状态码推向页面
	public void pushToClient(int code) throws IOException {
		if(headInfo == null) {
			code = 500;
		}
		createHeadInfo(code);
		bw.append(headInfo.toString());
		bw.append(content.toString());
		bw.flush();
	}
	
	public void close() {
		if(bw != null) {
			try {
				bw.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}

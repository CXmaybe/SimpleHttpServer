package com.httpserver.server;

import java.io.IOException;
import java.net.Socket;

import com.httpserver.servlet.Servlet;

/*
 * Dispatcher�ַ���---ÿ���յ�����ʹ���һ���߳�ȥ��������
 * 
 */
public class Dispatcher implements Runnable {
	//����socjet
	private Socket socket;
	//�������
	private Request req;
	//��Ӧ����
	private Response resp;
	//��Ӧ��
	private int code;

	/*
	 * ������---��ȡsocket
	 * ��ʼ���������Ӧ
	 * ��code��ֵΪ200
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
			//���ֶԴ���code����500---����������
			code = 500;
		}
	}

	public void run() {
		try {
			//��������url,ͨ������web.xml�ļ���Ӧ��,��ö�Ӧ��servletȥ����.
			Servlet servlet = WebApp.getServlet(req.getUrl());
			if (null == servlet) {
				//servletΪnull  ��code����Ϊ404
				this.code = 404;
			} else {
				//����servlet��service����
				servlet.service(req, resp);
			}
			//����Ӧ��״̬�뷢��ǰ��ҳ��
			resp.pushToClient(code);
		} catch (Exception e) {
			// e.printStackTrace();
			this.code = 500;
			//���ִ���code����Ϊ500
		}
		try {
			//������code����ǰ��
			resp.pushToClient(code);
			//socket�ر�
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

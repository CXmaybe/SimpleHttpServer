package com.httpserver.servlet;

import com.httpserver.server.Request;
import com.httpserver.server.Response;

public class LoginServlet extends Servlet {

	@Override
	public void doGet(Request req, Response resp) {
		resp.println("<html><head><title>��Ӧʾ��</title>");
		resp.println("</head><body>");
		resp.print("��ӭ").print(req.getParameter("uname")).print("����!");
		resp.println("</body></html>");		
	}
	
	@Override
	public void doPost(Request req, Response resp) {
		
	}

}

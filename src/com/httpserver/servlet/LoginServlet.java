package com.httpserver.servlet;

import com.httpserver.server.Request;
import com.httpserver.server.Response;

public class LoginServlet extends Servlet {

	@Override
	public void doGet(Request req, Response resp) {
		resp.println("<html><head><title>响应示例</title>");
		resp.println("</head><body>");
		resp.print("欢迎").print(req.getParameter("uname")).print("回来!");
		resp.println("</body></html>");		
	}
	
	@Override
	public void doPost(Request req, Response resp) {
		
	}

}

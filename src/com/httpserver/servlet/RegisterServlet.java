package com.httpserver.servlet;

import com.httpserver.server.Request;
import com.httpserver.server.Response;

public class RegisterServlet extends Servlet {

	@Override
	public void doGet(Request req, Response resp) {

	}

	@Override
	public void doPost(Request req, Response resp) {
		String str = "";
		if ("chenxu".equals(req.getParameter("uname"))
				&& "123456".equals(req.getParameter("pwd"))) {
			str = "��¼�ɹ�";
		} else {
			str = "��¼ʧ��";
		}

		resp.println("<html><head><title>ע��</title>");
		resp.println("</head><body>");
		resp.println(str);
		resp.println("</body></html>");
	}

}

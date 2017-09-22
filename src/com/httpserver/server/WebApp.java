package com.httpserver.server;

import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.httpserver.servlet.Servlet;

public class WebApp {
	private static ServletContext context;
	
	static {
		context = new ServletContext();
		
		try {
			// ��ȡ��������
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// ��ȡ������
			SAXParser sax = factory.newSAXParser();
			// ָ��xml+������ѡ��
			WebHandler web = new WebHandler();
			sax.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("WEB_INF/web.xml"), web);
			
			Map<String, String> servlet = context.getServlet();
			//�洢servlet-name��servlet-class
			for(Entity entity : web.getEntityList()) {
				servlet.put(entity.getName(), entity.getClz());
			}
			
			Map<String, String> mapping = context.getMapping();
			//�洢servlet-name��url-pattern
			for(Mapping mapp : web.getMappingList()){
				List<String> urls = mapp.getUrlPattern();
				for(String url : urls) {
					mapping.put(url, mapp.getName());
				}
			}
 			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//ͨ��ServletContext�е�����ȡ��,ʹ�÷��乹��servlet����.
	public static Servlet getServlet(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if((null == url) || (url = url.trim()).equals("")) {
			return null;
		}
//		return context.getServlet().get(context.getMapping().get(url));
		return (Servlet)(Class.forName(context.getServlet().get(context.getMapping().get(url)))).newInstance();
 	}
	
}

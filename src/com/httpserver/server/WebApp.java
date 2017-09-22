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
			// 获取解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 获取解析器
			SAXParser sax = factory.newSAXParser();
			// 指定xml+解析首选项
			WebHandler web = new WebHandler();
			sax.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("WEB_INF/web.xml"), web);
			
			Map<String, String> servlet = context.getServlet();
			//存储servlet-name和servlet-class
			for(Entity entity : web.getEntityList()) {
				servlet.put(entity.getName(), entity.getClz());
			}
			
			Map<String, String> mapping = context.getMapping();
			//存储servlet-name和url-pattern
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
	
	//通过ServletContext中的内容取出,使用反射构造servlet对象.
	public static Servlet getServlet(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if((null == url) || (url = url.trim()).equals("")) {
			return null;
		}
//		return context.getServlet().get(context.getMapping().get(url));
		return (Servlet)(Class.forName(context.getServlet().get(context.getMapping().get(url)))).newInstance();
 	}
	
}

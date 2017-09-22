package com.httpserver.server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//sax解析xml文件是以流模式来解析的,
public class WebHandler extends DefaultHandler {
	private List<Entity> entityList;
	private List<Mapping> mappingList;
	private Entity entity;
	private Mapping mapping;
	private String beginTag;
	private boolean isMapping = false;

	@Override
	public void startDocument() throws SAXException {
		// 文档的解析开始
		entityList = new ArrayList<>();
		mappingList = new ArrayList<>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// 开始元素
		if (null != qName) {
			beginTag = qName;
			if (qName.equals("servlet")) {
				isMapping = false;
				entity = new Entity();
			} else if (qName.equals("servlet-mapping")) {
				isMapping = true;
				mapping = new Mapping();
			}
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// 处理内容
		if (null != beginTag) {
			String str = new String(ch, start, length);
			if (isMapping) {
				if (beginTag.equals("servlet-name")) {
					mapping.setName(str);
				} else if (beginTag.equals("url-pattern")) {
					mapping.getUrlPattern().add(str);
				}
			} else {
				if (beginTag.equals("servlet-name")) {
					entity.setName(str);
				} else if (beginTag.equals("servlet-class")) {
					entity.setClz(str);
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (null != qName) {
			if (qName.equals("servlet")) {
				entityList.add(entity);
			} else if (qName.equals("servlet-mapping")) {
				mappingList.add(mapping);
			}
		}
		beginTag = null;
	}

	@Override
	public void endDocument() throws SAXException {
		// 文档解析结束
	}

	public List<Entity> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Entity> entityList) {
		this.entityList = entityList;
	}

	public List<Mapping> getMappingList() {
		return mappingList;
	}

	public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}

}

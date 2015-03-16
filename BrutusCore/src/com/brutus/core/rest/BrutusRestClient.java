package com.brutus.core.rest;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.brutus.base.Plugin;

@XmlRootElement(name = "plugin")
public class BrutusRestClient  extends Plugin{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int port = 8080;
	
	public BrutusRestClient() {
	super();
	}
	
	public BrutusRestClient(String name) {
		super(name);
	}
	
	@Override
	public boolean isEnable() {
		return enable;
	}
	@Override
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	} 	

	@XmlAttribute
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	

}

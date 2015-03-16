
package com.brutus.base;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;

//@XmlRootElement()
public class Plugin implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String name;
	protected String type;
	protected int timeout;
	protected boolean enable = true;
	
	public Plugin(){
	}
	public Plugin(String name){
		super();
		this.name = name;
	}
	public Plugin(String name, String type){
		super();
		this.name = name;
		this.type = type;
	}

	
	@XmlAttribute(name = "timeout")
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	@XmlAttribute(name = "enable")
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	} 
}

package com.brutus.base;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="brutus")
public class Brutus implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Core core;
	
	@XmlElement(name = "plugins",required = true)
	public Plugins plugins;
	
	
	@XmlElement(name = "core",required = true)
	public Core getCore() {
		return core;
	}

	public void setCore(Core core) {
		this.core = core;
	}
}

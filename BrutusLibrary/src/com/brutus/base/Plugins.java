package com.brutus.base;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="plugins")
public class Plugins implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="plugin")
	public List<Object> plugin;

	
	
}


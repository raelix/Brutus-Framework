package com.brutus.base;
import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType (propOrder={"tag","address"})
public class Device implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String tag;
	protected String address;
	protected String type;

	public Device(){
		super();
	}

	public Device(String tag){
		this.tag = tag;
	}

	public Device(String tag , String address){
		this.tag = tag;
		this.address = address;
	}

	@XmlAttribute
	public String getTag() {
		return tag;
	}
	
	public void setTag(String tag) {
		this.tag = tag;
	}

	@XmlAttribute
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString(){
		return "Device - name: "+getTag()+" - address: "+getAddress();	
	}
}

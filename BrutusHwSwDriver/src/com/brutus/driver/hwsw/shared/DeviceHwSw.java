package com.brutus.driver.hwsw.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.brutus.base.Device;
import com.brutus.shared.BrutusConf;

public class DeviceHwSw extends Device{
	private int port = 80;
	private ArrayList<ParamHwSw> param = new ArrayList<ParamHwSw>();
	@XmlTransient
	private ArrayList<ParamHwSw> request = new ArrayList<ParamHwSw>();

	public DeviceHwSw() {
	super();
	}
	
	public DeviceHwSw(String tag,String address) {
		super(tag, "127.0.0.1");
	}

	@XmlAttribute
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public ArrayList<ParamHwSw> getRequestParam(){
		return request;	
	}
	
	@XmlElement(name="param")
	public ArrayList<ParamHwSw> getParam() {
		return param;
	}

	public void setParam(ArrayList<ParamHwSw> param) {
		this.param = param;
	}
	public void addRequestParam(ParamHwSw param){
		if(request.indexOf(param) == -1)
			request.add(param);
	}

	public void resetRequest(){
		if(request != null)
			request.clear();
		request = new ArrayList<ParamHwSw>();
	}

	@Override
	@XmlAttribute
	public String getAddress() {
		return BrutusConf.getDeviceByName(getTag()).getAddress();
	}
	@Override
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	@XmlAttribute
	public String getType() {
		return BrutusConf.getDeviceByName(getTag()).getType();
	}

	public void setType(String type) {
		this.type = type;
	}
}

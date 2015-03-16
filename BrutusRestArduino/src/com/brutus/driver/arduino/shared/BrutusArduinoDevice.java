package com.brutus.driver.arduino.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import com.brutus.base.Device;
import com.brutus.shared.BrutusConf;

public class BrutusArduinoDevice extends Device{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BrutusArduinoParam> param = new ArrayList<BrutusArduinoParam>();
	private boolean invert;
	private int timeout;
	
	public BrutusArduinoDevice() {
		super();
	}
	
	public BrutusArduinoDevice(String name) {
		super(name);
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
	
	@XmlElement(name = "param")
	public ArrayList<BrutusArduinoParam> getParam() {
		return param;
	}

	public void setParam(ArrayList<BrutusArduinoParam> param) {
		this.param = param;
	}
	@XmlAttribute
	public boolean isInvert() {
		return invert;
	}

	public void setInvert(boolean invert) {
		this.invert = invert;
	}
	@XmlAttribute
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}

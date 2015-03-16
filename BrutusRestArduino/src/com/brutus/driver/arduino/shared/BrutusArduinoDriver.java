package com.brutus.driver.arduino.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.brutus.base.Plugin;

@XmlRootElement(name = "plugin")
public class BrutusArduinoDriver extends Plugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BrutusArduinoDevice> device = new ArrayList<BrutusArduinoDevice>();
	
	public BrutusArduinoDriver() {
		super();
	}
	
	public BrutusArduinoDriver(String name) {
		super(name);
	}
	
	@XmlElement(name="device")
	public ArrayList<BrutusArduinoDevice> getDevice() {
		return device;
	}
	
	public void setDevice(ArrayList<BrutusArduinoDevice> device) {
		this.device = device;
	}
	
	
	
}

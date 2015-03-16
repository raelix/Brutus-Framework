package com.brutus.client.modbus.shared;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.brutus.base.Plugin;

@XmlRootElement(name = "plugin")
public class BrutusModbusClient extends Plugin{

	private MasterModbus device;

	public BrutusModbusClient() {
		super();
	}

	public BrutusModbusClient(String name) {
		super(name);
	}

	@Override
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	} 

	@XmlElement(name="device")
	public MasterModbus getDevice() {
		return device;
	}

	public void setDevice(MasterModbus device) {
		this.device = device;
	}


}
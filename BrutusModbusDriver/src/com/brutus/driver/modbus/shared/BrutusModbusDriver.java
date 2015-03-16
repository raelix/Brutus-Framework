package com.brutus.driver.modbus.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.brutus.base.Param;
import com.brutus.base.Plugin;

@XmlRootElement(name = "plugin")
public class BrutusModbusDriver extends Plugin{

	private ArrayList<DeviceModbus> device;

	public BrutusModbusDriver() {
		super();
	}

	public BrutusModbusDriver(String name) {
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
	public ArrayList<DeviceModbus> getDevice() {
		return device;
	}


	public void setDevice(ArrayList<DeviceModbus> device) {
		this.device = device;
	}

	public DeviceModbus findDeviceModbusByTag(String tag){
		if(getDevice() != null && getDevice().size() > 0)
			for(DeviceModbus dev : getDevice())
				if(dev.getTag().contentEquals(tag))
					return dev;
		return null;
	}

	public ParamModbus findParamModbusByTag(String tag){
		if(getDevice() != null && getDevice().size() > 0)
			for(DeviceModbus dev : getDevice())
				if(dev.getParam() != null && dev.getParam().size() > 0)
					for(ParamModbus param : dev.getParam())
						if(param.getTag().contentEquals(tag))
							return param;
		return null;
	}

	public DeviceModbus findDeviceModbusByParamTag(String paramTag){
		if(getDevice() != null && getDevice().size() > 0){
			for(DeviceModbus dev : getDevice()){
				if(dev.getParam() != null && dev.getParam().size() > 0){
					for(ParamModbus param : dev.getParam()){
						if(param.getTag().contentEquals(paramTag)){
							return dev;
						}
					}
				}
			}
		}
		return null;
	}
	
	public DeviceModbus findSerialDevice(){
		if(getDevice() != null)
		for(DeviceModbus dev : getDevice()){
			if(dev.getType().toLowerCase().contains("rtu"))
				return dev;
		}
		return null;
	}

	public ArrayList<DeviceModbus> findDeviceModbusListByParamList(ArrayList<Param> params){
		ArrayList<DeviceModbus> dev = new ArrayList<DeviceModbus>();
		for(Param par : params){
			DeviceModbus temp = findDeviceModbusByParamTag(par.getTag());
			if(temp != null){
				if(dev.indexOf(temp) == -1){
					temp.addRequestParam(findParamModbusByTag(par.getTag()));
					dev.add(temp);

				}
				else{
					temp.addRequestParam(findParamModbusByTag(par.getTag()));
				}
			}
		}
		return dev;
	}
}
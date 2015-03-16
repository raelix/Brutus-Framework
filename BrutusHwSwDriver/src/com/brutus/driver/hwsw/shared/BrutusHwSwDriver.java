package com.brutus.driver.hwsw.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.brutus.base.Param;
import com.brutus.base.Plugin;

@XmlRootElement(name = "plugin")
public class BrutusHwSwDriver extends Plugin{

	private ArrayList<DeviceHwSw> device;

	public BrutusHwSwDriver() {
		super();
	}

	@XmlElement(name="device")
	public ArrayList<DeviceHwSw> getDevice() {
		return device;
	}

	public void setDevice(ArrayList<DeviceHwSw> device) {
		this.device = device;
	}

	public ArrayList<DeviceHwSw> findDeviceHwSwListByParamList(ArrayList<Param> params){
		ArrayList<DeviceHwSw> dev = new ArrayList<DeviceHwSw>();
		for(Param par : params){
			DeviceHwSw temp = findDeviceHwSwByParamTag(par.getTag());
			if(temp != null){
				if(dev.indexOf(temp) == -1){
					temp.addRequestParam(findParamHwSwByTag(par.getTag()));
					dev.add(temp);
				}
				else{
					temp.addRequestParam(findParamHwSwByTag(par.getTag()));
				}
			}
		}
		return dev;
	}
	
	public 	ArrayList<ParamHwSw> getAllParams(){
		ArrayList<ParamHwSw> par = new ArrayList<ParamHwSw>();
	for( DeviceHwSw dev : getDevice()){
		for(ParamHwSw pa : dev.getParam() ){
			par.add(pa);
		}
	}
		return par;
	}

	public ParamHwSw findParamHwSwByTag(String tag){
		if(getDevice() != null && getDevice().size() > 0)
			for(DeviceHwSw dev : getDevice())
				if(dev.getParam() != null && dev.getParam().size() > 0)
					for(ParamHwSw param : dev.getParam())
						if(param.getTag().contentEquals(tag))
							return param;
		return null;
	}

	public DeviceHwSw findDeviceHwSwByParamTag(String paramTag){
		if(getDevice() != null && getDevice().size() > 0){
			for(DeviceHwSw dev : getDevice()){
				if(dev.getParam() != null && dev.getParam().size() > 0){
					for(ParamHwSw param : dev.getParam()){
						if(param.getTag().contentEquals(paramTag)){
							return dev;
						}
					}
				}
			}
		}
		return null;
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
}

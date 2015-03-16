package com.brutus.driver.modbus.shared;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.brutus.base.Device;
import com.brutus.shared.BrutusConf;

public class DeviceModbus extends Device{

	private ArrayList<ParamModbus> param;
	private int port = 502;
	private int id = 1;
	private int baudrate = 9600;
	private int retries = 0;
	private int timeout = 1000;
	private int transdelay = 0;
	private ArrayList<ParamModbus> request = new ArrayList<ParamModbus>();

	public DeviceModbus() {
		super();
	}

	public DeviceModbus(String tag) {
		super(tag);
	}
	@Override
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@XmlElement(name = "param")
	public ArrayList<ParamModbus> getParam() {
		return param;
	}

	public void setParam(ArrayList<ParamModbus> param) {
		this.param = param;
	}

	public boolean hasChildWithTag(String tag){
		if(getParam() != null){
			for(ParamModbus param : getParam()){
				if(param.getTag().contentEquals(tag))
					return true;
			}
		}
		return false;
	}

	public ParamModbus findChildByTag(String tag){
		if(getParam() != null){
			for(ParamModbus param : getParam()){
				if(param.getTag().contentEquals(tag))
					return param;
			}
		}
		return null;
	}

	@XmlAttribute
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@XmlAttribute
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	@XmlAttribute
	public int getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(int baudrate) {
		this.baudrate = baudrate;
	}
	@XmlAttribute
	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}
	@XmlAttribute
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void addRequestParam(ParamModbus param){
		if(request.indexOf(param) == -1)
			request.add(param);
	}

	public void resetRequest(){
		if(request != null)
		request.clear();
		request = null;
		request = new ArrayList<ParamModbus>();
	}
	@XmlTransient
	public ArrayList<ParamModbus> getRequestParam(){
	return request;	
	}
	@XmlAttribute
	public int getTransdelay() {
		return transdelay;
	}

	public void setTransdelay(int transdelay) {
		this.transdelay = transdelay;
	}


}
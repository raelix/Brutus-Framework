package com.brutus.base;
import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder={"debugLevel","productKey","param","device"})
public class Core implements Serializable {

	private static final long serialVersionUID = 1L;
	private int debugLevel;
	private String productKey;
	private String idKey;
	private ArrayList<Device> device;
	private ArrayList<Param> param;

	public Core(){}
	public Core(String productKey){
		super();
		this.productKey = productKey;
		this.debugLevel = 0;
	}
	public Core(String productKey, int debugLevel){
		super();
		this.productKey = productKey;
		this.debugLevel = debugLevel;
	}
	@XmlAttribute
	public int getDebugLevel() {
		return debugLevel;
	}
	public void setDebugLevel(int debugLevel) {
		this.debugLevel = debugLevel;
	}
	@XmlAttribute
	public String getProductKey() {
		return productKey;
	}
	public void setProductKey(String productKey) {
		this.productKey = productKey;
	}
	@XmlElement(name = "device")
	public ArrayList<Device> getDevice() {
		return device;
	}
	public void setDevice(ArrayList<Device> device) {
		this.device = device;
	}

	@XmlElement(name = "param")
	public ArrayList<Param> getParam() {
		return param;
	}
	public void setParam(ArrayList<Param> param) {
		this.param = param;
	}
	@XmlTransient
	public ArrayList<Param> getRetentantParam() {
		ArrayList<Param> temp = new ArrayList<Param>();
		for(Param value : param){
			if(value.isRetentant())
				temp.add(value);
		}
		return temp;
	}
	@Deprecated
	public Object findParamValueByTag(String tag){
		if(getParam() != null)
		for(Param par : getParam()){
			if(par.getTag().contentEquals(tag))
				return tag;
		}
		return null;
	}
	@Override
	public String toString(){
		return "Core: - debugLevel: "+getDebugLevel()+" - ProductKey: "+getProductKey()+" parameter declared: "+(getParam() != null ? getParam().size() : 0);

	}
	
	@XmlAttribute
	public String getIdKey() {
		return idKey;
	}
	public void setIdKey(String idKey) {
		this.idKey = idKey;
	}
	
	
}

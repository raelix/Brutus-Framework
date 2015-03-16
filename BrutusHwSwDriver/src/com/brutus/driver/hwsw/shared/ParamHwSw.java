package com.brutus.driver.hwsw.shared;
import javax.xml.bind.annotation.XmlAttribute;
import com.brutus.base.Param;

public class ParamHwSw extends Param{

	private String type ;
	private int delay = 0;

	public ParamHwSw() {
		super();
	}
	
	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String getTag() {
		return tag;
	}
	@Override
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@XmlAttribute
	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	@Override
	public String toString(){
		return "Param tag: "+getTag()+" - type: "+getType();
	}
	

}

package com.brutus.base;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="param")
public class ParamExp extends Param {
	
	private static final long serialVersionUID = 1L;

	@Override
	@XmlElement
	public int getQuality() {
		return quality;
	}
	@Override
	public void setQuality(int quality) {
		this.quality = quality;
	}

	@Override
	@XmlElement
	public Object getValue() {
		return value;
	}
	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString(){
		return getTag()+" - "+getValue();
	}
	@Override
	@XmlElement
	public ArrayList<ParamHistory> getHistory() {
		return super.getHistory();
	}
	@Override
	public void setHistory(ArrayList<ParamHistory> history) {
		this.history = history;
	}
	@Override
	@XmlElement
	public ArrayList<ParamAlarm> getAlarm() {
		return super.getAlarm();
	}

	public void setAlarm(ArrayList<ParamAlarm> alarm) {
		this.alarm = alarm;
	}
}
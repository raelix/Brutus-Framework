package com.brutus.base;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ParamAlarm implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tag;
	private Long date;
	private int quality;
	private Object value;

	public ParamAlarm() {
	}

	public ParamAlarm(String tag) {
		this.tag = tag;
	}
	
	public ParamAlarm(String tag, Long date,int quality,Object value) {
		this.tag = tag;
		this.date = date;
		this.quality = quality;
		this.value = value;
	}
	
	public ParamAlarm(Param param) {
		this.tag = param.getTag();
		this.date = new Date().getTime();
		this.quality = param.getQuality();
		this.value = param.getValue();
	}
	
	@XmlAttribute
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	@XmlAttribute
	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}
	@XmlAttribute
	public int getQuality() {
		return quality;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}
	@XmlElement
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	 @Override
	 public String toString(){
		 return getTag()+" - "+getQuality()+" - "+getValue()+" - "+getDate();
	 }


}

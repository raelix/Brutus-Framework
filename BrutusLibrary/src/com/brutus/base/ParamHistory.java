package com.brutus.base;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="param")
public class ParamHistory implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String tag;
	protected Long date;
	protected int quality;
	protected Object value;
	protected double energyComputed;//energy consumption
	protected double price; //in euros

	public ParamHistory() {
	}

	public ParamHistory(String tag) {
		this.tag = tag;
	}
	
	public ParamHistory(String tag, int quality,Object value) {
		this.tag = tag;
		this.date = new Date().getTime();
		this.quality = quality;
		this.value = value;
	}
	
	public ParamHistory(String tag, Long date,int quality,Object value) {
		this.tag = tag;
		this.date = date;
		this.quality = quality;
		this.value = value;
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
	@XmlElement
	public double getEnergyComputed() {
		return energyComputed;
	}

	public void setEnergyComputed(double energyComputed) {
		this.energyComputed = energyComputed;
	}
	@XmlAttribute
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	


}

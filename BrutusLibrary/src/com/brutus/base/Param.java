package com.brutus.base;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="param")
public class Param implements Serializable{

	private static final long serialVersionUID = 1L;
	protected ArrayList<ParamHistory> history;
	protected ArrayList<ParamAlarm> alarm;
	protected int quality;
	protected Object value;
	protected String tag;
	protected boolean retentant = false;//default setting
	protected int polling = 2000;//default setting
	protected int minAlarm;
	protected int maxAlarm;
	protected boolean alarmEnable;
	protected boolean alarming;
	protected int repeat;
	protected int retriesTime;
	protected boolean logEnable;
	protected boolean energyCalculate;//power must be in watt 
	protected double multiplier;
	protected float costPerCent = 12; // default 0.12 cent euros
	
	public Param(){
	}
	
	public Param(String tag){
		super();
		this.tag = tag;
		
	}
	
	public Param(String tag,Object value){
		super();
		this.tag = tag;
		this.value = value;
		
	}
	
	public Param(int quality, Object value){
		super();
		this.quality = quality;
		this.value = value;
	}
	
	public Param(Param copy){
		super();
		this.tag = copy.getTag();
		this.quality = copy.getQuality();
		this.value = copy.getValue();
	}

	@XmlTransient
	public int getQuality() {
		return quality;
	}
	public void setQuality(int quality) {
		this.quality = quality;
	}
	
	@XmlTransient
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	@XmlTransient
	public boolean isRetentant() {
		return retentant;
	}
	public void setRetentant(boolean retentant) {
		this.retentant = retentant;
	}
	@XmlAttribute
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	@Override
	public String toString(){
		return "Param - name: "+getTag()+" - quality: "+getQuality()+" - value: "+getValue();	
	}
	@XmlAttribute
	public int getPolling() {
		return polling;
	}
	public void setPolling(int polling) {
		this.polling = polling;
	}
	@XmlTransient
	public int getMinAlarm() {
		return (int)minAlarm;
	}

	public void setMinAlarm(int minAlarm) {
		this.minAlarm = minAlarm;
	}
	@XmlTransient
	public int getMaxAlarm() {
		return (int)maxAlarm;
	}

	public void setMaxAlarm(int maxAlarm) {
		this.maxAlarm = maxAlarm;
	}
	@XmlTransient
	public boolean isAlarmEnable() {
		return alarmEnable;
	}

	public void setAlarmEnable(boolean alarmEnable) {
		this.alarmEnable = alarmEnable;
	}
	@XmlTransient
	public int getRepeat() {
		return repeat;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	@XmlTransient
	public boolean isAlarming() {
		return alarming;
	}

	public void setAlarming(boolean alarming) {
		this.alarming = alarming;
	}

	@XmlTransient
	public int getRetriesTime() {
		return retriesTime;
	}

	public void setRetriesTime(int retriesTime) {
		this.retriesTime = retriesTime;
	}
	
	public int incrementRetriesTime(){
		retriesTime+=1;
		return retriesTime;
	}
	@XmlTransient
	public ArrayList<ParamHistory> getHistory() {
		if(history == null)
			history = new ArrayList<ParamHistory>();
		return history;
	}

	public void setHistory(ArrayList<ParamHistory> history) {
		this.history = history;
	}
	@XmlTransient
	public ArrayList<ParamAlarm> getAlarm() {
		if(alarm == null)
			alarm = new ArrayList<ParamAlarm>();
		return alarm;
	}

	public void setAlarm(ArrayList<ParamAlarm> alarm) {
		this.alarm = alarm;
	}
	@XmlTransient
	public boolean isLogEnable() {
		return logEnable;
	}

	public void setLogEnable(boolean logEnable) {
		this.logEnable = logEnable;
	}
	@XmlTransient
	public boolean isEnergyCalculate() {
		return energyCalculate;
	}

	public void setEnergyCalculate(boolean energyCalculate) {
		this.energyCalculate = energyCalculate;
	}
	@XmlTransient
	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}

	public float getCostPerCent() {
		return costPerCent;
	}

	public void setCostPerCent(float costPerCent) {
		this.costPerCent = costPerCent;
	}
	
	
	
	
	

	
	
	
}
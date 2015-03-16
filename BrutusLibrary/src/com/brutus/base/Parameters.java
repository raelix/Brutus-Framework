package com.brutus.base;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "parameters")
public class Parameters implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="param")
	private ArrayList<ParamExp> param;

	public Parameters() {
	}

	public Parameters(ArrayList<Param>  paramt) {
		param = new ArrayList<ParamExp>();
		for(Param par : paramt){
			ParamExp pa = new ParamExp();
			pa.setPolling(par.getPolling());
			pa.setQuality(par.getQuality());
			pa.setRetentant(par.isRetentant());
			pa.setTag(par.getTag());
			pa.setValue(par.getValue());
			pa.setAlarm(par.getAlarm());
			pa.setAlarmEnable(par.isAlarmEnable());
			pa.setAlarming(par.isAlarming());
			pa.setRetentant(par.isRetentant());
			pa.setHistory(par.getHistory());
			pa.setMaxAlarm(par.getMaxAlarm());
			pa.setMinAlarm(par.getMinAlarm());
			pa.setRepeat(par.getRepeat());
			pa.setRetriesTime(par.getRetriesTime());
			param.add(pa);
		}
	}

	public Parameters(Param paramt) {
		param = new ArrayList<ParamExp>();
		ParamExp pa = new ParamExp();
		pa.setPolling(paramt.getPolling());
		pa.setQuality(paramt.getQuality());
		pa.setRetentant(paramt.isRetentant());
		pa.setTag(paramt.getTag());
		pa.setValue(paramt.getValue());
		param.add(pa);
	}

	//    public Params(ArrayList<Paramet>  param) {
	//	this.param = param;
	//    }
	public ArrayList<ParamExp> getParam() {
		if(param == null)
			param = new ArrayList<ParamExp>();
		return param;
	}

	public void setParam(ArrayList<ParamExp> param) {
		this.param = param;
	}

	public ParamExp getParamByTagName(String tagName){
		if(param != null && param.size() > 0){
			for(ParamExp par : param){
				if(par.getTag().contentEquals(tagName))
					return par;
			}
		}
		return null;
	}


}
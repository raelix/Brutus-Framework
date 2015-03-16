package com.brutus.core.event;
import java.util.ArrayList;
import java.util.Date;

import com.brutus.base.Param;
import com.brutus.base.ParamHistory;
import com.brutus.core.storage.MapAdapter;

public class EventRepeatExecutor extends Thread {
	Param oldParam;
	Param newParam;
	Param param;
	String tag;

	public EventRepeatExecutor(Param oldParam,Param newParam) {
		this.oldParam = oldParam;
		this.newParam = newParam;
		this.tag = newParam.getTag();
	}

	@Override
	public void run() {
		super.run();
		if(MapAdapter.getInstance().getParam(tag) == null){
			param = newParam;
			MapAdapter.getInstance().putParam(tag,param);
		}
		else
			param = MapAdapter.getInstance().getParam(tag);
		setParamValue(param);
		manageAlarm(param);
		manageLog(param);
//		MapAdapter.getInstance().putParam(tag,param);
	}

	public void setParamValue(Param param){
		param.setValue(newParam.getValue());					
		param.setQuality(newParam.getQuality());
	}

	public void manageLog(Param param){
		if(param.isLogEnable()){
			if(param.getHistory() != null && param.getHistory().size() > 0 ){
				ParamHistory lastParam = param.getHistory().get(param.getHistory().size() - 1);
				long lastRequest = lastParam.getDate();
				if(new Date().getTime() - lastRequest > param.getPolling()){
					ParamHistory logged = new ParamHistory(tag, param.getQuality(), param.getValue()); //create new history param
					if(param.isEnergyCalculate()){	//if have to energy calculate
						int lastElement = param.getHistory().size() - 1; 
						long lastLogDate = param.getHistory().get(lastElement).getDate(); 
						long timeElapsed = (logged.getDate() - lastLogDate) ;//in millisec time elapsed from last request
						double power = getParamDoubleValue(param);
						double hour = (((((double) timeElapsed / 1000) / 60) / 60 )  ); //time elapsed in hours
						double kWh = (hour * power) / 1000; // calculate energy 
						float totalCost = (float) (( kWh * param.getCostPerCent() ) / 100); //calculate amount of cost
						logged.setEnergyComputed(kWh);//set energy , not sum with last
						logged.setPrice(totalCost); //set price of this history consumption
						//logged.setEnergyComputed(kWh + lastParam.getEnergyComputed());//?????????? sommo con i precedenti o ognuno il suo?
					}
					param.getHistory().add(logged);
				}
			} 
			else {
				ArrayList<ParamHistory> history  = new ArrayList<ParamHistory>();
				history.add(new ParamHistory(tag, param.getQuality(), param.getValue()));
				param.setHistory(history);

			}
		}
	}
	public double getParamDoubleValue(Param param){
		if(param != null && param.getValue() != null){
			if(param.getValue() instanceof Integer){
				return ((Integer) param.getValue()).doubleValue();
			}
			else if(param.getValue() instanceof Double){
				return ((Double) param.getValue()).doubleValue();
			}
			else if(param.getValue() instanceof Long){
				return ((Long) param.getValue()).doubleValue();
			}
			else if(param.getValue() instanceof Short){	
				return ((Short) param.getValue()).doubleValue();
			}
			else if(param.getValue() instanceof Float){
				return ((Float) param.getValue()).doubleValue();
			}
		}
		return 0;
	} 

	public void manageAlarm(Param param){
		if(param.isAlarmEnable()){ //only if alarm or in future if log
			if(param.getValue() == null || oldParam.getQuality() == 0 && param.getQuality() == 180 || oldParam.getQuality() == 180 && param.getQuality() == 0){
				new AlarmExecutor(param,true).start(); //device disconnected
			}
			else if( param.getMaxAlarm() > param.getMinAlarm()){
				if(param.getValue() instanceof Integer){
					if(((Integer) param.getValue()).intValue() > param.getMaxAlarm() ||
							((Integer) param.getValue()).intValue() <  param.getMinAlarm()){
						new AlarmExecutor(param,false).start();
					}
				}
				else if(param.getValue() instanceof Double){
					if(((Double) param.getValue()).doubleValue() > param.getMaxAlarm() ||
							((Double) param.getValue()).doubleValue() <  param.getMinAlarm()){
						new AlarmExecutor(param,false).start();
					}
				}
				else if(param.getValue() instanceof Long){
					if((Long)param.getValue() > param.getMaxAlarm() ||
							(Long)param.getValue() <  param.getMinAlarm()){
						new AlarmExecutor(param,false).start();
					}
				}
				else if(param.getValue() instanceof Short){	
					if((Short)param.getValue() > param.getMaxAlarm() ||
							(Short) param.getValue() < param.getMinAlarm()){
						new AlarmExecutor(param,false).start();
					}
				}
				else if(param.getValue() instanceof Float){
					if((Float)param.getValue() > param.getMaxAlarm() ||
							(Float)param.getValue() <  param.getMinAlarm()){
						new AlarmExecutor(param,false).start();
					}
				}
			}
		}
	}



}
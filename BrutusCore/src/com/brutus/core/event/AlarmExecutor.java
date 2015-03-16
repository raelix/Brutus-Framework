package com.brutus.core.event;
import java.io.IOException;
import java.util.Date;

import com.brutus.base.Param;
import com.brutus.base.ParamAlarm;
import com.brutus.core.BrutusCore;
import com.brutus.core.storage.MapAdapter;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Debug;
import com.google.android.gcm.server.Message;

public class AlarmExecutor extends Thread {
	Param param;
	boolean connectionStatus;
	
	public AlarmExecutor(Param param,boolean connectionStatus) {
		super();
		this.param = param;
		this.connectionStatus = connectionStatus;
	}
	
	@Override
	public void run() {
		super.run();
		if( param.getRetriesTime() < param.getRepeat() || param.getRepeat() == 0 || connectionStatus ){
			if(connectionStatus && param.getQuality() == 0 || !connectionStatus)
			param.setAlarming(true);
//			else if(connectionStatus && param.getQuality() == 180)
//				param.setAlarming(false);
			Message message = new Message.Builder()
			.addData("message", param.getTag()+";"+param.getQuality()+";"+param.getValue()+";"+new Date().getTime())
			.addData("other-parameter", "Alarm")
			.build();
			Debug.printError("Sending alarm for param: "+param.getTag(),3);
			if( BrutusConf.getInstance().getCore().getIdKey() != null && !BrutusConf.getInstance().getCore().getIdKey().contentEquals("")){
				try {
					BrutusCore.sender.send(message,  BrutusConf.getInstance().getCore().getIdKey() , 3) ;
					if(!connectionStatus)
					param.incrementRetriesTime();
					param.getAlarm().add(new ParamAlarm(param));
					//salvo i cambiamenti sul db così al prossimo riavvio avrò ancora le allarmi e gli storici sui parametri ma non sul file di configurazione xml
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
			MapAdapter.getInstance().putParam(param.getTag(),param);
		}
	}
}

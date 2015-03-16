package com.brutus.core;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;

import com.brutus.adapter.CoreAdapter;
import com.brutus.base.Param;
import com.brutus.core.event.EventManager;
import com.brutus.core.event.EventRepeatExecutor;
import com.brutus.core.rest.BrutusRestClientStarter;
import com.brutus.core.storage.MapAdapter;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Debug;
import com.google.android.gcm.server.Sender;


//Addparam ,what plugin name? load xsd, what device name? new device,param description //AIzaSyDviw_0myCcDVDLX8GfAn5RG8IE02D4crw
//spedisco json/xml con aggiunta campi ,inutile aggiungere param senza plugin ,aggiungere type in plugin per sapere il  protocollo es:Modbus

public class BrutusCore extends CoreAdapter  {

	public static HashMap<String, Param> bufferKey = new HashMap<String, Param>();
	public static ArrayList<Param> buffer = new ArrayList<Param>();
	public static  Sender sender = new Sender("AIzaSyDviw_0myCcDVDLX8GfAn5RG8IE02D4crw");
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);//for param change value 
	protected RefreshBuffer updater;
	protected PollerThread poller;
	protected long lastRequest = 0L ;
	protected int waitStatus = 0;
	protected static EventManager eventManager;
	
	//implementare energy x log in rest e android, event con parser usano hash e dicono di leggere il file <Parametro nome,Nome Script>,usare moltiplicatore per i parametri  

	public BrutusCore(){
		super();
		buffer = BrutusConf.getInstance().getCore().getParam();//first buffer create by configuration params			

		try {
			LibrarySharedLoad.loadModules(this,serverIstance, clientIstance); 
			LibrarySharedLoad.addLibrary();
		} catch (Exception e) {
			Debug.printError("Cannot load plugins!", 1);
		}
		try {
			startServices();
		}
		catch (Exception e) {
			Debug.printError("Cannot start Brutus services!", 1);
		}
	}


	@Override
	public void run(){
		super.run();

	}


	public void startServices() throws InterruptedException{
		lastRequest = maxWait;
		//first complete request, wait recive response
		updater = new RefreshBuffer();
		updater.start();
		updater.join();

		eventManager = new EventManager();
		addPropertyChangeListener(eventManager);

		poller = new PollerThread();
		poller.start();

		BrutusRestClientStarter rest = new BrutusRestClientStarter(this);
		rest.start();

	}

	public void addPropertyChangeListener(PropertyChangeListener listener ){
		if(listener != null)
			pcs.addPropertyChangeListener(listener);
	}

	@Override
	public ArrayList<Param> getValues(ArrayList<Param> params) {//recuperati dal buffer aggiornato ogni 5 secondi e dopo la prima chiamata esterna ogni 2 secondi
		lastRequest = getTimeNowMs();

		if(waitStatus == longWaitStatus){
			poller.interrupt();
			poller = new PollerThread();
			poller.start();
		}

		ArrayList<Param> retList = new ArrayList<Param>();

		for(Param par :params){
			Param temp = MapAdapter.getInstance().getParam(par.getTag());
			temp.setValue(bufferKey.get(par.getTag()).getValue());
			temp.setQuality(bufferKey.get(par.getTag()).getQuality());
			retList.add(temp);
		}

		return retList;
	}

	public class PollerThread  extends Thread{
		@Override
		public void run() {
			super.run();
			try {
				while(true){
					if(updater != null){
						updater.interrupt();
					}
					updater = new RefreshBuffer();
					updater.start();
					updater.join();
				}
			} catch (InterruptedException e) {}
		}
	}

	public class RefreshBuffer extends Thread{//recupero i dati in base al tempo, memorizzati poi nel buffer, chiamata continua ogni 2/5 secondi
		@Override
		public void run() {
			super.run();
			try {
				if(getTimeNowMs() - lastRequest > maxWait ){
					waitStatus = longWaitStatus;
					sleep(longWait);
					getValue(buffer);//problema interruzione
				}
				else{
					waitStatus = shortWaitStatus;
					sleep(shortWait);
					getValue(buffer);
				}
				//fire event for param changes
				for(Param par : buffer){
					Param oldParam = par;
					if(bufferKey.get(par.getTag()) != null) {
						oldParam = bufferKey.get(par.getTag());
						if((oldParam.getValue() != null && par.getValue() != null 
								|| oldParam.getValue() != null && par.getValue() == null 
								|| oldParam.getValue() == null && par.getValue() != null )
								&& (oldParam.getValue() != par.getValue() ||
								oldParam.getQuality() != par.getQuality())){							
							pcs.firePropertyChange(par.getTag(),oldParam.getQuality()+":"+oldParam.getValue() ,par.getQuality()+":"+par.getValue());
						}						
					}

					Param instanceBuffer = new Param(par);
					new EventRepeatExecutor(oldParam,instanceBuffer).start();
					bufferKey.put(instanceBuffer.getTag(), instanceBuffer);
				}

			} catch (InterruptedException e) {}
		}
	}





}
package com.brutus.adapter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import com.brutus.base.Param;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Constants;
import com.brutus.shared.Debug;

public class CoreAdapter extends Thread implements Constants {

	public  Object lock = new Object();
	public static int counter = 0;
	protected LinkedList<DriverAdapter> serverIstance = new LinkedList<DriverAdapter>();
	protected LinkedList<ClientAdapter> clientIstance = new LinkedList<ClientAdapter>();

	public void reboot(){
		Debug.print("Rebooting server... \n wait...", 0);
		System.exit(5);
	}

	public void setValues(ArrayList<Param> params){
		synchronized (lock) {
			if(counter == LIMIT_CLIENT){
				try {
					lock.wait();
				} catch (InterruptedException e) {
				}
			}
			counter++;
		}
		if(serverIstance != null)
			for(DriverAdapter adapt : serverIstance){
//			new NoBlockingSetterThread(adapt, params).start();
				adapt.setValues(params);
			}
		synchronized (lock) {
			lock.notify();
			counter--;
		}
	}

//	class NoBlockingSetterThread extends Thread {
//		
//		DriverAdapter adp ;
//		ArrayList<Param> par;
//		
//		public NoBlockingSetterThread(DriverAdapter adp, ArrayList<Param> par) {
//		this.adp = adp;
//		this.par = par;
//		}
//		
//		@Override
//		public void run() {
//			adp.setValues(par);
//			super.run();
//		}
//		
//	}

	public long getTimeNowMs(){
		return new Date().getTime();
	}


	public  ArrayList<Param> getValues(ArrayList<Param> params){
		return getValue(params);
	}


	public  ArrayList<Param> getValue(ArrayList<Param> params){
		synchronized (lock) {
			if(counter == LIMIT_CLIENT){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			counter++;
			if(serverIstance == null || serverIstance.size() <= 0){
				Debug.printError("No Servers instance found "+"\n",5);
				for(Param temp : params){
					temp.setQuality(BAD_QUALITY);
				}
				lock.notify();
				counter--;
				return params;
			}

			LinkedList<FutureTask<ArrayList<Param>>> serverQueue = new LinkedList<FutureTask<ArrayList<Param>>>();
			ExecutorService executor = Executors.newFixedThreadPool(serverIstance.size());

			for(DriverAdapter temp : serverIstance){
				temp.setBuffer(params);
				serverQueue.add(new FutureTask<ArrayList<Param>>(temp));
			}

			// This will make the executor accept no new threads
			// and finish all existing threads in the queue
			// executor.shutdown();		

			String name = "";
			int time = 0;
			long timeElapsedForResponse = System.nanoTime();
			System.out.println("\n*****************************************");
			//while(!executor.isTerminated())

			for(int k = 0; k < serverQueue.size()  ; k++){
				try {

					executor.execute(serverQueue.get(k));
					long startTime = System.nanoTime();
					//name without Starter
					name = serverIstance.get(k).getClass().getSimpleName().replace("Starter", "");
					//timeout of plugin
					time = BrutusConf.getPluginTimeout(name);
					//check if param is in this device
						if(time == 0) {
							serverQueue.get(k).get(SERVER_WAIT, TimeUnit.MILLISECONDS);														
						}
						else {
							serverQueue.get(k).get(time, TimeUnit.MILLISECONDS);							
						}

						long elapsed = (System.nanoTime() - startTime) / 1000000;
						Debug.print(name+" answer after: "+elapsed+" ms",3);
					
				}
				catch (Exception e) {
					e.getStackTrace();
					Debug.print(""+name+" not reply after: "+(time == 0 ? SERVER_WAIT : time)+" ms",3);
					continue;
				}
			}
			lock.notify();
			counter--;
			System.out.println("Total time for generating response: "+(System.nanoTime() - timeElapsedForResponse)/ 1000000+" ms");
			System.out.println("*****************************************\n");
			executor.shutdown();
		}
		return params;


	}


}

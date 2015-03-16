package com.brutus.driver.arduino;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.brutus.adapter.DriverAdapter;
import com.brutus.base.Param;
import com.brutus.driver.arduino.shared.BrutusArduinoDevice;
import com.brutus.driver.arduino.shared.BrutusArduinoDriver;
import com.brutus.driver.arduino.shared.BrutusArduinoParam;
import com.brutus.driver.arduino.shared.Response;
import com.brutus.shared.BrutusConf;
import com.google.gson.Gson;

public class BrutusArduinoDriverStarter extends DriverAdapter {

	public static BrutusArduinoDriver plugin;
	public HashMap<String, BrutusArduinoParam> paramHash = new HashMap<String, BrutusArduinoParam>();
	//	public  static Object lock = new Object();
	//	public static int counter = 0;
	//	public static int LIMIT_CLIENT = 2;
	public static final int limit_error = 4;

	public BrutusArduinoDriverStarter() {
		super();
		init();
	}

	public void init(){
		try {
			plugin = (BrutusArduinoDriver) BrutusConf.loadPluginClass(BrutusArduinoDriver.class, "BrutusArduinoDriver");
		} catch (Exception e) {
			System.err.println("No configuration found for BrutusArduinoDriver! will exit now!");
			return;
		}
		initPin();
	}

	public void initPin(){
		if(plugin == null)
			return;
		for(BrutusArduinoDevice dev : plugin.getDevice()){
			ArrayList<BrutusArduinoParam> param = dev.getParam();
			for(BrutusArduinoParam par : param){
				par.setDevAddress(dev.getAddress());
				par.setDevTimeout(dev.getTimeout());
				par.setInverted(dev.isInvert());
				paramHash.put(par.getTag(), par);
				if(par.getVariable() == null || par.getVariable().contentEquals("")){
					String request = dev.getAddress() + "/" + "mode" + "/" + par.getPin() + "/" + par.getDirection() ; 
					new simpleRequest(request, dev.getTimeout()).start();
				}
			}
		}
	}
	public class simpleRequest extends Thread{
		
		String request;
		int timeout;
		
		public  simpleRequest(String request,int timeout) {
			this.request = request;
			this.timeout = timeout;
		}
		 @Override
		public void run(){
			super.run();
			 if(!request.startsWith("http://"))
				request = "http://" + request;
			String uri = request;
			URL url;
			try {
				url = new URL(uri);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				//connection.setRequestProperty("Accept", "application/json");
				connection.setConnectTimeout( timeout);
//				connection.setReadTimeout(timeout);
				//InputStream xml = 
				connection.getInputStream();
				//JSONObject object = (JSONObject) JSONValue.parse(new InputStreamReader(xml));
				//object.get("age").toString();
				connection.disconnect();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

	@Override
	public void setValues(final ArrayList<Param> buffer) {
		new Thread(){
			@Override
			public void run() {
				for(int k = 0 ; k < buffer.size() ; k++){
					Param par = buffer.get(k);
					if(paramHash.get(par.getTag()) != null){
						BrutusArduinoParam paramArduino = paramHash.get(par.getTag());
						int value = -1;
						if(par.getValue() != null){
							if(par.getValue() instanceof Double)
								value = ((Double) par.getValue()).intValue();
							else if(par.getValue() instanceof Long)
								value = ((Long) par.getValue()).intValue();
							else if(par.getValue() instanceof Float)
								value = ((Float) par.getValue()).intValue();
							else if(par.getValue() instanceof Short)
								value = ((Short) par.getValue()).intValue();
							else if(par.getValue() instanceof Integer)
								value = (int) par.getValue();
							if(value != -1){
								if(paramArduino.isInverted()){
									if(value == 0)
										value = 1;
									else
										value = 0;
								}
								new simpleRequest(paramArduino.getDevAddress() + "/" + paramArduino.getMode() + "/" + paramArduino.getPin() + "/" + value, paramArduino.getDevTimeout()).start();				
							}
						}
					}
				}
				super.run();
			}
		}.start();
		
	}

	@Override
	public ArrayList<Param> OnParamsRequest(ArrayList<Param> buffer) {
		for(int k = 0 ; k < buffer.size() ; k++){
			Param par = buffer.get(k);

			if(paramHash.get(par.getTag()) != null){
				BrutusArduinoParam paramArduino = paramHash.get(par.getTag());
				if(paramArduino.getVariable() == null || paramArduino.getVariable().contentEquals("")){
					buffer =  getDataSpec(buffer, paramArduino, k);
				}
				else{
					buffer = getData(buffer, paramArduino, k);
				}
			}
		}
		return buffer;
	}

		public ArrayList<Param> getData(ArrayList<Param> buffer,BrutusArduinoParam paramArduino,int positionBuffer){
			String request = paramArduino.getDevAddress() + "/" + paramArduino.getVariable()   ; //read command ex: 192.168.0.2/digital/5/r
			Param paramInBuffer = buffer.get(positionBuffer);
			if(!request.startsWith("http://"))
				request = "http://" + request;
			String uri = request;
			URL url;
			try {
				url = new URL(uri);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
				connection.setConnectTimeout( paramArduino.getDevTimeout());
//				connection.setReadTimeout( paramArduino.getDevTimeout());
				//System.out.println("request "+request+" timeout "+paramArduino.getDevTimeout());
				InputStream xml = connection.getInputStream();
				JSONObject object = (JSONObject) JSONValue.parse(new InputStreamReader(xml));
				int value = Integer.parseInt(object.get(paramArduino.getVariable()).toString());
				boolean connected = Boolean.parseBoolean(object.get("connected").toString());
				if(connected){
					paramInBuffer.setValue(value);
					paramInBuffer.setQuality(180);
				}
				else{
					paramInBuffer.setValue(value);
					paramInBuffer.setQuality(0);
				}
				buffer.set(positionBuffer,paramInBuffer);
				connection.disconnect();
				paramArduino.setError(0);
				paramHash.put(paramArduino.getTag(), paramArduino);
				return buffer;
			} catch (IOException e) {
				if(paramArduino.getError() == limit_error){
					paramInBuffer.setValue(null);
					paramInBuffer.setQuality(0);
					buffer.set(positionBuffer,paramInBuffer);
					paramArduino.setError(0);
				}
				else{
					paramArduino.setError(paramArduino.getError() + 1);
					paramHash.put(paramArduino.getTag(), paramArduino);					
				}
			}
			return buffer;
		}



		public ArrayList<Param> getDataSpec(ArrayList<Param> buffer,BrutusArduinoParam paramArduino,int positionBuffer){
			String request = paramArduino.getDevAddress() + "/" + paramArduino.getMode() + "/" + paramArduino.getPin() + "/r"  ; //read command ex: 192.168.0.2/digital/5/r
			Param paramInBuffer = buffer.get(positionBuffer);
			if(!request.startsWith("http://"))
				request = "http://" + request;
			String uri = request;
			URL url;
			try {
				url = new URL(uri);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
				connection.setConnectTimeout( paramArduino.getDevTimeout());
//				connection.setReadTimeout( paramArduino.getDevTimeout());
				InputStream xml = connection.getInputStream();
				Gson gson = new Gson();
				Response resp = gson.fromJson(new InputStreamReader(xml), Response.class);
				if(resp.isConnected()){
					if(paramArduino.isInverted()){
						if(resp.getReturn_value() == 0)
							resp.setReturn_value(1);
						else
							resp.setReturn_value(0);
					}
					paramInBuffer.setValue(resp.getReturn_value());
					paramInBuffer.setQuality(180);
				}
				else{
					paramInBuffer.setValue(resp.getReturn_value());
					paramInBuffer.setQuality(0);
				}
				buffer.set(positionBuffer,paramInBuffer);
				connection.disconnect();
				paramArduino.setError(0);
				paramHash.put(paramArduino.getTag(), paramArduino);
				return buffer;
			} catch (IOException e) {
				if(paramArduino.getError() == limit_error){
					paramInBuffer.setValue(null);
					paramInBuffer.setQuality(0);
					buffer.set(positionBuffer,paramInBuffer);
					paramArduino.setError(0);
				}
				else{
					paramArduino.setError(paramArduino.getError() + 1);
					paramHash.put(paramArduino.getTag(), paramArduino);					
				}
			}
			return buffer;
		}
}

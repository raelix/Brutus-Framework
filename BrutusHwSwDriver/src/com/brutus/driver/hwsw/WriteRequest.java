package com.brutus.driver.hwsw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.brutus.base.Param;
import com.brutus.driver.hwsw.shared.DeviceHwSw;
import com.brutus.driver.hwsw.shared.ParamHwSw;

public class WriteRequest extends Thread{

	public static final String prefix = "/forms.htm?";
	public static final String suffix = "http://";

	public  WriteRequest(ArrayList<Param> buffer,DeviceHwSw dev){
		super();
		for(Param pars : buffer){
			for(ParamHwSw par : dev.getRequestParam()){
				if(par.getTag().contentEquals(pars.getTag())){
					int value = getValueByTagName(buffer, par.getTag());
					try {
						new Switch(dev, par, value).start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	class Switch extends Thread{
		DeviceHwSw dev; ParamHwSw par; int value;
		public Switch(DeviceHwSw dev, ParamHwSw par, int value){
			super();
			this.dev = dev;
			this.par = par;
			this.value = value;
		}
		@Override
		public void run(){
			super.run();	
			try {
				if(value == 1 && par.getDelay() != 0){
					sendGet(dev, par.getType(), value);
					sleep(par.getDelay());
					sendGet(dev, par.getType(), 0);
				}
				else sendGet(dev, par.getType(), value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void sendGet(DeviceHwSw dev, String concat, int value) throws Exception {
			if(!dev.getAddress().startsWith("http://"))
				dev.setAddress(suffix + dev.getAddress());
			String url = dev.getAddress()+prefix+concat+"="+value;
			//			System.out.println(url);
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			//			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			//			System.out.println("Response Code : " + responseCode);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//			System.out.println(response.toString());
		}
	}


	public int getValueByTagName(ArrayList<Param> buffer,String name){
		for(Param par : buffer){
			if(par.getTag().contains(name)){
				if( par.getValue() != null){
					if(par.getValue() instanceof Double)
					return ((Double) par.getValue()).intValue();
					if(par.getValue() instanceof Long)
						return ((Long) par.getValue()).intValue();
					else if(par.getValue() instanceof Float)
						return ((Float) par.getValue()).intValue();
					else if(par.getValue() instanceof Short)
						return ((Short) par.getValue()).intValue();
					else if(par.getValue() instanceof Integer)
					return (int) par.getValue();
				}
			}
		}
		return 0;
	}
}

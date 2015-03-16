package com.apc.starter;
import java.io.IOException;

import javax.usb.util.UsbUtil;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDManager;


@Path("/")
public class EntryPoint {
	static final int VENDOR_ID = 1309;
	static final int PRODUCT_ID = 0002;
	private static final int BUFSIZE = 6;
	static final int percentBattery = 1;	
	static final int minLeft = 2;
	static final int voltage = 3;
	static final int power = 5;
	byte id6 = 	 (byte) 0x06; //charging/discharging
	byte id12 =  (byte) 0x0C;
	byte id35 =  (byte) 0x23;
	byte id49 =  (byte) 0x31;
	byte id80 =  (byte) 0x50;
	byte id82 =  (byte) 0x52;


	//---------------------------   POWER    -----------------------------------	

	//http://localhost:8080/ups/power
	@GET
	@Path("/power")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String power() throws IOException {
		return elaborateRequest(power);
	}	


	//-------------------------minLeft-----------------------------------

	//http://localhost:8080/ups/minLeft
	@GET
	@Path("/minLeft")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String minLeft() throws IOException {
		return elaborateRequest(minLeft);
	}	

	//---------------------------voltage---------------------------------------	

	//http://localhost:8080/ups/voltage
	@GET
	@Path("/voltage")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String voltage() throws IOException {
		return elaborateRequest(voltage);
	}	

	//----------------------------------------------------------------------------	


	
	
	public String elaborateRequest(int requestCode) throws IOException{
		HIDDevice dev = null;
		@SuppressWarnings("unused")
		int n = 0;
		byte[] buf = new byte[BUFSIZE];
		HIDManager hid_mgr = HIDManager.getInstance();
		for(int i = 0 ; i < hid_mgr.listDevices().length ; i++){
			if(hid_mgr.listDevices()[i].getVendor_id() == VENDOR_ID)
				dev = hid_mgr.listDevices()[i].open();
		}
		buf[0] = id6;
		n = dev.getFeatureReport(buf) ;
		int charging =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
		int discharging =  Integer.parseInt(UsbUtil.toHexString (buf[2]),16); 
		buf[0] = id80;
		n = dev.getFeatureReport(buf) ;
		int usingPercentCapacity =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
		buf[0] = id12;
		n = dev.getFeatureReport(buf) ;
		int percentBattery =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
		buf[0] = id49;
		n = dev.getFeatureReport(buf) ;
		int inputVoltage =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
		boolean connected = discharging == 1 ? false : true;
		boolean chargingDev = charging == 1 ? true : false;

		switch(requestCode){

		case minLeft:
			buf[0] = id12;
			n = dev.getFeatureReport(buf) ;
			String first ="";
			if(UsbUtil.toHexString (buf[3]).startsWith("0")) first = UsbUtil.toHexString (buf[3]).substring(0);
			else first = UsbUtil.toHexString (buf[3]);
			int duringSecondsTime =  Integer.parseInt(first+UsbUtil.toHexString (buf[2]),16) / 60; 
			return produceJson("MinuteRemain", ""+duringSecondsTime, connected, chargingDev,usingPercentCapacity, percentBattery,inputVoltage);
		case voltage:
			buf[0] = id49;
			n = dev.getFeatureReport(buf) ;
			 inputVoltage =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
			return produceJson("voltage", ""+inputVoltage, connected, chargingDev,usingPercentCapacity, percentBattery,inputVoltage);
		case power:
			buf[0] = id80;
			n = dev.getFeatureReport(buf) ;
			usingPercentCapacity =  Integer.parseInt(UsbUtil.toHexString (buf[1]),16); 
			int watt = ((usingPercentCapacity*330)/100);
			return produceJson("power", ""+watt, connected, chargingDev,usingPercentCapacity, percentBattery,inputVoltage);
		}
		return produceJson("empty", "empty", connected, chargingDev,usingPercentCapacity, percentBattery,inputVoltage);
	}


	public String produceJson(String name, String value, boolean connected, boolean charging, int usingPercentCapacity,int batteryLoadPercent,int voltage){
		return "{\""+name+"\": "+value+", \"id\": \""+VENDOR_ID+"\", \"name\": \"ups\", \"connected\": "+connected+", \"charging\": "+charging+", \"voltage\": "+voltage+", \"power use\": \""+usingPercentCapacity+"%\", \"battery\": \""+batteryLoadPercent+"%\"}";
	}
}
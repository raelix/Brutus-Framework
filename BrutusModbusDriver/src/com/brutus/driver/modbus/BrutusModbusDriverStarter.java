package com.brutus.driver.modbus;
import java.util.ArrayList;

import com.brutus.adapter.DriverAdapter;
import com.brutus.base.Param;
import com.brutus.driver.modbus.shared.DeviceModbus;
import com.brutus.driver.modbus.shared.GenericRtuRequest;
import com.brutus.driver.modbus.shared.GenericTcpRequest;
import com.brutus.driver.modbus.shared.LibraryUtils;
import com.brutus.driver.modbus.shared.ParamModbus;
import com.brutus.driver.modbus.shared.BrutusModbusDriver;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Debug;

public class BrutusModbusDriverStarter extends DriverAdapter {
	BrutusModbusDriver context ;
	public final static boolean IsWriting = true;
	public final static boolean IsReading = false;
	
	public BrutusModbusDriverStarter() {
		super();
		Debug.print("BrutusModbusDriver started...", 2);
		LibraryUtils.installLibrary();
		init();
	}

	public void init(){
		try {
			context = (BrutusModbusDriver) BrutusConf.loadPluginClass(BrutusModbusDriver.class, "BrutusModbusDriver");
		} catch (Exception e) {
			System.err.println("No configuration found for BrutusModbusDriver will now exit!");
			return;
		}
	}

	@Override
	public void setValues(ArrayList<Param> buffer) {
		if(context == null)
			return;
		ArrayList<DeviceModbus> request = context.findDeviceModbusListByParamList(buffer);

		for(DeviceModbus dev : request){
			Debug.print("BrutoModbusDriver: writing to "+dev,4);
			if(dev.getType().contentEquals("tcp")){
				new GenericTcpRequest(dev).request(buffer,IsWriting);
//				new DeviceRequestTCP().writeRequest(dev, buffer);
				}
			if(dev.getType().contentEquals("rtu")){
				new GenericRtuRequest(dev).request(buffer,IsWriting);
//				DeviceRequestRTU temp =	new DeviceRequestRTU(dev);
//				temp.connect();
//				temp.writeRequest(dev, buffer);
//				temp.close();
				}
		}
		for(DeviceModbus dev : request)
		dev.resetRequest();
	}

	@Override
	public ArrayList<Param> OnParamsRequest(ArrayList<Param> buffer) {
		if(context == null)
			return buffer;
		ArrayList<DeviceModbus> request = context.findDeviceModbusListByParamList(buffer);
		for(DeviceModbus dev : request){
			if(dev.getType().contentEquals("tcp")){
				new GenericTcpRequest(dev).request(buffer,IsReading);
//				new DeviceRequestTCP().request(dev);
			}
			if(dev.getType().contentEquals("rtu")){
				new GenericRtuRequest(dev).request(buffer,IsReading);
//				DeviceRequestRTU temp =	new DeviceRequestRTU(dev);
//				temp.connect();
//				temp.request(dev);
//				temp.close();
			}
		}
		ArrayList<Param> im = buffer;
		for(DeviceModbus dev : request){
			for(ParamModbus par : dev.getRequestParam()){
				im = setParamValueByTagName(buffer, par.getTag(), par.getValue(), par.getQuality());
//				Debug.print("recived value for "+par.getTag()+" value: "+par.getValue()+" quality: "+par.getQuality(),4);
			}
			dev.resetRequest();
		}
		return im;
	}
	
	public ArrayList<Param> setParamValueByTagName(ArrayList<Param> buffer, String tag, Object value,int quality){
		if(buffer != null)
		for(Param par : buffer){
			if(par.getTag().contentEquals(tag)){
				par.setValue(value);
				par.setQuality(quality);
			}
		}
		return buffer;
	}

}

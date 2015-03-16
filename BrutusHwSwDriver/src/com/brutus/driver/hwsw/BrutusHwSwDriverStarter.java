package com.brutus.driver.hwsw;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBException;
import com.brutus.adapter.DriverAdapter;
import com.brutus.base.Param;
import com.brutus.driver.hwsw.shared.DeviceHwSw;
import com.brutus.driver.hwsw.shared.BrutusHwSwDriver;
import com.brutus.shared.BrutusConf;
import com.brutus.shared.Debug;

public class BrutusHwSwDriverStarter extends DriverAdapter {
	BrutusHwSwDriver context ;

	public BrutusHwSwDriverStarter() {
		super();
		Debug.print("BrutusHwSwDriver started...", 2);
		init();
	}

	public void init(){
		try {
			context = (BrutusHwSwDriver) BrutusConf.loadPluginClass(BrutusHwSwDriver.class, "BrutusHwSwDriver");
		} catch (Exception e) {
			System.err.println("No configuration found for BrutusHwSwDriver! will exit now!");
			return;
		}
	}

	@Override
	public void setValues(final ArrayList<Param> buffer) {
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ArrayList<DeviceHwSw> request = context.findDeviceHwSwListByParamList(buffer);
				for(DeviceHwSw dev : request){
					new WriteRequest(buffer, dev).start();
				}
				super.run();
			}
		}.start();
	}

	@Override
	public ArrayList<Param> OnParamsRequest(ArrayList<Param> buffer) {//test qui del tempo impiegato
		ArrayList<DeviceHwSw> request = context.findDeviceHwSwListByParamList(buffer);
		ArrayList<Param> temp = null;
		for(DeviceHwSw dev : request){
			try {
				temp = 	new StatusRequest().getResponse( buffer,dev,context);
			} catch (IOException | JAXBException e) {
//				e.printStackTrace();
				System.err.println("BrutusHwSwDriver: No response from device "+dev.getTag());
			}
		}
		if(temp != null) return temp;
		else{
				for(Param have : context.getAllParams()){
					for(Param par : buffer){
					
					if(par.getTag().contentEquals(have.getTag())){
						par.setQuality(0);
						buffer.set(buffer.indexOf(par), par);
					}
				}
			}
			return buffer;
		}
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

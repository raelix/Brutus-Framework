package com.brutus.driver.hwsw;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.brutus.base.Param;
import com.brutus.driver.hwsw.shared.BrutusHwSwDriver;
import com.brutus.driver.hwsw.shared.DeviceHwSw;
import com.brutus.driver.hwsw.shared.ParamHwSw;
import com.brutus.driver.hwsw.shared.Response;

public class StatusRequest {
	BrutusHwSwDriver context;
	public static final String suffix = "http://";
	public static final String prefix = "/status.xml";

	public ArrayList<Param> getResponse(ArrayList<Param> buffer,DeviceHwSw dev,BrutusHwSwDriver context) throws IOException, JAXBException{
		if(!dev.getAddress().startsWith("http://"))
			dev.setAddress(suffix + dev.getAddress());
		String uri = dev.getAddress()+prefix;
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/xml");
		connection.setConnectTimeout(  context.getTimeout());
		connection.setReadTimeout( context.getTimeout());
		//		IoUtils.copy(connection.getInputStream(), System.out);
		JAXBContext jc = JAXBContext.newInstance(Response.class);
		InputStream xml = connection.getInputStream();
		Response response = (Response) jc.createUnmarshaller().unmarshal(xml);
		buffer = createArrayFromResponse(buffer, response,dev);
		connection.disconnect();
		return buffer;
	}
	
	public ArrayList<Param> createArrayFromResponse(ArrayList<Param> buffer,Response response,DeviceHwSw dev){
		for(ParamHwSw par : dev.getRequestParam()){
			switch(par.getType()){
			case "led0":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed0());
				break;
			case "led1":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed1());
				break;
			case "led2":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed2());
				break;
			case "led3":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed3());
				break;
			case "led4":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed4());
				break;
			case "led5":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed5());
				break;
			case "led6":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed6());
				break;
			case "led7":
				 setParamValueByTagName( buffer,par.getTag(),response.getLed7());
				break;
			case "pot0":
				 setParamValueByTagName( buffer,par.getTag(),response.getPot0());
				 break;
			case "pot1":
				 setParamValueByTagName( buffer,par.getTag(),response.getPot1());
				break;
			case "pot2":
				 setParamValueByTagName( buffer,par.getTag(),response.getPot2());
				break;
			case "pot3":
				 setParamValueByTagName( buffer,par.getTag(),response.getPot3());
				break;
			case "btn4":
				 setParamValueByTagName( buffer,par.getTag(),response.getBtn4());
				break;
			case "btn5":
				 setParamValueByTagName( buffer,par.getTag(),response.getBtn5());
				break;
			case "btn6":
				 setParamValueByTagName( buffer,par.getTag(),response.getBtn6());
				break;
			case "btn7":
				 setParamValueByTagName( buffer,par.getTag(),response.getBtn7());
				break;
			}
		}
		return buffer;
	}
	
	public void setParamValueByTagName(ArrayList<Param> buffer,String name,Object value){
		for(Param par : buffer){
			if(par.getTag().contentEquals(name)){
				par.setValue(value);
				par.setQuality(180);
			}
		}
	}

}

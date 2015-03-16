package com.brutus.client.modbus;
import com.brutus.shared.ManifestCreator;

public class MainManifest {

	/**
	 * @throws Exception 
	 */

	public static void main(String[] args) throws Exception {//MUST FIX LIBRARY SERIAL IMPORT
		ManifestCreator man = new ManifestCreator();
		man.setAuthor("raelix");
		man.setPackages(BrutusModbusClientStarter.class.getCanonicalName());
		man.setType("Client");
		man.setVersion("1.0.0");
		man.generateManifest();
//		BrutusModbusDriver dr = new BrutusModbusDriver();
//		dr.start();
//		for(int i = 0 ; i < 5; i++){//read test
//			Thread.sleep(500);
//			ArrayList<Param> buffer = new ArrayList<Param>();
//			buffer.add(new Param("david0_param1"));
//			buffer.add(new Param("david0_param2"));
//			buffer.add(new Param("david0_param3"));
//			buffer.add(new Param("david0_param4"));
//			buffer.add(new Param("david0_param5"));
//			buffer.add(new Param("david2_param1"));
//			buffer  = dr.OnParamsRequest(buffer);
//		}
//		for(int i = 0 ; i < 5; i++){//write test
//			Thread.sleep(500);
//			ArrayList<Param> buffers = new ArrayList<Param>();
//			buffers.add(new Param("david0_param3",i % 2 == 0 ? 1 : 0));
//			buffers.add(new Param("david0_param4",i % 2 == 0 ? 0 : 1));
//			buffers.add(new Param("david0_param5",i % 2 == 0 ? 1 : 0));
//			dr.setValues(buffers);
//		}
	}

}

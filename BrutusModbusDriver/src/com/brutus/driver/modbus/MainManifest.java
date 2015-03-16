package com.brutus.driver.modbus;
import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import com.brutus.driver.modbus.shared.BrutusModbusDriver;
import com.brutus.shared.ManifestCreator;

public class MainManifest {

	/**
	 * @throws Exception 
	 */
	public static class MySchemaOutputResolver extends SchemaOutputResolver {

	    public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
	        File file = new File(suggestedFileName);
	        StreamResult result = new StreamResult(file);
	        result.setSystemId(file.toURI().toURL().toString());
	        return result;
	    }

	}
	public static void main(String[] args) throws Exception {//MUST FIX LIBRARY SERIAL IMPORT
		
		JAXBContext jaxbContext = JAXBContext.newInstance(BrutusModbusDriver.class);
		SchemaOutputResolver sor = new MySchemaOutputResolver();
		jaxbContext.generateSchema(sor);

		
		ManifestCreator man = new ManifestCreator();
		man.setAuthor("raelix");
		man.setPackages(BrutusModbusDriverStarter.class.getCanonicalName());
		man.setType("Server");
		man.setVersion("1.0.0");
		man.generateManifest();
//		BrutusModbusDriver dr = new BrutusModbusDriver();
//		dr.start();
//		for(int i = 0 ; i < 1; i++){//read test
//			Thread.sleep(500);
//			ArrayList<Param> buffer = new ArrayList<Param>();
//			buffer.add(new Param("david0_param1"));
//			buffer.add(new Param("david0_param2"));
////			buffer.add(new Param("david0_param3"));
////			buffer.add(new Param("david0_param4"));
////			buffer.add(new Param("david0_param5"));
////			buffer.add(new Param("david2_param1"));
//			buffer  = dr.OnParamsRequest(buffer);
//		}
//		for(int i = 0 ; i < 5; i++){//write test
//			System.gc();
//			ArrayList<Param> buffers = new ArrayList<Param>();
//			Thread.sleep(100);
//			buffers.add(new Param("david0_param3",i % 2 == 0 ? 1 : 0));
//			buffers.add(new Param("david0_param4",i % 2 == 0 ? 0 : 1));
//			buffers.add(new Param("david0_param5",i % 2 == 0 ? 1 : 0));
//			dr.setValues(buffers);
//		}
	}

}

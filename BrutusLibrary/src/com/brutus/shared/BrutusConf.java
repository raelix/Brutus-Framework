package com.brutus.shared;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.brutus.base.Brutus;
import com.brutus.base.Device;
import com.brutus.base.Param;
import com.brutus.base.Plugin;

@SuppressWarnings("rawtypes")
public class BrutusConf {
	public static Brutus instance;
	public static final String SharedName = "Shared";
	public static final String brutusPluginFloder = "./"+BrutusConf.SharedName+"/";
	public static final String pluginDirectory = "./plugins/";
	public static final String fileName = "./brutoConf.xml";
	public static final String dbPath = "./drivers/database";
	public static final String pluginChildName = "plugin";
	public static final String pluginAttributeNameInTree = "name";
	public static List<Class> classe = new ArrayList<Class>();
	public static ArrayList<Object> instances = new ArrayList<Object>();
	static HashMap<String, Param> containerParameters = new HashMap<String, Param>();

	public static synchronized Brutus getInstance() {
		if(instance == null){
			try {
				instance = BrutusConf.loadFile();
			} catch (Exception e) {
				System.err.println("Error reading from file!");
				e.printStackTrace();
			}
		}
		return instance;
	}

	public static Brutus loadFile() throws Exception{
		if(!new File(fileName).exists()){
//			System.out.println("scrivi si per proseguire senza file");
//		boolean iWantContinueIwouldUseRestFul = false;
//		InputStreamReader reader = new InputStreamReader (System.in);
//		BufferedReader myInput = new BufferedReader (reader);
//		String str= new String();
//		try {
//		str = myInput.readLine(); } catch (IOException e) {
//		System.out.println ("Si è verificato un errore: " + e);
//		System.exit(-1); }
//		System.out.println ("Hai scritto: " +str);  
		while( !new File(fileName).exists()) {
			System.out.println("waiting configuration file");
			Thread.sleep(2000);
		}
		}
		File file = new File(fileName);
		JAXBContext jaxbContext;
		if(classe.indexOf(Brutus.class) == -1) classe.add(Brutus.class);
		Class[] registersValue = new Class[classe .size()];
		registersValue =classe .toArray(registersValue);
		jaxbContext = JAXBContext.newInstance(registersValue);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		Brutus bruto = (Brutus) jaxbUnmarshaller.unmarshal(file);
		for(Param par : bruto.getCore().getParam())
			containerParameters.put(par.getTag(), par);
		return bruto;
	}

	public static void writeFile() throws JAXBException{
		File file = new File(fileName);
		JAXBContext jaxbContext;
		if(classe.indexOf(Brutus.class) == -1)
			classe.add(Brutus.class);
		Class[] registersValue = new Class[classe .size()];
		registersValue =classe .toArray(registersValue);
		jaxbContext = JAXBContext.newInstance(registersValue);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(getInstance(), file);
	}



//	public static void saveHash4PluginAndParam(Class classes, String name) throws Exception{
//		File file = new File(fileName);
//		XMLInputFactory xif = XMLInputFactory.newFactory();
//		StreamSource xml = new StreamSource(file);
//		boolean enableAtRightPlugin = false;
//		XMLStreamReader xsr = xif.createXMLStreamReader(xml);
//		HashMap<String, Param> paramName = new HashMap<String, Param>();
//		while(xsr.hasNext()) {
//			if (xsr.getEventType() == XMLStreamReader.START_ELEMENT) {
//				if(xsr.getLocalName().toLowerCase().contentEquals(pluginChildName)){
//					String pluginName = xsr.getAttributeValue("" , pluginAttributeNameInTree);
//					if(pluginName.contentEquals(name)){
//						xsr.next();
//						enableAtRightPlugin = true;
//						continue;
//					}
//				}
//				if(xsr.getLocalName().toLowerCase().contentEquals("param") && enableAtRightPlugin){
//					paramName.put(xsr.getAttributeValue("" , "tag"),containerParameters.get(xsr.getAttributeValue("" , "tag")));
//				}
//			}
//			if (xsr.getEventType() == XMLStreamReader.END_ELEMENT & enableAtRightPlugin) {
//				if(xsr.getLocalName().toLowerCase().contentEquals("plugin")){
//					break;
//				}
//			}
//			xsr.next();
//		}
//		map.put(name, paramName);
//	}

	public static Device getDeviceByName(String name) {
		ArrayList<Device> dev = BrutusConf.getInstance().getCore().getDevice();
		for(Device temp : dev)
			if(temp.getTag().contentEquals(name))
				return temp;
		return null;
	}

	public static Param getParamByName(String name) {
		//		ArrayList<Param> dev = BrutusConf.getInstance().getCore().getParam();
		//		for(Param temp : dev)
		//			if(temp.getTag().contentEquals(name))
		//				return temp;
		return containerParameters.get(name);
	}


	public static int getParamIndexByName(String name) {
		ArrayList<Param> dev = BrutusConf.getInstance().getCore().getParam();
		for(Param temp : dev)
			if(temp.getTag().contentEquals(name))
				return BrutusConf.getInstance().getCore().getParam().indexOf(temp);
		return -1;
	}

	public static int getTotalDevicesCount() {
		return BrutusConf.getInstance().getCore().getDevice().size();
	}

	public static int getTotalParamsCount() {
		return BrutusConf.getInstance().getCore().getParam().size();
	}

	public static Object loadPluginClass(Class classes, String name) throws Exception{
		File file = new File(fileName);
		XMLInputFactory xif = XMLInputFactory.newFactory();
		StreamSource xml = new StreamSource(file);
		XMLStreamReader xsr = xif.createXMLStreamReader(xml);
		while(xsr.hasNext()) {
			if (xsr.getEventType() == XMLStreamReader.START_ELEMENT) {
				if(xsr.getLocalName().toLowerCase().contentEquals(pluginChildName)){
					String pluginName = xsr.getAttributeValue("" , pluginAttributeNameInTree);
					if(pluginName.contentEquals(name))
						break;
				}
			}
			xsr.next();
		}
		JAXBContext jc = JAXBContext.newInstance(classes);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		Object toType = unmarshaller.unmarshal(xsr, classes).getValue();
		classe.add(classes);
		instances.add((Plugin) toType);
//		saveHash4PluginAndParam(classes,name);
		SchemaOutputResolver sor = new MySchemaOutputResolver(name);
		jc.generateSchema(sor);
		return toType;
	}

	public static class MySchemaOutputResolver extends SchemaOutputResolver {
		private String pluginName;

		public MySchemaOutputResolver(String pluginName) {
			this.pluginName = pluginName;
		}

		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			File file = new File("plugins/"+pluginName+".xsd");
			StreamResult result = new StreamResult(file);
			result.setSystemId(file.toURI().toURL().toString());
			return result;
		}

	}

	public static int getPluginTimeout(String name) throws Exception{
		File file = new File(fileName);
		XMLInputFactory xif = XMLInputFactory.newFactory();
		StreamSource xml = new StreamSource(file);
		XMLStreamReader xsr = xif.createXMLStreamReader(xml);
		while(xsr.hasNext()) {
			if (xsr.getEventType() == XMLStreamReader.START_ELEMENT) {
				if(xsr.getLocalName().toLowerCase().contentEquals(pluginChildName)){
					String pluginName = xsr.getAttributeValue("" , pluginAttributeNameInTree);
					if(pluginName.contentEquals(name)){
						String timeout = xsr.getAttributeValue("" , "timeout");
						return Integer.parseInt(timeout != null ? timeout : "0");
					}
				}
			}
			xsr.next();
		}

		return 0;
	}

	public static Object loadClassByName(Class<?> classes,String nameClass, String nameContent) throws JAXBException, XMLStreamException{
		File file = new File(fileName);
		XMLInputFactory xif = XMLInputFactory.newFactory();
		StreamSource xml = new StreamSource(file);
		XMLStreamReader xsr = xif.createXMLStreamReader(xml);
		while(xsr.hasNext()) {
			if (xsr.getEventType() == XMLStreamReader.START_ELEMENT) {
				if(xsr.getLocalName().contentEquals(nameClass)){
					String pluginName = xsr.getAttributeValue("" , pluginAttributeNameInTree);
					if(pluginName.contentEquals(nameContent)){
						break;
					}
				}
			}
			xsr.next();
		}
		JAXBContext jc = JAXBContext.newInstance(classes);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		Object toType = unmarshaller.unmarshal(xsr, classes).getValue();
		return toType;
	}



}

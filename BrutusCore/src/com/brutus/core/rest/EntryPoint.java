package com.brutus.core.rest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import com.brutus.base.Brutus;
import com.brutus.base.Param;
import com.brutus.base.ParamExp;
import com.brutus.base.ParamHistory;
import com.brutus.base.Parameters;
import com.brutus.base.Plugins;
import com.brutus.core.BrutusCore;
import com.brutus.core.storage.MapAdapter;
import com.brutus.shared.BrutusConf;
import com.google.gson.Gson;

@Path("/")
public class EntryPoint {
	/****************************************************************************************************************************
	http://localhost:8080/brutus/log					LOG EDITING         in working...90%
	 * @throws ParseException 
	 ****************************************************************************************************************************/	

	@GET
	@Path("/energy")//http://192.168.0.2:8080/brutus/energy?tag=bonjour&from=25-02-2015&to=26-02-2015      ??? &type=h/d/m/y division by hour,day,month,year
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})//?
	public ArrayList<ParamHistory> energyGet( @Context UriInfo uriInfo ) throws JAXBException, ParseException {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		List<String> from =  uriInfo.getQueryParameters().get("from");
		List<String> to =  uriInfo.getQueryParameters().get("to");
		if(specificParam != null && from != null && to != null){
				Param param = null;
				if(MapAdapter.getInstance().getParam(specificParam.get(0)) != null)
					param = MapAdapter.getInstance().getParam(specificParam.get(0));
				else
					param = BrutusCore.bufferKey.get(specificParam.get(0));
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date fromDate = formatter.parse(from.get(0));
				Date toDate = formatter.parse(to.get(0));
				System.out.println(fromDate.getTime()+" "+toDate.getTime());
				ArrayList<ParamHistory> requestElement = new ArrayList<ParamHistory>();
				double totalEnergy = 0;
				double totalPrice = 0;
				for(int i = 0 ; i < param.getHistory().size() ; i++ ){
					ParamHistory hst = param.getHistory().get(i);
					System.out.println(hst.getDate());
					if(hst.getDate() > fromDate.getTime() && hst.getDate() < toDate.getTime()){
						totalEnergy += hst.getEnergyComputed();
						totalPrice+=hst.getPrice();
						requestElement.add(hst);
					}
				}
//				if(requestElement.size() > 0){
//				requestElement.get(requestElement.size() - 1).setEnergyComputed(totalEnergy);
//				requestElement.get(requestElement.size() - 1).setPrice(totalPrice);
//				}
//				if(costPerCent != null)
//					param.setCostPerCent(Integer.parseInt(costPerCent.get(0)));
				

			return requestElement;
		}
		
		return null;
	} 
	
	@GET
	@Path("/log")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response logGet( @Context UriInfo uriInfo ) throws JAXBException {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		List<String> logEnable =  uriInfo.getQueryParameters().get("logEnable");
		List<String> polling =  uriInfo.getQueryParameters().get("polling");
		List<String> energyCalculate =  uriInfo.getQueryParameters().get("energyCalculate");

		List<String> costPerCent =  uriInfo.getQueryParameters().get("costPerCent");
		
		if(specificParam != null ){
			for(int k = 0 ; k < specificParam.size(); k++){
				Param param = null;
				if(MapAdapter.getInstance().getParam(specificParam.get(k)) != null)
					param = MapAdapter.getInstance().getParam(specificParam.get(k));
				else
					param = BrutusCore.bufferKey.get(specificParam.get(k));

				if(logEnable != null)
					param.setLogEnable(Boolean.parseBoolean(logEnable.get(k)));
				if(polling != null)
					param.setPolling(Integer.parseInt(polling.get(k)));
				if(energyCalculate != null )
					param.setEnergyCalculate(Boolean.parseBoolean(energyCalculate.get(k)));
				if(costPerCent != null)
					param.setCostPerCent(Integer.parseInt(costPerCent.get(k)));
				
				MapAdapter.getInstance().putParam(param.getTag(), param);
			}

			return Response.status(new GenericStatus("Log Set!", Status.ACCEPTED).getStatus()).build();
		}
		return Response.status(new GenericStatus("Cant set Log!", Status.BAD_REQUEST).getStatus()).build();
	} 

	@POST
	@Path("/log")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	public Parameters setLog(Parameters param) throws JAXBException{
		String tag = param.getParam().get(0).getTag();
		@SuppressWarnings("unused")
		Param par;

		if(MapAdapter.getInstance().getParam(tag) != null)
			par = MapAdapter.getInstance().getParam(tag);
		else
			par = BrutusCore.bufferKey.get(tag);

		MapAdapter.getInstance().putParam(param.getParam().get(0).getTag(), param.getParam().get(0));
		return new Parameters(MapAdapter.getInstance().getParam(tag));

	}

	@GET
	@Path("/resetlog")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response resetLogPath(@Context UriInfo uriInfo ) throws JAXBException {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		if(specificParam != null && specificParam.size() > 0){
			for(int k = 0 ; k < specificParam.size(); k++){
				Param par;

				if(MapAdapter.getInstance().getParam(specificParam.get(k)) != null)
					par = MapAdapter.getInstance().getParam(specificParam.get(k));
				else
					par = BrutusCore.bufferKey.get(specificParam.get(k));

				par.getHistory().clear();//reset all history alarm, wrong?
				MapAdapter.getInstance().putParam(par.getTag(), par);
			}
		}
		return Response.status(new GenericStatus("Reset log success!", Status.ACCEPTED).getStatus()).build();
	}  
	@GET
	@Path("/resetlog/{tag}")
	@Produces(MediaType.TEXT_HTML)
	public String resetLog(@PathParam("tag") String tag) throws JAXBException {
		Param par;

		if(MapAdapter.getInstance().getParam(tag) != null)
			par = MapAdapter.getInstance().getParam(tag);
		else
			par = BrutusCore.bufferKey.get(tag);

		par.getHistory().clear();//reset all history alarm, wrong?
		MapAdapter.getInstance().putParam(par.getTag(), par);
		return tag+" log reset success!";
	}  

	/****************************************************************************************************************************
http://localhost:8080/brutus/alarm					ALARM EDITING         in working...90%
	 ****************************************************************************************************************************/	

	@GET
	@Path("/alarm")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response alarmGet( @Context UriInfo uriInfo ) throws JAXBException {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		List<String> minAlarm =  uriInfo.getQueryParameters().get("minAlarm");
		List<String> maxAlarm =  uriInfo.getQueryParameters().get("maxAlarm");
		List<String> repeat =  uriInfo.getQueryParameters().get("repeat");
		List<String> enable =  uriInfo.getQueryParameters().get("enable");
		if(specificParam != null && minAlarm != null && maxAlarm != null && enable != null){
			for(int k = 0 ; k < specificParam.size(); k++){
				Param param = null;
				if(MapAdapter.getInstance().getParam(specificParam.get(k)) != null)
					param = MapAdapter.getInstance().getParam(specificParam.get(k));
				else
					param = BrutusCore.bufferKey.get(specificParam.get(k));

				if(minAlarm != null && maxAlarm != null &&  enable != null ){
					param.setMinAlarm(Integer.parseInt(minAlarm.get(k)));
					param.setMaxAlarm(Integer.parseInt(maxAlarm.get(k)));
				}
				if(repeat != null)
					param.setRepeat(Integer.parseInt(repeat.get(k)));

				param.setAlarmEnable(Boolean.parseBoolean(enable.get(k)));
				param.setRetentant(Boolean.parseBoolean(enable.get(k)));
				MapAdapter.getInstance().putParam(param.getTag(), param);
			}

			return Response.status(new GenericStatus("Alarm Set!", Status.ACCEPTED).getStatus()).build();
		}
		return Response.status(new GenericStatus("Cant set Alarm!", Status.BAD_REQUEST).getStatus()).build();
	} 

	@POST
	@Path("/alarm")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	public Parameters setAlarm(Parameters param) throws JAXBException{
		String tag = param.getParam().get(0).getTag();
		@SuppressWarnings("unused")
		Param par;

		if(MapAdapter.getInstance().getParam(tag) != null)
			par = MapAdapter.getInstance().getParam(tag);
		else
			par = BrutusCore.bufferKey.get(tag);

		MapAdapter.getInstance().putParam(param.getParam().get(0).getTag(), param.getParam().get(0));
		return new Parameters(MapAdapter.getInstance().getParam(tag));

	}

	@GET
	@Path("/resetalarm")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response resetAlarmPath(@Context UriInfo uriInfo ) throws JAXBException {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		if(specificParam != null && specificParam.size() > 0){
			for(int k = 0 ; k < specificParam.size(); k++){
				Param par;

				if(MapAdapter.getInstance().getParam(specificParam.get(k)) != null)
					par = MapAdapter.getInstance().getParam(specificParam.get(k));
				else
					par = BrutusCore.bufferKey.get(specificParam.get(k));

				par.setRetriesTime(0);
				par.setAlarming(false);
				par.getAlarm().clear();//reset all history alarm, wrong?
				MapAdapter.getInstance().putParam(par.getTag(), par);
			}
		}
		return Response.status(new GenericStatus("Reset alarm success!", Status.ACCEPTED).getStatus()).build();
	}  
	@GET
	@Path("/resetalarm/{tag}")
	@Produces(MediaType.TEXT_HTML)
	public String resetAlarm(@PathParam("tag") String tag) throws JAXBException {
		Param par;

		if(MapAdapter.getInstance().getParam(tag) != null)
			par = MapAdapter.getInstance().getParam(tag);
		else
			par = BrutusCore.bufferKey.get(tag);

		par.setRetriesTime(0);
		par.getAlarm().clear();//reset all history alarm, wrong?
		MapAdapter.getInstance().putParam(par.getTag(), par);
		return tag+" alarm reset success!";
	}  

	/****************************************************************************************************************************
http://localhost:8080/brutus/alarm					ID KEY EDIT        force save file!
	 ****************************************************************************************************************************/	

	@GET
	@Path("/idkey/{idkey}")
	@Produces(MediaType.TEXT_HTML)
	public String pluginXml(@PathParam("idkey") String idkey) throws JAXBException {
		if(idkey != null && !idkey.contentEquals("") && idkey.length() > 2){
			BrutusConf.getInstance().getCore().setIdKey(idkey);
			BrutusConf.writeFile();
			System.out.println("called idkey");
			//			BrutusConf.instance = null;//force reload
			return "IdKey saved!";
		}
		else return "IdKey can't be null!";
	} 

	/****************************************************************************************************************************
http://localhost:8080/brutus/plugin					PLUGIN LIST        
	 ****************************************************************************************************************************/	

	@GET
	@Path("plugin")
	@Produces(MediaType.APPLICATION_XML)
	public Object pluginXml() throws JAXBException {
		return BrutusConf.getInstance().plugins;
	} 

	@GET
	@Path("/plugin")
	@Produces(MediaType.APPLICATION_JSON)
	public Object pluginJson() throws JAXBException {
		Plugins pl = new Plugins();
		pl.plugin = BrutusConf.instances;
		return  pl;
	} 

	/* plugin schema by name */
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/plugin/{id}")
	public String getUserById(@PathParam("id") String id) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(new File(BrutusConf.pluginDirectory+id+".xsd")));
		String line;
		StringBuilder sb = new StringBuilder();

		while((line=br.readLine())!= null){
			sb.append(line.trim());
		}
		return sb.toString();

	}

	/****************************************************************************************************************************
http://localhost:8080/brutus/plugin					CONFIGURATION FILE        
	 ****************************************************************************************************************************/

	@GET
	@Path("/configuration")
	@Produces( MediaType.APPLICATION_XML)
	public Object configurationXml() throws JAXBException  {
		Brutus br = BrutusConf.getInstance();
		return br;
	} 

	@GET
	@Path("/configuration")
	@Produces(MediaType.APPLICATION_JSON)
	public Object configurationJson() throws JAXBException  {
		Brutus prova = new Brutus();
		prova.setCore(BrutusConf.getInstance().getCore());
		Plugins pl = new Plugins();
		pl.plugin = BrutusConf.instances;
		prova.plugins = pl;
		return new Gson().toJson(prova);
	}


	/****************************************************************************************************************************
http://localhost:8080/brutus/plugin					REBOOT       						require auth!
	 ****************************************************************************************************************************/

	@GET
	@Path("/reboot")
	@Produces({MediaType.TEXT_HTML})
	public Response reboot( ) {
		BrutusRestClientStarter.Core.reboot();
		return Response.ok("In Reboot...!").build();
	} 

	/****************************************************************************************************************************
		//http://localhost:8080/brutus/read							READ
	 ****************************************************************************************************************************/

	//http://localhost:8080/brutus/read
	//http://localhost:8080/brutus/read?tag=test&tag=taxt
	@GET
	@Path("/read")
	@Produces(MediaType.APPLICATION_XML )
	public Object readGet( @Context UriInfo uriInfo ,@Context HttpServletRequest req) {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		ArrayList<Param> requestCore = new ArrayList<Param>();
		System.out.println("new rest request from: "+req.getRemoteAddr());
		if(specificParam != null){
			for(String tagName : specificParam){
				requestCore.add(new Param(tagName));
			}
			requestCore = BrutusRestClientStarter.Core.getValues(requestCore);
		}
		else
			requestCore = BrutusRestClientStarter.Core.getValues(BrutusCore.buffer);
		return new Parameters(requestCore);
	} 



	@GET
	@Path("/read")
	@Produces( MediaType.APPLICATION_JSON)
	public String readGets( @Context UriInfo uriInfo ,@Context HttpServletRequest req) {
		Gson gson = new Gson();
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		ArrayList<Param> requestCore = new ArrayList<Param>();
		System.out.println("New RestFul request from ip: "+req.getRemoteAddr());
		if(specificParam != null){
			for(String tagName : specificParam){
				requestCore.add(new Param(tagName));
			}
			requestCore = BrutusRestClientStarter.Core.getValues(requestCore);
		}
		else
			requestCore = BrutusRestClientStarter.Core.getValues(BrutusCore.buffer);
		return gson.toJson(new Parameters(requestCore));
	} 

	//http://localhost:8080/brutus/read
	@POST
	@Path("/read")
	@Consumes({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON  })
	public Parameters readPost(Parameters params){
		ArrayList<Param> requestCore = new ArrayList<Param>();
		if(params != null && params.getParam() != null){
			if(params.getParam().size() > 0){
				for(Param param : params.getParam()){
					requestCore.add(new Param(param.getTag()));
				}
				requestCore = BrutusRestClientStarter.Core.getValues(requestCore);
			}
		}
		else
			requestCore = BrutusRestClientStarter.Core.getValues(BrutusCore.buffer);
		return new Parameters(requestCore);
	}

	@Path("/read")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Parameters readFormParam(@FormParam("param") List<String> param) {
		ArrayList<Param> requestCore = new ArrayList<Param>();
		if(param != null){
			for(String tagName : param){
				requestCore.add(new Param(tagName));
			}
			requestCore = BrutusRestClientStarter.Core.getValues(requestCore);
		}
		else
			requestCore = BrutusRestClientStarter.Core.getValues(BrutusCore.buffer);
		return new Parameters(requestCore);
	}

	/****************************************************************************************************************************
                    http://localhost:8080/brutus/write						WRITE
	 ****************************************************************************************************************************/	

	//http://localhost:8080/brutus/write
	//http://localhost:8080/brutus/write?tag=test&value=12&tag=taxt&value=33
	@GET
	@Path("/write")
	@Produces(MediaType.TEXT_HTML)
	public Response writeGet( @Context UriInfo uriInfo ) {
		List<String> specificParam =  uriInfo.getQueryParameters().get("tag");
		List<String> specificWrite =  uriInfo.getQueryParameters().get("value");
		ArrayList<Param> requestCore = new ArrayList<Param>();
		if(specificParam != null && specificWrite != null){
			for(int i = 0 ; i < specificParam.size() ; i++){
				String tagName = specificParam.get(i);
				int value = Integer.valueOf(specificWrite.get(i));
				requestCore.add(new Param(tagName,value));
			}
			BrutusRestClientStarter.Core.setValues(requestCore);
		}
		else
			return Response.ok("Errore! sintassi es. tag=nome&value=12").build();

		return Response.ok("Valori Settati!").build();
	} 

	//http://localhost:8080/brutus/write
	@POST
	@Path("/write")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response writePost( Parameters params ) {
		ArrayList<Param> requestCore = new ArrayList<Param>();
		if(params != null && params.getParam() != null){
			if(params.getParam().size() > 0){
				for(ParamExp param : params.getParam()){
					requestCore.add(param);
				}
				BrutusRestClientStarter.Core.setValues(requestCore);
			}
		}
		else return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(params).build();
		return Response.ok(params).build();
	} 

	/****************************************************************************************************************************
http://localhost:8080/brutus/write						PARAM EDITING         in working...50%
	 ****************************************************************************************************************************/	

	//http://localhost:8080/brutus/addparam
	@POST
	@Path("/addparam")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response addParamPost( Parameters params ) {
		if(params != null && params.getParam() != null){
			if(params.getParam().size() > 0){
				for(ParamExp param : params.getParam()){
					if(BrutusConf.getParamByName(param.getTag()) == null){
						BrutusConf.getInstance().getCore().getParam().add(param);
						try {
							BrutusConf.writeFile();
						} catch (JAXBException e) {
							return Response.status(new GenericStatus("Cannot save changes in configuration!", Status.CONFLICT).getStatus()).build();
						}
					}
					else 
						return Response.status(new GenericStatus("Param exist!", Status.CONFLICT).getStatus()).build();
				}
			}
		}
		else 
			return Response.status(new GenericStatus("Param exist!", Status.BAD_REQUEST).getStatus()).build();
		return Response.status(new GenericStatus("Param added!", Status.ACCEPTED).getStatus()).entity(params).build();
	} 

	//http://localhost:8080/brutus/addparam
	@POST
	@Path("/remparam")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response removeParamPost( Parameters params ) {
		if(params != null && params.getParam() != null){
			if(params.getParam().size() > 0){
				for(ParamExp param : params.getParam()){
					if(BrutusConf.getParamByName(param.getTag()) != null){
						BrutusConf.getInstance().getCore().getParam().remove(BrutusConf.getParamByName(param.getTag()));
						try {
							BrutusConf.writeFile();
						} catch (JAXBException e) {
							return Response.status(new GenericStatus("Cannot save changes in configuration!", Status.CONFLICT).getStatus()).build();
						}
					}
					else 
						return Response.status(new GenericStatus("Param not found in configuration!", Status.CONFLICT).getStatus()).build();
				}
			}
		}
		else 
			return Response.status(new GenericStatus("Param not found in configuration!", Status.BAD_REQUEST).getStatus()).build();
		return Response.status(new GenericStatus("Param removed!", Status.ACCEPTED).getStatus()).build();
	} 

	//http://localhost:8080/brutus/addparam
	@POST
	@Path("/editparam")
	@Produces({MediaType.APPLICATION_XML , MediaType.APPLICATION_JSON})
	public Response editParamPost( Parameters params ) {
		if(params != null && params.getParam() != null){
			if(params.getParam().size() > 0){
				for(ParamExp param : params.getParam()){
					if(BrutusConf.getParamByName(param.getTag()) != null){
						BrutusConf.getInstance().getCore().getParam().remove(BrutusConf.getParamByName(param.getTag()));
						BrutusConf.getInstance().getCore().getParam().add(param);
						try {
							BrutusConf.writeFile();
						} catch (JAXBException e) {
							return Response.status(new GenericStatus("Cannot save changes in configuration!", Status.CONFLICT).getStatus()).build();
						}
					}
					else 
						return Response.status(new GenericStatus("Param not found in configuration!", Status.CONFLICT).getStatus()).build();
				}
			}
		}
		else 
			return Response.status(new GenericStatus("Param not found in configuration!", Status.BAD_REQUEST).getStatus()).build();
		return Response.status(new GenericStatus("Param removed!", Status.ACCEPTED).getStatus()).build();
	} 
}
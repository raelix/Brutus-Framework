package com.brutus.andbrutus.task;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//not used
public class RestRequestAsyncTaskInDeploy extends AsyncTask<String, BufferedReader, BufferedReader> implements Rest{
	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onPostExecute(BufferedReader param) {

	}

	@SuppressWarnings("rawtypes")
	@Override
	protected BufferedReader doInBackground(String... parames) {
		BufferedReader reader = null;
		try {
			Thread.currentThread().setName("RestRequestAsyncTask");
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
			HttpGet uri = new HttpGet(prefix+Preferences.getString("hostext")+parames[0].replaceAll(" ", "%20"));     
			//			System.out.println("result "+prefix+Preferences.getString("hostext")+parames[0]);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpConnectionParams.setSoTimeout(httpParameters, 3000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			uri.setHeader("Accept", "application/json");
			HttpResponse resp = client.execute(uri);
			StatusLine status = resp.getStatusLine();
			if (status.getStatusCode() != 200) {
				Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
			} 
			InputStream json = resp.getEntity().getContent();
			reader = new BufferedReader(new InputStreamReader(json));
//			Gson gson = new Gson();
			JsonElement jelement = new JsonParser().parse(reader);
			//			Brutus one = gson.fromJson(reader , Brutus.class); 
			//			 System.out.println("jelement è null? "+jelement.getAsJsonObject().getAsJsonObject("plugins").getAsJsonArray("plugin").get(0).getAsJsonObject().get("name").getAsString());
			//plugin nome
			JsonArray plugin =  jelement.getAsJsonObject().getAsJsonObject("plugins").getAsJsonArray("plugin");

			for(int i = 0 ; i < plugin.size() ; i++){
				JsonObject obj = plugin.get(i).getAsJsonObject();
				System.out.println(obj.get("name").getAsString());
				JsonObject arrayPlug = jelement.getAsJsonObject().getAsJsonObject("plugins").getAsJsonArray("plugin").get(i).getAsJsonObject();
				//device name
				if(arrayPlug.get("device") != null ){
					if(arrayPlug.get("device") instanceof JsonArray){
						JsonArray arrayDev	= arrayPlug.getAsJsonArray("device");
						if(arrayDev != null){
							for(int k = 0 ; k < arrayDev.size(); k++){
								JsonObject dev = arrayDev.get(k).getAsJsonObject();
								System.out.println(" ");
								System.out.println("Device: "+dev.get("tag").getAsString());
								Iterator its = dev.entrySet().iterator();
								while(its.hasNext()){
									Object temp = its.next();
									if(!(temp.toString()).contains("param") && !(temp.toString()).contains("requestParam"))
									System.out.println(temp);
								}
								//param
								JsonArray arrayParam	= dev.getAsJsonArray("param");
								if(arrayParam != null){
									for(int f = 0 ; f < arrayParam.size(); f++){
										JsonObject param = arrayParam.get(f).getAsJsonObject();
										System.out.println(" ");
										System.out.println("Param: "+param.get("tag").getAsString());
										Iterator it = param.entrySet().iterator();
										while(it.hasNext()){
											System.out.println(it.next());
										}
									}
								}
								
							}
						}
					}
					else if (arrayPlug.get("device") instanceof JsonObject){
						JsonObject dev	= arrayPlug.getAsJsonObject("device");
						System.out.println(" ");
						System.out.println("Device: "+dev.get("tag").getAsString());
						//No param 
					}
				}
			}

			//			  JsonObject jsonObject = (JsonObject) one.plugins.plugin.get(0);
			//			  String name = jsonObject.get("plugin")       // get the 'user' JsonElement
			//                      .getAsJsonObject() // get it as a JsonObject
			//                      .get("name")       // get the nested 'name' JsonElement
			//                      .getAsString();    // get it as a String
			//System.out.println(name);
		} catch (  Exception  e) {
			e.printStackTrace();
		}
		return reader;
	}



}

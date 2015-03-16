package com.brutus.andbrutus.task;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.google.gson.Gson;
//not used
public class RestPostRequestAsyncTaskDeprecated extends AsyncTask<String, BufferedReader, BufferedReader> implements Rest,Serializable{

	private static final long serialVersionUID = -5819973673012183180L;
	Object pojo;
	Context context;
	boolean error = false;

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onPostExecute(BufferedReader param) {
		if(context != null){
		if(error){
			new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
			.setTitleText("Send error!")
			.show();
		}
		else{
			new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
			.setTitleText("Send success!")
			.show();
		}
		}
	}

	public RestPostRequestAsyncTaskDeprecated(Object pojo,Context context) {
		this.pojo = pojo;
		this.context = context;
	}

	@Override
	protected BufferedReader doInBackground(String... parames) {
		BufferedReader reader = null;
		try {

			Thread.currentThread().setName("RestPostRequestAsyncTask");
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//			System.out.println("result "+prefix+Preferences.getString("hostext")+parames[0]);
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpConnectionParams.setSoTimeout(httpParameters, 3000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			Gson gson= new Gson();
			HttpPost post = new HttpPost(prefix+Preferences.getString("hostext")+parames[0].replaceAll(" ", "%20")); 
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-type","application/json");
			StringEntity  postingString =new StringEntity(gson.toJson(pojo)); //convert to json
			post.setEntity(postingString);
			HttpResponse  response = client.execute(post);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != 200) {
				Log.d("Schema app", "HTTP error, invalid server status code: " + response.getStatusLine());  
				error = true;
				return null;
			}
			InputStream json = response.getEntity().getContent();
			reader = new BufferedReader(new InputStreamReader(json));
			error = false;
			//			Gson gson = new Gson();
			//			Brutus one = gson.fromJson(reader , Brutus.class);
			//			System.out.println("debug level: "+one.getCore().getDebugLevel());
			//			System.out.println(one.getCore().toString());
		} catch (  Exception  e) {
			e.printStackTrace();
		}
		return reader;
	}



}

package com.brutus.andbrutus.task;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.base.Parameters;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;

public class RestConfigRequestAsyncTask extends AsyncTask<String, String, String> implements Rest{
	SnackBar snack;
	Activity activity;
	SwipeRefreshLayout swipeLayout;
	Handler handler;
	Parameters emptyViewParamTag;
	boolean refreshShow = true;

	public RestConfigRequestAsyncTask(Activity activity, SwipeRefreshLayout swipeLayout,Handler handler) {
		this.activity = activity;
		this.swipeLayout = swipeLayout;
		this.handler = handler;
	}
	public RestConfigRequestAsyncTask(Activity activity, SwipeRefreshLayout swipeLayout,Handler handler,boolean refreshShow) {
		this.activity = activity;
		this.swipeLayout = swipeLayout;
		this.handler = handler;
		this.refreshShow = refreshShow;
	}

	@Override
	protected void onPreExecute() {
		if(refreshShow)
			swipeLayout.setRefreshing(true);
	}

	@Override
	protected void onPostExecute(String param) {
		Message msg = new Message();
		if(emptyViewParamTag != null){
		msg.obj = emptyViewParamTag;
		}
		else msg.obj = "error";
		handler.sendMessage(msg);
		if(refreshShow)
			swipeLayout.setRefreshing(false);
	}
	public void setRefreshShow(boolean show){
		this.refreshShow = show;
	}
	@Override
	protected String doInBackground(String... parames) {
		try {
			Thread.currentThread().setName("RestConfigRequestAsyncTask");
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
			HttpGet uri = new HttpGet(prefix+Preferences.getString("hostext")+parames[0].replaceAll(" ", "%20")); 
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			HttpConnectionParams.setSoTimeout(httpParameters, 3000);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			uri.setHeader("Accept", "application/json");
			HttpResponse resp = client.execute(uri);
			StatusLine status = resp.getStatusLine();
			if (status.getStatusCode() != 200) {
				Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
				new Exception("Error connection");
			}
			InputStream json = resp.getEntity().getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(json));
			Gson gson = new Gson();
			Parameters params = gson.fromJson(reader , Parameters.class);
			emptyViewParamTag = params;
		} catch (  Exception  e) {
			e.printStackTrace();

		}
		return "";
	}

}

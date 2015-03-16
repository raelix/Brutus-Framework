package com.brutus.andbrutus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.view.CustomNotification;
import com.brutus.base.Parameters;
import com.google.gson.Gson;

public class NotificationService extends Service implements Rest{
	public static Handler handler;
	private static final String TAG = "AndBrutus Notification Service";
	private boolean isContextRegistered;
	private Timer timerScheduler;
	NotificationBackgroundService serviceInBackground;
	public NotificationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");
		timerScheduler = new Timer();
		serviceInBackground =  new NotificationBackgroundService(this);
		timerScheduler.schedule(serviceInBackground, pollingStart, pollingRefresh) ;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		if(serviceInBackground != null)
		serviceInBackground.cancel();
		if(timerScheduler != null)
			timerScheduler.cancel();
	}


	public class NotificationBackgroundService extends TimerTask{
		Context context;

		public NotificationBackgroundService(Context context) {
			super();
			this.context = context;
		}

		@Override
		public void run(){
			Thread.currentThread().setName("NotificationBackgroundService");
			System.out.println("Nuova richiesta");
			new Preferences(getApplicationContext());
			Gson gson = new Gson();
			Message msg = new Message();
			try {
				BufferedReader reader = null ;
				HttpGet uri = new HttpGet(prefix+Preferences.getString("hostext")+read);   
				HttpParams httpParameters = new BasicHttpParams();
				DefaultHttpClient client = new DefaultHttpClient(httpParameters);
				uri.setHeader("Accept", "application/json");
				HttpResponse resp = client.execute(uri);
				StatusLine status = resp.getStatusLine();
				if (status.getStatusCode() != 200) {
					Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
				} 
				else{
					InputStream json = resp.getEntity().getContent();
					reader = new BufferedReader(new InputStreamReader(json));
				}
				if(reader != null){
					Parameters params = gson.fromJson(reader , Parameters.class); ;
					if(Preferences.getBoolean("notificationEnable")){
						if(!isContextRegistered){
							new CustomNotification(context).refreshNotification(params);
							isContextRegistered = true;
						}
						else{
							new CustomNotification(context,true).refreshNotification(params);
						}
					}
					else if(! Preferences.getBoolean("notificationEnable") && isContextRegistered){
						new CustomNotification(context).removeNotification(params);
					}
					msg.obj = params;
				}
				else{
					msg.obj = "Error Connection";	
				}
				if(handler != null)
					handler.sendMessage(msg);				
			} catch (    IOException e) {
				e.printStackTrace();
			}
		}

	}

}

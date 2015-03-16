package com.brutus.andbrutus.task;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import android.util.Log;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;

public class RestHtmlRunnable implements Runnable,Rest{
	String url;

	public RestHtmlRunnable(String url) {
		this.url = url;
	}

	@Override
	public void run() {
		try {
			Thread.currentThread().setName("RestHtmlRunnableThread");
			HttpGet uri = new HttpGet(prefix+Preferences.getString("hostext")+url.replaceAll(" ", "%20"));  
			HttpParams httpParameters = new BasicHttpParams();
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
			HttpResponse resp = client.execute(uri);
			StatusLine status = resp.getStatusLine();
			if (status.getStatusCode() != 200) {
				Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
				return;
			} 
		} catch (  Exception  e) {
			e.printStackTrace();
			return;
		}
	}
}
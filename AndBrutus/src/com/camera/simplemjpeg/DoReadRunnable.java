package com.camera.simplemjpeg;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class DoReadRunnable implements Runnable{
	MjpegView cam;
	String url;
	public DoReadRunnable(MjpegView cam,String url) {
		this.cam = cam;
		this.url = url;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("DoRead");
		HttpResponse res = null;
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
		HttpParams httpParams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5*1000);
		System.out.println("sendinggg");
		try {
			res = httpclient.execute(new HttpGet(URI.create(url)));
			if(res.getStatusLine().getStatusCode()==401){
				return ;
			}  
			if(cam != null ){
				if(cam!=null){
					if(cam.isStreaming()){
						cam.stopPlayback();
					}
				}
				cam.setSource(new MjpegInputStream(res.getEntity().getContent()));
				//        		result.setSkip(1);
				cam.setDisplayMode(MjpegView.SIZE_BEST_FIT);
				cam.showFps(true);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

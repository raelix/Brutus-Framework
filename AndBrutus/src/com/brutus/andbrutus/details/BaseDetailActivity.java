package com.brutus.andbrutus.details;
import java.io.BufferedReader;
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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brutus.andbrutus.ParamBinderItemActivity;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.utils.SystemBarTintManager;
import com.brutus.andbrutus.utils.Utils;
import com.brutus.andbrutus.view.CircleButton;
import com.brutus.andbrutus.view.ObservableScrollView;
import com.brutus.andbrutus.view.ObservableScrollView.OnScrollListener;
import com.brutus.andbrutus.view.RevealTransition;
import com.brutus.base.ParamExp;
import com.brutus.base.Parameters;
import com.camera.simplemjpeg.DoRead;
import com.camera.simplemjpeg.MjpegView;
import com.google.gson.Gson;

abstract public class BaseDetailActivity extends ActionBarActivity implements Rest, OnScrollListener,OnClickListener {

	Toolbar toolbar;
	ParamExp param;
	View customView;
	TextView title;
	TextView desc;
	ImageView statusImg;
	float limitMax;
	String unitMeasure;
	Timer timer;
	RelativeLayout cameraLayout;
	RelativeLayout logLayout;
	RelativeLayout alarmLayout;
	ObservableScrollView descriptionLayoutScroll;
	CircleButton alarm;
	CircleButton log;
	CircleButton camera;
	CircleButton setting;
	boolean enable = true;
	boolean alarmVisible;
	boolean suspending;
	boolean logVisible;
	boolean cameraVisible;
	MjpegView cameraView;
	abstract View getCustomView();
	abstract public void update(int result);

	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		super.onCreate(savedInstanceState);
		//Must call setContentView() and init();
	}

	public void init(){
		timer = new Timer();
		title = (TextView) findViewById(R.id.nameDef);
		desc = (TextView) findViewById(R.id.nameDesc);
		statusImg = (ImageView) findViewById(R.id.sensor);
		cameraLayout = (RelativeLayout) findViewById(R.id.camLayoutMjpeg);//listener
		//		logLayout = (RelativeLayout) findViewById(R.id.camLayoutMjpeg);//move
		//		alarmLayout = (RelativeLayout) findViewById(R.id.camLayoutMjpeg);
		descriptionLayoutScroll = (ObservableScrollView) findViewById(R.id.descriptionLayoutScroll);
		alarm = (CircleButton) findViewById(R.id.alarm);
		log = (CircleButton) findViewById(R.id.log);
		cameraView = (MjpegView) findViewById(R.id.cameraMjpeg);
		camera = (CircleButton) findViewById(R.id.camera);
		setting = (CircleButton) findViewById(R.id.setting);
		param = (ParamExp) getIntent().getSerializableExtra("param");
		limitMax = Preferences.getFloat(param.getTag()+_limitMax);
		unitMeasure = Preferences.getString(param.getTag()+_unit);
		title.setText(param.getTag());
		descriptionLayoutScroll.setOnScrollListener(this);
		alarm.setOnClickListener(this);
		log.setOnClickListener(this);
		setting.setOnClickListener(this);
		camera.setOnClickListener(this);
		customView = getCustomView();
		initTransition();
		initToolBar();
		initTimer();
		//first time update all data in view
		updateValue(Utils.getIntFromObject(param.getValue()));
		if(Preferences.getBoolean(param.getTag()+_cameraEnable)){
			new DoRead(cameraView).execute(Preferences.getString(param.getTag()+_camera));
//			new Thread(new DoReadRunnable(cameraView, Preferences.getString(Preferences.getString(param.getTag()+_camera)))).start();
		}
	}

	public void reloadCustomView(){
		getCustomView();
	}

	@Override
	protected void onResume(){ 
		super.onResume();
		if(cameraView != null ){
			if(suspending){
				new DoRead(cameraView).execute(Preferences.getString(param.getTag()+_camera));
//				new Thread(new DoReadRunnable(cameraView, Preferences.getString(Preferences.getString(param.getTag()+_camera)))).start();
				suspending = false;
			}
		}
	}

	public void onStop() {

		cameraView.setVisibility(View.GONE);
		super.onStop();
	}

	public void onDestroy() {	    	
		if(cameraView != null){
			cameraView.freeCameraMemory();
		}
		cameraView.setVisibility(View.GONE);
		super.onDestroy();
	}

	public void onPause() {
		super.onPause();
		if(cameraView!=null){
			if(cameraView.isStreaming()){
				cameraView.stopPlayback();
				suspending = true;
			}
		}
	}
	public void zoomInToolBar(){
		ObjectAnimator translateY = ObjectAnimator.ofFloat(toolbar, "translationY", 0.0f); 
		translateY.setDuration(500);
		translateY.start();
	}
	public void zoomOutToolBar(){
		ObjectAnimator translateY = ObjectAnimator.ofFloat(toolbar, "translationY", -getStatusBarHeight()*4); 
		translateY.setDuration(500);
		translateY.start();
	}


	public void SlideIn(){
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator translateY = ObjectAnimator.ofFloat(cameraLayout, "translationY", -cameraLayout.getHeight(), 0.0f); 
		translateY.setDuration(550);
		set.playTogether( translateY);
		set.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {cameraLayout.setVisibility( View.VISIBLE );}
			@Override public void onAnimationRepeat(Animator animation) {}
			@Override public void onAnimationEnd(Animator animation) {  }
			@Override public void onAnimationCancel(Animator animation) {}});
		set.start();
	}

	public void SlideOut(){
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator translateY = ObjectAnimator.ofFloat(cameraLayout, "translationY",  0.0f,-cameraLayout.getHeight()); 
		translateY.setDuration(550).setInterpolator( new android.view.animation.AccelerateInterpolator());
		set.playTogether( translateY);
		translateY.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {}
			@Override public void onAnimationRepeat(Animator animation) {}
			@Override public void onAnimationEnd(Animator animation) { cameraLayout.setVisibility( View.GONE ); }
			@Override public void onAnimationCancel(Animator animation) {}});
		set.start();

	}

	public void zoomIn(){
		ObjectAnimator translateX = ObjectAnimator.ofFloat(customView, "translationX", 0.0f);
		ObjectAnimator translateY = ObjectAnimator.ofFloat(customView, "translationY", 0.0f); 
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(customView, "scaleX", 1f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(customView, "scaleY", 1f);
		translateX.setDuration(250).setInterpolator( new android.view.animation.AccelerateInterpolator());
		translateY.setDuration(250).setInterpolator( new android.view.animation.AccelerateInterpolator());
		scaleX.start();
		scaleY.start();
		translateX.start();
		translateY.start();
	}

	public void zoomOut(){
		ObjectAnimator translateX = ObjectAnimator.ofFloat(customView, "translationX", 175.0f);
		ObjectAnimator translateY = ObjectAnimator.ofFloat(customView, "translationY", 360.0f);
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(customView, "scaleX", 0.5f);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(customView, "scaleY", 0.5f);
		translateX.setDuration(250).setInterpolator( new android.view.animation.AccelerateInterpolator());
		translateY.setDuration(250).setInterpolator( new android.view.animation.AccelerateInterpolator());
		scaleX.start();
		scaleY.start();
		translateX.start();
		translateY.start();
	}

	public void initTransition(){
		TransitionInflater inflater = TransitionInflater.from(this);
		Window window = getWindow();
		Transition otherEnterTransition = inflater.inflateTransition(R.transition.detail_enter);
		RevealTransition revTrans = createRevealTransition();
		window.setEnterTransition(sequence(revTrans,otherEnterTransition));
		Transition otherReturnTransition = inflater.inflateTransition(R.transition.detail_return);
		window.setReturnTransition(sequence( otherReturnTransition,revTrans));
		Transition shareTransitionClone = window.getSharedElementReturnTransition().clone();
		window.setSharedElementReturnTransition(shareTransitionClone);
	}

	public TransitionSet together(Transition... transitions) {
		TransitionSet enterTransition = new TransitionSet();
		enterTransition.setOrdering(TransitionSet.ORDERING_TOGETHER);
		for (Transition t:transitions) {
			enterTransition.addTransition(t);
		}
		return enterTransition;
	}

	private TransitionSet sequence(Transition... transitions) {
		TransitionSet enterTransition = new TransitionSet();
		enterTransition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
		for (Transition t:transitions) {
			enterTransition.addTransition(t);
		}
		return enterTransition;
	}

	private RevealTransition createRevealTransition() {
		//		Point epicenter = getIntent().getParcelableExtra("position");
		Point epicenter = new Point(0, 0); //other possibility
		//		Display display = getWindowManager().getDefaultDisplay();
		//		Point epicenter = new Point();
		//		display.getSize(epicenter);
		//		epicenter.x = epicenter.x /2;
		//		epicenter.y = epicenter.y /4;
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics.heightPixels);
		RevealTransition reveal = new RevealTransition(epicenter, 0, bigRadius, 400);
		return reveal;
	}


	public void initTimer(){
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				new JsonGetRequest().execute(read+"?tag="+param.getTag());
			}
		},0,1000);
	}

	public void updateValue(int result){
		update(result);
		if(param.isAlarming())
			statusImg.setImageResource(R.drawable.ic_error_red_36dp);
		else
			statusImg.setImageResource(R.drawable.ic_equalizer_black_36dp);
	}


	@Override
	public void onClick(View v) {
		if(v == alarm){

		}
		else if(v == log){

		}
		else if(v == setting){
			Intent paramBinder = new Intent(getApplicationContext(), ParamBinderItemActivity.class);
			paramBinder.putExtra("param", param);
			startActivity(paramBinder);
			endActivity();
		}
		else if(v == camera){
			if(!cameraVisible){
				showCamera();
				cameraVisible = true;
			}
			else{
				hideCamera();
				cameraVisible = false;
			}
		}
	}


	public void showCamera(){
		SlideIn();

	}
	public void hideCamera(){
		SlideOut();
	}

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		toolbar.setTitleTextColor(getResources().getColor(R.color.white));
		if (toolbar != null) { 
			setSupportActionBar(toolbar);    
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.light_blue_700);
	}

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,int oldX, int oldY) {
		if(y > 540)
			scrollView.setScrollY(540);
		//slide up
		if(y > oldY && y > 140 && enable){
			zoomOut();
			zoomOutToolBar();
			enable = false;
		}
		//slide down
		else if(y <= oldY && y < 186 && !enable){
			zoomIn();
			zoomInToolBar();
			enable = true;
		}
	}

	@Override
	public void onEndScroll(ObservableScrollView scrollView) {}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		endActivity();
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		endActivity();
	}

	public void endActivity(){
		timer.cancel();
		finishAfterTransition();
	}

	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	class JsonGetRequest extends AsyncTask<String, Void, ParamExp> implements Rest{
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(ParamExp param) {
			int result = Utils.getIntFromObject(param.getValue());
			if(result != -1){
				updateValue(result);		
			}
		}

		@Override
		protected ParamExp doInBackground(String... parames) {
			BufferedReader reader = null;
			try {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				HttpGet uri = new HttpGet(prefix+Preferences.getString("hostext")+parames[0].replaceAll(" ", "%20")); 
				HttpParams httpParameters = new BasicHttpParams();
				DefaultHttpClient client = new DefaultHttpClient(httpParameters);
				uri.setHeader("Accept", "application/json");
				HttpResponse resp = client.execute(uri);
				StatusLine status = resp.getStatusLine();
				if (status.getStatusCode() != 200) {
					Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
				} 
				InputStream json = resp.getEntity().getContent();
				reader = new BufferedReader(new InputStreamReader(json));
				Gson gson = new Gson();
				param = gson.fromJson(reader , Parameters.class).getParam().get(0);
			} catch (  Exception  e) {
				e.printStackTrace();
				return null;
			}
			return param;
		}
	}



}

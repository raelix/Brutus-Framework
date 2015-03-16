package com.brutus.andbrutus;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.brutus.andbrutus.adapter.AdapterBinderItem;
import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.utils.SystemBarTintManager;
import com.brutus.base.ParamExp;
import com.gc.materialdesign.views.Switch;

public class ParamBinderItemActivity extends ActionBarActivity implements Rest{
	//	static String tag;
	static ParamExp param;
	static  EditText alarmMin;
	static  EditText alarmMax;
	static  EditText repeat;
	static  Switch alarmEnable;
	static  EditText camUrl;
	static  Switch logEnable;
	static  Switch camEnable;
	static RelativeLayout alarmLayout;
	static RelativeLayout logLayout;
	static RelativeLayout camLayout;
	ImageView alarm ;
	ImageView log ;
	ImageView camera ;
	ImageView readOnlyImg ;
	ImageView disableImg;
	Animation animEnter ; 
	Animation animExit; 
	boolean readOnly;
	boolean disable;

	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binder_item);
		param = (ParamExp) getIntent().getExtras().getSerializable("param");
		System.out.println("selected tag "+param.getTag());
		initToolBar();
		initRecycleView();
		animEnter = AnimationUtils.loadAnimation(this, R.anim.popup_enter); 
		animExit = AnimationUtils.loadAnimation(this, R.anim.popup_exit); 
		alarm = (ImageView)  findViewById(R.id.alarm);
		log = (ImageView)  findViewById(R.id.log);
		camera = (ImageView)  findViewById(R.id.camera);
		readOnlyImg = (ImageView)  findViewById(R.id.editable);
		disableImg = (ImageView)  findViewById(R.id.disable);
		alarmMin = (EditText)  findViewById(R.id.minAlarm);
		alarmMax = (EditText)  findViewById(R.id.maxAlarm);
		repeat = (EditText)  findViewById(R.id.repeateAlarm);
		camUrl = (EditText)  findViewById(R.id.cam_url);
		camEnable = (Switch)  findViewById(R.id.switchView_cam);
		alarmEnable = (Switch)  findViewById(R.id.switchView_alarm);
		logEnable = (Switch)  findViewById(R.id.switchView_log);
		alarmLayout = (RelativeLayout)  findViewById(R.id.relative_alarm);
		logLayout = (RelativeLayout)  findViewById(R.id.relative_log);
		camLayout = (RelativeLayout)  findViewById(R.id.relative_cam);
		readOnly = Preferences.getBoolean(param.getTag()+_readOnly);
		disable = Preferences.getBoolean(param.getTag()+_disable);
		initListener();

	}

	public void initListener(){

		if(param.isAlarmEnable()){
			alarmEnable.setChecked(true);
			alarmMax.setText(""+param.getMaxAlarm());
			alarmMin.setText(""+param.getMinAlarm());
			repeat.setText(""+param.getRepeat());
		}

		if(Preferences.getBoolean(param.getTag()+_alarm)){
			alarmEnable.setChecked(true);
			alarmMax.setText(""+Preferences.getInt(param.getTag()+_alarmMax));
			alarmMin.setText(""+Preferences.getInt(param.getTag()+_alarmMin));
			repeat.setText(""+Preferences.getInt(param.getTag()+_repeate));
		}
		
		if(Preferences.getBoolean(param.getTag()+_cameraEnable)){
			camEnable.setChecked(true);
			camUrl.setText(Preferences.getString(param.getTag()+_camera));
		}
		
		if(readOnly)
			readOnlyImg.setImageResource(R.drawable.ic_create_black_18dp);	
		readOnlyImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(readOnlyImg.getTag() == null || readOnlyImg.getTag() == "false"){
					((ImageView)v).setImageResource(R.drawable.ic_create_white_18dp);		
					readOnlyImg.setTag("true");
					Preferences.putBoolean(param.getTag()+_readOnly, false);
				}
				else{
					((ImageView)v).setImageResource(R.drawable.ic_create_black_18dp);	
					readOnlyImg.setTag("false");
					Preferences.putBoolean(param.getTag()+_readOnly, true);
				}
			}
		});
		if(disable)
			disableImg.setImageResource(R.drawable.ic_block_black_18dp);	
		disableImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(disableImg.getTag() == null || disableImg.getTag() == "false"){
					((ImageView)v).setImageResource(R.drawable.ic_block_white_18dp);		
					disableImg.setTag("true");
					Preferences.putBoolean(param.getTag()+_disable, false);
				}
				else{
					((ImageView)v).setImageResource(R.drawable.ic_block_black_18dp);	
					disableImg.setTag("false");
					Preferences.putBoolean(param.getTag()+_disable, true);
				}
			} 
		});

		camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(camLayout.getTag() == null || camLayout.getTag() == "false"){
					((ImageView)v).setImageResource(R.drawable.ic_camera_alt_black_18dp);	
					camLayout.startAnimation(animEnter);
					camLayout.setVisibility(View.VISIBLE);
					camLayout.setTag("true");
				}
				else{
					((ImageView)v).setImageResource(R.drawable.ic_camera_alt_white_18dp);	
					animExit.setAnimationListener(new AnimationListener() {
						@Override public void onAnimationStart(Animation arg0) {}
						@Override public void onAnimationRepeat(Animation arg0) {}
						@Override public void onAnimationEnd(Animation arg0) {
							camLayout.setVisibility(View.GONE);
						}
					});
					camLayout.startAnimation(animExit);
					camLayout.setTag("false");
					saveChanges();
				}
			}
		});
		log.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(logLayout.getTag() == null || logLayout.getTag() == "false"){
					((ImageView)v).setImageResource(R.drawable.ic_trending_up_black_18dp);	
					logLayout.startAnimation(animEnter);
					logLayout.setVisibility(View.VISIBLE);
					logLayout.setTag("true");
				}
				else{
					((ImageView)v).setImageResource(R.drawable.ic_trending_up_white_18dp);	
					animExit.setAnimationListener(new AnimationListener() {
						@Override public void onAnimationStart(Animation arg0) {}
						@Override public void onAnimationRepeat(Animation arg0) {}
						@Override public void onAnimationEnd(Animation arg0) {
							logLayout.setVisibility(View.GONE);
						}
					});
					logLayout.startAnimation(animExit);
					logLayout.setTag("false");
					saveChanges();
				}
			}
		});
		alarm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(alarmLayout.getTag() == null || alarmLayout.getTag() == "false"){
					((ImageView)v).setImageResource(R.drawable.ic_verified_user_black_18dp);	
					alarmLayout.startAnimation(animEnter);
					alarmLayout.setVisibility(View.VISIBLE);
					alarmLayout.setTag("true");
				}
				else{
					((ImageView)v).setImageResource(R.drawable.ic_verified_user_white_18dp);	
					animExit.setAnimationListener(new AnimationListener() {
						@Override public void onAnimationStart(Animation arg0) {}
						@Override public void onAnimationRepeat(Animation arg0) {}
						@Override public void onAnimationEnd(Animation arg0) {
							alarmLayout.setVisibility(View.GONE);
						}
					});
					alarmLayout.startAnimation(animExit);
					alarmLayout.setTag("false");
					saveChanges();
				}
			}
		});
	}


	public static void saveChanges(){
		if(alarmLayout.getTag() != null){
			if(!alarmMax.getText().toString().contentEquals("") && !alarmMin.getText().toString().contentEquals("")){
				int limitAlarmMax = Integer.parseInt(alarmMax.getText().toString());
				int limitAlarmMin = Integer.parseInt(alarmMin.getText().toString());
				if(limitAlarmMin < limitAlarmMax ||  !alarmEnable.isCheck()){//o i limiti sono corretti o allarme è disabilitata
					int repeatInt = 0;
					if(repeat.getText().toString() != null && !repeat.getText().toString().contentEquals("")){
						try{
							repeatInt = Integer.parseInt(repeat.getText().toString());
						}catch(Exception e){
							e.printStackTrace();
						}
					} ;
					Preferences.putBoolean(param.getTag()+_alarm, alarmEnable.isCheck() ? true : false);
					Preferences.putInt(param.getTag()+_alarmMax, limitAlarmMax);
					Preferences.putInt(param.getTag()+_alarmMin, limitAlarmMin);
					Preferences.putInt(param.getTag()+_repeate, repeatInt);
//					new RestHtmlRequestAsyncTask().execute(Rest.alarm+
//							"?&tag="+param.getTag()+
//							"&minAlarm="+limitAlarmMin+
//							"&maxAlarm="+limitAlarmMax+
//							"&repeat="+repeatInt+
//							"&enable="+alarmEnable.isCheck());
					
					new Thread(new RestHtmlRunnable(Rest.alarm+
							"?&tag="+param.getTag()+
							"&minAlarm="+limitAlarmMin+
							"&maxAlarm="+limitAlarmMax+
							"&repeat="+repeatInt+
							"&enable="+alarmEnable.isCheck())).start();
				}
			}
		}
		//LOG
		if(logLayout.getTag() != null && logLayout.getTag().toString().contentEquals("true"))
			if(logEnable.isCheck())
				Preferences.putBoolean(param.getTag()+_log, true);
		//IPCAM
			if(!camUrl.getText().toString().contentEquals("") && camEnable.isCheck()){
				Preferences.putBoolean(param.getTag()+_cameraEnable, true);//possibile bug se usa lo stesso hash poiche sovrascrive
				Preferences.putString(param.getTag()+_camera, camUrl.getText().toString());
			}
			else
				Preferences.putBoolean(param.getTag()+_cameraEnable, false);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		saveChanges();
		this.finish();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		saveChanges();
		finish();
		return super.onOptionsItemSelected(item);
	}

	public void initToolBar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(getResources().getString(R.string.choose_theme));
		toolbar.setSubtitle(getResources().getString(R.string.param)+" "+param.getTag());
		if (toolbar != null) { 
			setSupportActionBar(toolbar);    
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.light_blue_700);
	}

	public void initRecycleView(){
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//		LinearLayoutManager   mLayoutManager = new LinearLayoutManager(this);
//		recyclerView.setLayoutManager(mLayoutManager);
		AdapterBinderItem   mAdapter = new AdapterBinderItem(this,param.getTag());
		recyclerView.setAdapter(mAdapter);//new StaggeredGridLayoutManager(2,1));new GridLayoutManager(this,2)
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,1));
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
	}
}

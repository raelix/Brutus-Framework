package com.brutus.andbrutus;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.utils.SystemBarTintManager;
import com.brutus.andbrutus.view.SimpleAnimation;

public class OptionActivity extends ActionBarActivity implements Rest,OnCheckedChangeListener{

	CheckBox showConnected;
	CheckBox notificationEnable;
	Toolbar toolbar ;
	EditText host;

	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_option);
		findViewById(R.id.optioncontentrelative).setVisibility(View.GONE);
		initToolBar();
		initContent();
		new SimpleAnimation().createCircularRevealAnimation(findViewById(R.id.optioncontentrelative), 500);
	}

	public void initContent(){
		showConnected = (CheckBox) findViewById(R.id.setonlyconnected);
		notificationEnable = (CheckBox) findViewById(R.id.setNotifyService);
		host = (EditText) findViewById(R.id.sethost);
		if(Preferences.getBoolean("showConnected"))
			showConnected.setChecked(true);
		else 
			showConnected.setChecked(false);
		host.setText(Preferences.getString("hostext"));
		if(Preferences.getBoolean("notificationEnable"))
			notificationEnable.setChecked(true);
		else 
			notificationEnable.setChecked(false);
		showConnected.setOnCheckedChangeListener(this);
		notificationEnable.setOnCheckedChangeListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		endActivity();
	}

	public void endActivity(){
		new SweetAlertDialog(OptionActivity.this, SweetAlertDialog.WARNING_TYPE)
		.setTitleText("Are you sure?")
		.setContentText("Change host!")
		.setCancelText("No,cancel!")
		.setConfirmText("Yes,change!")
		.showCancelButton(true)
		.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sDialog.setTitleText("Cancelled!")
				.setContentText("nothings changed!")
				.setConfirmText("OK")
				.showCancelButton(false)
				.setCancelClickListener(null)
				.setConfirmClickListener(null)
				.changeAlertType(SweetAlertDialog.ERROR_TYPE);

				new SimpleAnimation().destroyCircularRevealAnimation(findViewById(R.id.optioncontent), 0);
				finishAfterTransition();
			}
		})
		.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
			@Override
			public void onClick(SweetAlertDialog sDialog) {
				sDialog.setTitleText("Change!")
				.setContentText("change success!!")
				.setConfirmText("OK")
				.showCancelButton(false)
				.setCancelClickListener(null)
				.setConfirmClickListener(null)
				.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
				Preferences.putString("hostext",host.getText().toString());

				new SimpleAnimation().destroyCircularRevealAnimation(findViewById(R.id.optioncontent), 0);
				finishAfterTransition();
			}
		}).show();
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		endActivity();
		return super.onOptionsItemSelected(item);
	}

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		//		toolbar.setTitle("AndBrutus");
		toolbar.setTitle("Customize Options");
		if (toolbar != null) { 
			setSupportActionBar(toolbar);    
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setOnMenuItemClickListener(
				new Toolbar.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						endActivity();
						return false;
					}
				}
				);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.light_blue_700);
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(buttonView == showConnected){
			if(isChecked)
				Preferences.putBoolean("showConnected",true);
			else
				Preferences.putBoolean("showConnected",false);
		}
		else if(buttonView == notificationEnable){
			if(isChecked)
				Preferences.putBoolean("notificationEnable",true);
			else
				Preferences.putBoolean("notificationEnable",false);
		}

	}
}

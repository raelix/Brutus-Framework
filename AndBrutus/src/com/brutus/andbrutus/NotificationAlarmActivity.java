package com.brutus.andbrutus;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener;

import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.base.ParamExp;

public class NotificationAlarmActivity extends Activity implements Rest{
	String title;
	String text;
	TextView txttitle;
	TextView txttext;
	String command;
	ParamExp param;
	NotificationManager notificationmanager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Intent i = getIntent();
		command = i.getStringExtra("command");
		param = (ParamExp) i.getSerializableExtra("param");
		if(param != null)
			dispatchCommand(command);

	}

	public void dispatchCommand(String command){
		if(command != null){
			switch(command){
			case "reset":
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText(getResources().getString(R.string.reset_notify))
				.setContentText(getResources().getString(R.string.desc_reset_notify))
				.setCancelText(getResources().getString(R.string.No))
				.setConfirmText(getResources().getString(R.string.Yes))
				.showCancelButton(true)
				.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText(getResources().getString(R.string.Cancelled))
						.setContentText(getResources().getString(R.string.Cancelled_desc_reset))
						.setConfirmText(getResources().getString(R.string.Ok))
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								finish();
							}
						})
						.changeAlertType(SweetAlertDialog.ERROR_TYPE);
					}
				})
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText(getResources().getString(R.string.Send))
						.setContentText(getResources().getString(R.string.reset_success))
						.setConfirmText(getResources().getString(R.string.Ok))
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								notificationmanager.cancel(notificationAlarmId);
								finish();
							}
						})
						.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
						new Thread(new RestHtmlRunnable(Rest.resetAlarm+"?&tag="+param.getTag())).start();
					}
				})
				.show();
				break;
			case "disable":
				new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
				.setTitleText(getResources().getString(R.string.disable_alarm_notify))
				.setContentText(getResources().getString(R.string.desc_disable_alarm_notify))
				.setCancelText(getResources().getString(R.string.No))
				.setConfirmText(getResources().getString(R.string.Yes))
				.showCancelButton(true)
				.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText(getResources().getString(R.string.Cancelled))
						.setContentText(getResources().getString(R.string.Cancelled_desc_disable))
						.setConfirmText(getResources().getString(R.string.Ok))
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								finish();
							}
						})
						.changeAlertType(SweetAlertDialog.ERROR_TYPE);
					}
				})
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText(getResources().getString(R.string.Send))
						.setContentText(getResources().getString(R.string.disable_success))
						.setConfirmText(getResources().getString(R.string.Ok))
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(new OnSweetClickListener() {

							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								notificationmanager.cancel(notificationAlarmId);
								finish();
							}
						})
						.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
						new Thread(new RestHtmlRunnable(Rest.alarm+"?&tag="+param.getTag()+"&enable="+false)).start();
//						new RestHtmlRequestAsyncTask().execute(Rest.alarm+"?&tag="+param.getTag()+"&enable="+false);
					}
				})
				.show();
				break;
			}
		}
	}
}
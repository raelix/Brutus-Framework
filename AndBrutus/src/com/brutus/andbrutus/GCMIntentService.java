package com.brutus.andbrutus;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.MaskMap;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.base.ParamAlarm;
import com.brutus.base.ParamExp;
import com.google.android.gcm.GCMBaseIntentService;
public class GCMIntentService extends GCMBaseIntentService implements Rest{

	public static final String SENDER_ID = "681925213640";
	public static String id = "681925213640";
	public static Activity activity;

	public GCMIntentService() {
		super(SENDER_ID);
	}


	@Override
	protected void onRegistered(Context arg0, String registrationId) {
		Toast.makeText(arg0, "OnRegistered", Toast.LENGTH_LONG).show();
		new Thread(new RestHtmlRunnable(idKey+registrationId)).start();
//		new RestHtmlRequestAsyncTask().execute(idKey+registrationId);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("message");
		//		generaNotifica(context, message);//da inserire sul db
		putAlarmInTree(message);
	}

	@Override
	protected void onError(Context arg0, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	public void putAlarmInTree(String message){
		if(message.contains(";")){//there are separator
			StringTokenizer token = new StringTokenizer(message, ";");
			String name = token.nextToken();
			String quality = token.nextToken();
			Object value = token.nextToken();
			String time = token.nextToken();
			new MaskMap(this.getApplicationInfo().dataDir+"/"+ dbName);//avoid null pointer on db directory folder
			ParamExp param = (ParamExp) MaskMap.getInstance().getFromTree(name, configParameters); // key = tagName, value = ParamExp.class , hashName=configParameters
			if(param == null){
				param = new ParamExp();
				param.setTag(name);
			}
			param.setQuality(Integer.parseInt(quality));
			param.setValue(value);
			param.getAlarm().add(new ParamAlarm(name, Long.parseLong(time), Integer.parseInt(quality), value));
			MaskMap.getInstance().putInTree(name, param, configParameters);
			customNotifyAlarm(param);

		}
	}

	public void customNotifyAlarm(ParamExp param) {
		RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.notification_alarm);

		String status;
		if(param.getQuality() == 0) status = "alarm: disconnected";
		else status ="alarm: "+param.getValue();

		Intent home = new Intent(this, MainActivity.class);
		PendingIntent homeIntent = PendingIntent.getActivity(this, 2, home,Intent.FLAG_ACTIVITY_NEW_TASK );

		Intent disable = new Intent(this, NotificationAlarmActivity.class);
		disable.putExtra(command, GCMIntentService.disable);
		disable.putExtra("param", param);
		PendingIntent disableIntent = PendingIntent.getActivity(this, 0, disable,Intent.FLAG_ACTIVITY_NEW_TASK );

		Intent reset = new Intent(this, NotificationAlarmActivity.class);
		reset.putExtra(command, GCMIntentService.reset);
		reset.putExtra("param", param);
		PendingIntent resetIntent = PendingIntent.getActivity(this, 1, reset,Intent.FLAG_ACTIVITY_NEW_TASK );

		NotificationCompat.Builder builder = new NotificationCompat.Builder(this).
				setSmallIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha)
				.setPriority(Notification.PRIORITY_MAX)
				.setTicker(""+param.getTag()+" "+status)
				.setStyle(new NotificationCompat.InboxStyle()
				.addLine(param.getTag())
				.addLine(status)
				.setBigContentTitle("AndBrutus"))
				.addAction(R.drawable.ic_block_white_24dp, "Disable", disableIntent)
				.addAction(R.drawable.ic_notifications_none_white_24dp, "Reset", resetIntent)
				.setDefaults(Notification.DEFAULT_ALL)
				.setContentIntent(homeIntent)
				.setContent(remoteViews)
				.setContentText(status)
				.setContentTitle(""+param.getTag())
				;

		remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.abc_ic_menu_cut_mtrl_alpha);
		remoteViews.setTextViewText(R.id.title,param.getTag());
		remoteViews.setTextViewText(R.id.text,status);
		remoteViews.setOnClickPendingIntent(R.id.reset, resetIntent);
		remoteViews.setOnClickPendingIntent(R.id.disable, disableIntent);
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify(notificationAlarmId, builder.build());

	}
}

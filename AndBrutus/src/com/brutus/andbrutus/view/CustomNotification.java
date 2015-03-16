package com.brutus.andbrutus.view;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.brutus.andbrutus.MainActivity;
import com.brutus.andbrutus.NotificationButtonReciver;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.R.color;
import com.brutus.andbrutus.adapter.AdapterBinderItem;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.base.ParamExp;
import com.brutus.base.Parameters;

public class CustomNotification implements Rest{
	boolean oneTime;
	RemoteViews remoteViews ;
	NotificationButtonReciver switchButtonListener;
	Context context;
	boolean isContextRegistered;

	public CustomNotification(Context context) {
		this.context = context;
		remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification_list);
		switchButtonListener = new NotificationButtonReciver(); //activity reciver result broadcast
	}

	public CustomNotification(Context context,boolean isContextRegistered) {
		this.context = context;
		remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification_list);
		this.isContextRegistered = isContextRegistered;
		switchButtonListener = new NotificationButtonReciver(); //activity reciver result broadcast
	}


	public void removeNotification(Parameters params){
		NotificationManager notificationmanager = (NotificationManager) context.getSystemService("notification");
		for(int k = 0 ; k < params.getParam().size() ; k++){
			notificationmanager.cancel(k);
		}
	}

	public void refreshNotification(Parameters params) {

		//create service?? Use multipleView for default and button? USO SERVICE  - ENABLE = 1TIMES/ALWAYS (tipo di refresh) - PARAM = SHOW/HIDE  (quale visualizzare)
		NotificationManager notificationmanager = (NotificationManager) context.getSystemService("notification");
		String desc ="";
//		String butt ="";

		//register parameters to activity broadcast reciver
		if(!oneTime && !isContextRegistered){
			for(int z = 0 ; z < params.getParam().size() ; z++){
				context.registerReceiver(switchButtonListener, new IntentFilter(params.getParam().get(z).getTag()));
			}
			oneTime = true;	
		}

		for(int k = 0 ; k < params.getParam().size() ; k++){

			//create title?

			int type = Preferences.getInt(params.getParam().get(k).getTag()+_type);

			//es if Preferences.getBoolean(param.getTag()+_notify) then show else hide
			if( type == AdapterBinderItem.button || type == AdapterBinderItem.defaults || type == AdapterBinderItem.graph){//per adesso si vede solo button e default bisogna limitare 
				NotificationCompat.Builder builder = new NotificationCompat.Builder(context);//i filtri soltanto a chi abilita la visualizzazione nelle notifiche

				ParamExp param = params.getParam().get(k);

				//Adapt Current View to different layout
				if(type == AdapterBinderItem.button){
					desc = (Double)param.getValue() > 0 ? context.getResources().getString(R.string.status_On) : context.getResources().getString(R.string.status_Off);
//					butt = (Double)param.getValue() > 0 ? context.getResources().getString(R.string.click_to_turn_off) : context.getResources().getString(R.string.click_to_turn_on);
					remoteViews.setViewVisibility(R.id.helper, View.VISIBLE);
					remoteViews.setTextViewText(R.id.helper, (Double)param.getValue() > 0 ? context.getResources().getString(R.string.On) :  context.getResources().getString(R.string.Off));
				}
				else{
					remoteViews.setViewVisibility(R.id.helper, View.GONE);
					String unitMeasure = Preferences.getString(param.getTag()+_unit);
					if(unitMeasure != null && !unitMeasure.contentEquals(""))
						desc = unitMeasure+": "+param.getValue();
					else
						desc =  context.getResources().getString(R.string.Value)+": "+param.getValue() ;
//					butt = context.getResources().getString(R.string.clic_show_data);
				}


				Intent switchIntent = new Intent(param.getTag());//Intent switch
				switchIntent.putExtra("param", param);
				switchIntent.putExtra("params", params);
				switchIntent.putExtra("switch", (Double)param.getValue()); //Passo il parametro attuale
				PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 888, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT ); //default 0

				Intent home = new Intent(context, MainActivity.class);
				PendingIntent homeIntent = PendingIntent.getActivity(context, 2, home,Intent.FLAG_ACTIVITY_NEW_TASK );

				//Build
				builder.setSmallIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha)
				.setTicker("Andbrutus Action Board")
				.setShowWhen(false)
				.setWhen(k)//permit update same
				.setCategory("Category")
				.setGroup("Group")
				.setGroupSummary(true)
				.setOnlyAlertOnce(true)
				.setOngoing(true)  //not removable
				.setContentIntent(homeIntent)
				.setContent(remoteViews);
				//.setStyle(new NotificationCompat.InboxStyle()
				//.addLine(param.getTag())
				//.addLine(desc)
				//.setBigContentTitle("AndBrutus"))
				//.addAction(R.drawable.ic_block_white_24dp, butt, pendingSwitchIntent);
				//remoteViews.setInt(R.id.notification_list, "setBackgroundColor", ColorTemplate.PASTEL_COLORS[k % (ColorTemplate.PASTEL_COLORS.length) ]);

				remoteViews.setInt(R.id.notification_list, "setBackgroundColor", color.blue_800);
				remoteViews.setImageViewResource(R.id.imagenotileft, R.drawable.abc_ic_menu_cut_mtrl_alpha); //icon   View minimizzata
				remoteViews.setTextViewText(R.id.title,param.getTag());										 //titolo
				remoteViews.setTextViewText(R.id.text,desc);												//descrizione
				remoteViews.setOnClickPendingIntent(R.id.helper, pendingSwitchIntent);
				notificationmanager.notify(k, builder.build());
			}
		}

	}
}

package com.brutus.andbrutus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Preferences {
	public static SharedPreferences preferences;

	public Preferences(Context context) {
		init(context);
	}

	public static void init(Context context){
		if(context != null)
		preferences = context.getApplicationContext().getSharedPreferences("AndBrutus", Context.MODE_PRIVATE);
	}

	public static void putString( String name, String value){
		if(preferences == null) return;
		Editor editor = preferences.edit();
		editor.putString(name, value).commit();
	}

	public static String getString(String name){
		if(preferences == null) return null;
		else return preferences.getString(name, null);
	}

	public static void putInt( String name, int value){
		if(preferences == null) return;
		Editor editor = preferences.edit();
		editor.putInt(name, value).commit();
	}

	public static int getInt(String name){
		if(preferences == null) return 0;
		else return preferences.getInt(name, -1);
	}
	
	public static void putBoolean( String name, boolean value){
		if(preferences == null) return;
		Editor editor = preferences.edit();
		editor.putBoolean(name, value).commit();
	}

	public static boolean getBoolean(String name){
		if(preferences == null) return false;
		else return preferences.getBoolean(name, false);
	}
	
	public static void putFloat( String name, float value){
		if(preferences == null) return;
		Editor editor = preferences.edit();
		editor.putFloat(name, value).commit();
	}

	public static float getFloat(String name){
		if(preferences == null) return 0;
		else return preferences.getFloat(name, 0);
	}
	
	public static void putString(Activity context, String name, String value){
		if(preferences == null)
			init(context);
		Editor editor = preferences.edit();
		editor.putString(name, value).commit();
	}

	public static String getString(Activity context,String name){
		return preferences.getString(name, null);
	}
}

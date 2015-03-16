package com.brutus.andbrutus.adapter;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brutus.andbrutus.ParamBinderItemActivity;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.viewholder.ViewHolderButtonBase;
import com.brutus.andbrutus.viewholder.ViewHolderDefaultBase;
import com.brutus.andbrutus.viewholder.ViewHolderGaugeBase;
import com.brutus.andbrutus.viewholder.ViewHolderPieBase;

public class AdapterBinderItem extends RecyclerView.Adapter<ViewHolder> implements Rest{
	String mDataset;
	Activity activity;
	Animation animEnter;
	Animation animExit; 

	public AdapterBinderItem(Activity activity, String myDataset) {
		this.activity = activity;
		this.animEnter = AnimationUtils.loadAnimation(activity, R.anim.popup_enter); 
		this.animExit = AnimationUtils.loadAnimation(activity, R.anim.popup_exit); 
		this.mDataset = myDataset;
	}

	@Override
	public ViewHolder onCreateViewHolder( ViewGroup parent,int viewType) {
		final String paramName = mDataset;
		if(viewType == defaults){
			return loadDefault(parent,paramName);
		}
		else if(viewType == gauge){
			return	loadGauge(parent, paramName);
		}
		else if(viewType == button){
			return loadButton(parent,paramName);
		}
		else if(viewType == input){
			return loadInput(parent,paramName);
		}
		else if(viewType == graph){
			return loadPie(parent,paramName);
		}
		else{
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_default, parent, false);
			return new ViewHolderDefaultBase(view);
		}
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		//		not used static content never notifydatasetchanged() called!
	}

	@Override 
	public int getItemViewType(int position) {
		switch(position){
		case 0:
			return defaults; 
		case 1:
			return gauge;
		case 2:
			return button;
		case 3:
			return input;
		case 4:
			return graph;
		default: 
			return defaults;
		}
	}
	@Override
	public int getItemCount() {
		return 5;
	}
	public ViewHolderPieBase loadPie(ViewGroup parent, final String paramName){
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_pie, parent, false);
		final CheckBox percent = (CheckBox) view.findViewById(R.id.usepercent);
		final CheckBox button = (CheckBox) view.findViewById(R.id.setdefaults);
		final EditText limitMinValue = (EditText) view.findViewById(R.id.minvalue);
		final EditText limitMaxValue = (EditText) view.findViewById(R.id.maxvalue);
		final EditText measureUnit = (EditText) view.findViewById(R.id.measureUnit);
		if(!Preferences.getBoolean(paramName+_percent))
			percent.setChecked(false);			
		else
			percent.setChecked(true);
		percent.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					Preferences.putBoolean(paramName+_percent, true);
				}	
				else{
					Preferences.putBoolean(paramName+_percent, false);
				}
			}
		});
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.putString(paramName, "graph");
				Preferences.putInt(paramName+_type, AdapterBinderItem.graph);
				ParamBinderItemActivity.saveChanges();
				if(!limitMaxValue.getText().toString().contentEquals("") &&
						!limitMinValue.getText().toString().contentEquals("")){
					float limitMax = Float.parseFloat(limitMaxValue.getText().toString());
					float limitMin = Float.parseFloat(limitMinValue.getText().toString());
					if(limitMin > limitMax){
						button.setChecked(false);
						Toast.makeText(activity, "Wronge limit!", Toast.LENGTH_SHORT).show();
						return;
					}
					else{
						Preferences.putString(paramName+_unit, measureUnit.getText().toString());
						Preferences.putFloat(paramName+_limitMax, limitMax);
						Preferences.putFloat(paramName+_limitMin, limitMin);
						ParamBinderItemActivity.saveChanges();
						activity.finish();
					}
				}else{
					button.setChecked(false);
					Toast.makeText(activity, "You must set limit scale!", Toast.LENGTH_SHORT).show();
					return;
				}
				activity.finish();
			}
		});
		return new ViewHolderPieBase(view);
	}
	public ViewHolderButtonBase loadButton(ViewGroup parent, final String paramName){
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_button, parent, false);
		CheckBox button = (CheckBox) view.findViewById(R.id.setdefaults);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.putString(paramName, "button");
				Preferences.putInt(paramName+_type, AdapterBinderItem.button);
				ParamBinderItemActivity.saveChanges();
				activity.finish();
			}
		});
		return new ViewHolderButtonBase(view);
	}
	public ViewHolderButtonBase loadInput(ViewGroup parent, final String paramName){
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_input, parent, false);
		CheckBox button = (CheckBox) view.findViewById(R.id.setdefaults);
		final EditText measureUnit = (EditText) view.findViewById(R.id.measureUnit);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.putString(paramName, "input");
				Preferences.putInt(paramName+_type, AdapterBinderItem.input);
				Preferences.putString(paramName+_unit, measureUnit.getText().toString());
				ParamBinderItemActivity.saveChanges();
				activity.finish();
			}
		});
		return new ViewHolderButtonBase(view);
	}
	public ViewHolderDefaultBase loadDefault(ViewGroup parent, final String paramName){
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_default, parent, false);
		CheckBox button = (CheckBox) view.findViewById(R.id.setdefaults);
		final EditText measureUnit = (EditText) view.findViewById(R.id.measureUnit);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.putString(paramName, "defaults");
				Preferences.putInt(paramName+_type, defaults);
				Preferences.putString(paramName+_unit, measureUnit.getText().toString());
				ParamBinderItemActivity.saveChanges(); 
				activity.finish();
			}
		});
		return new ViewHolderDefaultBase(view);
	}
	public ViewHolderGaugeBase loadGauge(ViewGroup parent,final String paramName){
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_gauge, parent, false);
		//		TextView  mTextView = (TextView) view.findViewById(R.id.texts);
		final CheckBox button = (CheckBox) view.findViewById(R.id.setgauge);
		final EditText limitMinValue = (EditText) view.findViewById(R.id.minvalue);
		final EditText limitMaxValue = (EditText) view.findViewById(R.id.maxvalue);
		final EditText measureUnit = (EditText) view.findViewById(R.id.measureUnit);
		((TextView) view.findViewById(R.id.texts1)).setText("Limit scale of "+mDataset);
		button.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(!limitMaxValue.getText().toString().contentEquals("") &&
						!limitMinValue.getText().toString().contentEquals("")){
					float limitMax = Float.parseFloat(limitMaxValue.getText().toString());
					float limitMin = Float.parseFloat(limitMinValue.getText().toString());
					if(limitMin > limitMax){
						button.setChecked(false);
						Toast.makeText(activity, "Wronge limit!", Toast.LENGTH_SHORT).show();
						return;
					}
					else{
						Preferences.putString(paramName, "gauge");
						Preferences.putInt(paramName+_type, gauge);
						Preferences.putString(paramName+_unit, measureUnit.getText().toString());
						Preferences.putFloat(paramName+_limitMax, limitMax);
						Preferences.putFloat(paramName+_limitMin, limitMin);
						ParamBinderItemActivity.saveChanges();
						activity.finish();
					}
				}else{
					button.setChecked(false);
					Toast.makeText(activity, "You must set limit scale!", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		});
		return new ViewHolderGaugeBase(view);
	}
}
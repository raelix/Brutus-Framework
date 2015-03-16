package com.brutus.andbrutus.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.brutus.andbrutus.ParamBinderItemActivity;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.view.SimpleAnimation;
import com.brutus.base.Parameters;
import com.gc.materialdesign.views.ButtonFlat;

public class AdapterBinder extends RecyclerView.Adapter<AdapterBinder.ViewHolder> implements Rest{
	private Parameters mDataset;
	private Activity activity;

	public AdapterBinder(Activity activity,Parameters myDataset) {
		this.mDataset = myDataset;
		this.activity = activity;
	}

	@Override
	public AdapterBinder.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_selector, parent, false);
		TextView  mTextView = (TextView) view.findViewById(R.id.texts);
		TextView  mTypeParam = (TextView) view.findViewById(R.id.typeParam);
		ButtonFlat edit = (ButtonFlat) view.findViewById(R.id.edit);
		new SimpleAnimation().translationY(view,1000);
		return new ViewHolder(view,mTextView,mTypeParam,edit);
	}



	@Override
	public void onBindViewHolder(ViewHolder holder,  int position) {
		final String tag = mDataset.getParam().get(position).getTag();
		final int pos = position;
		holder.checkParam.setOnCheckedChangeListener(null);
		holder.edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent paramBinder = new Intent(activity.getApplicationContext(), ParamBinderItemActivity.class);
				paramBinder.putExtra("param", mDataset.getParam().get(pos));
				activity.startActivity(paramBinder);
			}
		});

		holder.mText.setText(tag);

		if(Preferences.getString(tag) == null || Preferences.getString(tag).contentEquals("")){
			holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.only_data));
		}

		else{
			if(Preferences.getString(tag).contentEquals(defaultString))
				holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.Default));
			else if(Preferences.getString(tag).contentEquals(graphString))
				holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.Pie));
			else if(Preferences.getString(tag).contentEquals(gaugeString))
				holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.Gauge));
			else if(Preferences.getString(tag).contentEquals(buttonString))
				holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.Button));
			else if(Preferences.getString(tag).contentEquals(inputString))
				holder.mTypeParam.setText(holder.mView.getResources().getString(R.string.type_show)+" \n"+holder.mView.getResources().getString(R.string.Input));

		}

		if(!Preferences.getBoolean(tag+_disable)){
			holder.checkParam.setChecked(true);
		}
		else{
			holder.checkParam.setChecked(false);
		}
		
		holder.checkParam.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Preferences.putBoolean(tag+_disable,isChecked ? false : true);

			}
		});
		


	}

	@Override
	public int getItemCount() {
		return mDataset.getParam().size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public View mView;
		public TextView mText;
		TextView mTypeParam;
		ButtonFlat edit;
		CheckBox checkParam;

		public ViewHolder(View mView,TextView mText,TextView mTypeParam,ButtonFlat edit) {
			super(mView);
			this.mView = mView;
			this.mText = mText;
			this.mTypeParam = mTypeParam;
			this.edit = edit;
			this.checkParam = (CheckBox) mView.findViewById(R.id.checkParam);
		}
	}

}
package com.brutus.andbrutus.adapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brutus.andbrutus.R;
import com.brutus.base.ParamExp;

public class AlarmBinder extends RecyclerView.Adapter<AlarmBinder.ViewHolder> {
	 private ParamExp mDataset;

	public ParamExp getmDataset() {
		return mDataset;
	}

	public void setmDataset(ParamExp mDataset) {
		this.mDataset = mDataset;
	}

	public AlarmBinder(ParamExp myDataset) {
		this.mDataset = myDataset;
	}

	@Override
	public AlarmBinder.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
		View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_alarm, parent, false);
		TextView  mTextView = (TextView) view.findViewById(R.id.texts);
		ImageView  mTypeContent = (ImageView) view.findViewById(R.id.typeContent);
		TextView  mTypeParam = (TextView) view.findViewById(R.id.typeParam);
		return new ViewHolder(view,mTextView,mTypeContent,mTypeParam);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onBindViewHolder(ViewHolder holder,  int position) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		c.setTimeInMillis(mDataset.getAlarm().get(position).getDate());
		holder.mText.setText(""+sdf.format(c.getTime()));
		if(mDataset.getAlarm().get(position).getQuality() == 0){
			holder.mTypeContent.setImageResource(R.drawable.led_red);
		}
		else{
			holder.mTypeContent.setImageResource(R.drawable.led_green);
		}
		holder.mTypeParam.setText(""+mDataset.getAlarm().get(position).getValue());
		
	}

	@Override
	public int getItemCount() {
		return mDataset.getAlarm().size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public View mView;
		public TextView mText;
		ImageView mTypeContent;
		TextView mTypeParam;

		public ViewHolder(View mView,TextView mText,ImageView mTypeContent,TextView mTypeParam) {
			super(mView);
			this.mView = mView;
			this.mText = mText;
			this.mTypeContent = mTypeContent;
			this.mTypeParam = mTypeParam;
		}
	}

}
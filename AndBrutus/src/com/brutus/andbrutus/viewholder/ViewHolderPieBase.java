package com.brutus.andbrutus.viewholder;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.brutus.andbrutus.R;
import com.brutus.andbrutus.details.DetailGraphActivity;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.base.ParamExp;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

public class ViewHolderPieBase extends ViewHolderBase implements OnClickListener {
	public TextView nameDesc;
	public PieChart mChart;

	public ViewHolderPieBase(View mView) {
		super(mView);
	}

	public ViewHolderPieBase(View mView,Context activity ) {
		super(mView,activity);
		mChart = (PieChart) mView.findViewById(R.id.valueDef);
		nameDesc = (TextView) mView.findViewById(R.id.nameDesc); 
		mChart.animateXY(1500,1500);

	}

	public void setData(PieChart chart,int value,float limitMax,String unitMeasure) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		yVals1.add(new Entry(value, 0));
		yVals1.add(new Entry(Math.abs(limitMax - value), 1));
		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add(mView.getResources().getString(R.string.used));
		xVals.add(mView.getResources().getString(R.string.free));
		nameDesc.setText(mView.getResources().getString(R.string.avaible)+" "+(int)Math.abs(limitMax - value)+" "+unitMeasure);
		PieDataSet set1 = new PieDataSet(yVals1, "");
		set1.setSliceSpace(6f);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(mView.getResources().getColor(R.color.lime_600));
		colors.add(mView.getResources().getColor(R.color.green_600));//free
		set1.setColors(colors);
		PieData data = new PieData(xVals, set1);
		chart.setData(data);
		chart.highlightValues(null);
		chart.invalidate();
	}


	public void updateViewHolderPie( ArrayList<ParamExp> mDataset, Context activity,  int position){
		limitMax = Preferences.getFloat(mDataset.get(position).getTag()+_limitMax);
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf");
		String unitMeasure = Preferences.getString(mDataset.get(position).getTag()+_unit);
		mChart.setValueTypeface(tf);
		mChart.setValueTextSize(8);
		mChart.setCenterTextTypeface(Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf"));
		mChart.setHoleColor(mView.getResources().getColor(R.color.grey_200));
		mChart.setDrawUnitsInChart(false);
		mChart.setUnit(" "+unitMeasure);
		mChart.setDrawHoleEnabled(true);
		mChart.setHoleRadius(75);
		mChart.setDrawXValues(false);
		mChart.setDrawYValues(false);
		mChart.setValueTextColor(mView.getResources().getColor(R.color.grey_900));
		//		mChart.setDrawLegend(false);
		//		mChart.saveToGallery(fileName, quality)
		mChart.setCenterTextSize(14);
		if(Preferences.getBoolean(mDataset.get(position).getTag()+_percent))
			mChart.setUsePercentValues(true);
		else
			mChart.setUsePercentValues(false);
		mChart.setDescription("");
		if(mDataset.get(position).getValue() != null){
			if(mDataset.get(position).getValue() instanceof Integer){
				mChart.setCenterText(""+(Integer)mDataset.get(position).getValue()+" "+unitMeasure);
				setData(mChart,(Integer)mDataset.get(position).getValue(),limitMax,unitMeasure);
			}
			if(mDataset.get(position).getValue() instanceof Double){
				mChart.setCenterText(""+((Double) mDataset.get(position).getValue()).intValue()+" "+unitMeasure);
				setData(mChart,((Double)mDataset.get(position).getValue()).intValue(),limitMax,unitMeasure);
			}
			if(mDataset.get(position).getValue() instanceof Long){				
				mChart.setCenterText(""+((Long) mDataset.get(position).getValue()).intValue()+" "+unitMeasure);
				setData(mChart,((Long)mDataset.get(position).getValue()).intValue(),limitMax,unitMeasure);
			}
			if(mDataset.get(position).getValue() instanceof Short){
				mChart.setCenterText(""+((Short) mDataset.get(position).getValue()).intValue()+" "+unitMeasure);
				setData(mChart,((Short)mDataset.get(position).getValue()).intValue(),limitMax,unitMeasure);				
			}
			if(mDataset.get(position).getValue() instanceof Float){
				mChart.setCenterText(""+((Float) mDataset.get(position).getValue()).intValue()+" "+unitMeasure);
				setData(mChart,((Float)mDataset.get(position).getValue()).intValue(),limitMax,unitMeasure);				
			}
		}	 
	}

	@Override
	public void updateViewHolder( ArrayList<ParamExp> mDataset, Context activity,  int position){
		updateViewHolderPie(mDataset, activity, position);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == mText){
			Intent paramBinder = new Intent(activity, DetailGraphActivity.class);
			int[] location = new int[2];
			mChart.getLocationInWindow(location);
			Point epicenter = new Point(location[0] + mChart.getMeasuredWidth() / 2,location[1] + mChart.getMeasuredHeight() / 2);
			paramBinder.putExtra("position", epicenter);
			paramBinder.putExtra("param", mDataset.get(position));
			activity.startActivity(paramBinder, ActivityOptions.makeSceneTransitionAnimation(
					(Activity)activity,
					Pair.create((View)mText, "title"),
//					Pair.create((View)((Activity)activity).findViewById(R.id.toolbar), "toolbar"),
					Pair.create((View)mChart, "value"),
					Pair.create((View)sensor, "status")
					).toBundle());
			return;
		}
	}

}

//chart.highlightValues(null);
//chart.invalidate();
//}
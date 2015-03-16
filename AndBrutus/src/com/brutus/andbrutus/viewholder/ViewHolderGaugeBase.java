package com.brutus.andbrutus.viewholder;
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brutus.andbrutus.R;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.view.Thermometer;
import com.brutus.base.ParamExp;

public class ViewHolderGaugeBase extends ViewHolderBase{
	Thermometer term;
	public TextView nameDesc;

	public ViewHolderGaugeBase(View mView) {
		super(mView);
	}

	public ViewHolderGaugeBase(View mView,Context activity) {
		super(mView,activity);
		term = (Thermometer) mView.findViewById(R.id.thermometer);
		nameDesc = (TextView) mView.findViewById(R.id.nameDesc); 
	} 
	
	
	@Override
	public void updateViewHolder( ArrayList<ParamExp> mDataset, Context activity,  int position){
		updateGauge(term, mDataset.get(position));
	}

	public void updateGauge(Thermometer term,ParamExp param){
		String unitMeasure = Preferences.getString(mDataset.get(position).getTag()+_unit);
		//term.handPosition = ((Double) param.getValue()).intValue();
		term.scaleMinValue = (int) Preferences.getFloat(param.getTag()+_limitMin);
		term.scaleMaxValue =  (int) Preferences.getFloat(param.getTag()+_limitMax);
		term.scaleCenterValue = term.scaleMaxValue/2; 
		term.totalNotches = term.scaleMaxValue;
		term.degreesPerNotch = 360.0f / term.totalNotches;	
		term.incrementPerLargeNotch =  term.scaleMaxValue / 10;
		try{
			term.change(((Double) param.getValue()).intValue());
			term.handPosition = ((Double) param.getValue()).intValue();
			nameDesc.setText(mView.getResources().getString(R.string.Value)+" "+(int)((Double) param.getValue()).intValue()+" "+unitMeasure);
			term.scaleLowerTitle = ""+((Double) param.getValue()).intValue()+" "+unitMeasure;
		}
		catch(Exception e){
			term.change(0);
			term.handPosition = 0;
			nameDesc.setText(mView.getResources().getString(R.string.Value)+" ");
			term.scaleLowerTitle = "Error "+Preferences.getString(param.getTag()+_unit);
		}
		//		term.scaleUpperTitle = param.getTag();
		term.invalidate();
	}

	public void initGaugeView(View convertView){
		Thermometer term = (Thermometer) convertView.findViewById(R.id.thermometer);
		term.scaleCenterValue = (50); 
		term.scaleMinValue = 0;
		term.scaleMaxValue = 100;
		term.totalNotches = term.scaleMaxValue;
		term.degreesPerNotch = 360.0f / term.totalNotches;	
		term.incrementPerLargeNotch =  term.scaleMaxValue / 10;
		term.handPosition = 50;
		term.change(50);
		RelativeLayout.LayoutParams   lp = new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		lp.topMargin = 0;
		lp.width = 650;
		lp.height = 650;
		lp.bottomMargin=50;
		term.scaleUpperTitle = "Gauge";
		term.scaleLowerTitle = ""+50;
	}

	

}

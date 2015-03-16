package com.brutus.andbrutus.details;
import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.brutus.andbrutus.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

public class DetailGraphActivity extends BaseDetailActivity{

	PieChart chart;

	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_graph_base); 
		initChart();
		init();
		
	}

	@Override
	public void update(int result){
		setData(result);
	}

	@Override
	View getCustomView() {
		return chart;
	}

	public void initChart(){
		chart = (PieChart) findViewById(R.id.valueDef);
		Typeface tf = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		chart.setValueTypeface(tf);
		chart.setValueTextSize(9);
		chart.setCenterTextTypeface(tf);
		chart.setHoleColor(getResources().getColor(R.color.grey_200));
		chart.setDrawUnitsInChart(false);
		chart.setUnit(" "+unitMeasure);
		chart.setDrawHoleEnabled(true);
		chart.setHoleRadius(75);
		chart.setDrawXValues(true);
		chart.setDrawYValues(true);
		chart.setValueTextColor(getResources().getColor(R.color.grey_900));
		chart.setCenterTextSize(16);
		chart.setDescription("");
		//chart.setDrawLegend(false);
		//chart.saveToGallery(fileName, quality)
	}

	public void setData(int value) {
		ArrayList<Entry> yVals1 = new ArrayList<Entry>();
		yVals1.add(new Entry(value, 0));
		yVals1.add(new Entry(Math.abs(limitMax - value), 1));
		ArrayList<String> xVals = new ArrayList<String>();
		xVals.add(getResources().getString(R.string.used));
		xVals.add(getResources().getString(R.string.free));
		desc.setText(getResources().getString(R.string.avaible)+" "+(int)Math.abs(limitMax - value)+" "+unitMeasure);
		PieDataSet set1 = new PieDataSet(yVals1, "");
		set1.setSliceSpace(6f);
		ArrayList<Integer> colors = new ArrayList<Integer>();
		colors.add(getResources().getColor(R.color.lime_600));
		colors.add(getResources().getColor(R.color.green_600));//free
		set1.setColors(colors);
		PieData data = new PieData(xVals, set1);
		chart.setCenterText(""+value+" "+unitMeasure);
		chart.setData(data);
		chart.highlightValues(null);
		chart.invalidate();
	}

}

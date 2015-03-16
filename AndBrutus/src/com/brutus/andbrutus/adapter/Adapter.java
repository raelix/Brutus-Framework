package com.brutus.andbrutus.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.view.SimpleAnimation;
import com.brutus.andbrutus.viewholder.ViewHolderBase;
import com.brutus.andbrutus.viewholder.ViewHolderButtonBase;
import com.brutus.andbrutus.viewholder.ViewHolderDefaultBase;
import com.brutus.andbrutus.viewholder.ViewHolderGaugeBase;
import com.brutus.andbrutus.viewholder.ViewHolderPieBase;
import com.brutus.base.Parameters;

public class Adapter extends RecyclerView.Adapter<ViewHolder> implements Rest{
	private Parameters mDataset;
	Context activity;

	public Adapter(Context activity,Parameters myDataset) {
		this.mDataset = myDataset;
		this.activity = activity;
	} 

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
		if(viewType == -1 || viewType == defaults ){//Use default
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_default_base, parent, false);
			new SimpleAnimation().translationY(view,800);
			return new ViewHolderDefaultBase(view,activity);
		}
		else if(viewType == gauge){
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_gauge_base, parent, false);
			new SimpleAnimation().translationY(view,800);
			return new ViewHolderGaugeBase(view,activity);
		}
		else if(viewType == button){
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_button_base, parent, false);
			new SimpleAnimation().translationY(view,800);
			return new ViewHolderButtonBase(view,activity);
		}	
		//		else if(viewType == input){
		//			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_button, parent, false);
		//			TextView  mTextView = (TextView) view.findViewById(R.id.nameDef);
		//			FloatingActionButton  value = (FloatingActionButton) view.findViewById(R.id.valueDef);
		//			return new ViewHolderButton(view,mTextView,value);
		//		}	
		else if(viewType == graph){
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_graph_base, parent, false);
			new SimpleAnimation().translationY(view,800);
			return new ViewHolderPieBase(view,activity);
		}
		else{//Use default
			View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.layout_item_default_base, parent, false);
			new SimpleAnimation().translationY(view,1000);
			return new ViewHolderDefaultBase(view,activity);
		} 
	}


	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		((ViewHolderBase)holder).updateView(mDataset.getParam(),activity,position);
	}

	@Override
	public int getItemCount() {
		return mDataset.getParam().size();
	}

	@Override
	public int getItemViewType(int position) {
		return Preferences.getInt(mDataset.getParam().get(position).getTag()+_type);
	}

	public Parameters getmDataset() {
		return mDataset;
	}
	public void setmDataset(Parameters mDataset) {
		this.mDataset = mDataset;
	}
}
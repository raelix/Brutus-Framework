package com.brutus.andbrutus.viewholder;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.brutus.andbrutus.R;
import com.brutus.base.ParamExp;

public class ViewHolderDefaultBase extends ViewHolderBase{
	public TextView mValue;

	public ViewHolderDefaultBase(View mView) {
		super(mView);
	}

	public ViewHolderDefaultBase(View view, Context activity) {
		super(view,activity);
		mValue = (TextView) mView.findViewById(R.id.valueDef);
	}

	@Override
	public void updateViewHolder( ArrayList<ParamExp> mDataset, Context activity,  int position){
		mValue.setText(""+mDataset.get(position).getValue());
	}


}


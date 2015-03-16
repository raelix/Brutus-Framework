package com.brutus.andbrutus.viewholder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.Point;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.TextView;

import com.brutus.andbrutus.R;
import com.brutus.andbrutus.details.DetailButtonActivity;
import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.utils.Utils;
import com.brutus.base.ParamExp;

public class ViewHolderButtonBase extends ViewHolderBase implements OnClickListener{
	public TextView nameDesc;
	public Button button;

	public ViewHolderButtonBase(View mView) {
		super(mView);
	}

	public ViewHolderButtonBase(View mView,Context activity) {
		super(mView,activity);
		final Context temp = activity;
		button = (Button) mView.findViewById(R.id.valueDef);
		nameDesc = (TextView) mView.findViewById(R.id.nameDesc); 
		ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
			@Override
			public void getOutline(View view, Outline outline) {
				int size = temp.getResources().getDimensionPixelSize(R.dimen.button_size);
				outline.setOval(0, 0, size, size);
			}
		};
		button.setOutlineProvider(viewOutlineProvider);
		button.setOnClickListener(this);
	}

	@Override
	public void updateViewHolder( ArrayList<ParamExp> mDataset, Context activity,  int position){
		updateViewHolderButton(mDataset, activity, position);
	}

	public void updateViewHolderButton(final ArrayList<ParamExp> mDataset,final Context activity, final int position){
		updateToolBar(mDataset, activity, position);
		mText.setText(mDataset.get(position).getTag());
		boolean isEnable = false;
		if(Utils.getIntFromObject(mDataset.get(position).getValue()) == 1){
			isEnable = true;
		}
		if(isEnable){
			button.setTag(1);
			button.setText(activity.getResources().getString(R.string.On));
			nameDesc.setText(activity.getResources().getString(R.string.status_On));
		}
		else{
			button.setTag(0);
			button.setText(activity.getResources().getString(R.string.Off));
			nameDesc.setText(activity.getResources().getString(R.string.status_Off));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == button){
			param = mDataset.get(position);
			if(button.getTag() == null || (Integer)button.getTag() == 0){
				param.setValue(1);
				button.setTag(1);
			}
			else{
				param.setValue(0);
				button.setTag(0);
			}
			new Thread(new RestHtmlRunnable(Rest.write+"?&tag="+param.getTag()+"&value="+param.getValue())).start();
//			new RestHtmlRequestAsyncTask().execute(Rest.write+
//					"?&tag="+param.getTag()+
//					"&value="+param.getValue());


			//tempList.add(param);
			//tempParams.setParam(tempList);
			//new RestPostRequestAsyncTask(tempParams,activity).execute(write);	
		}	
		if(v == mText){
			Intent paramBinder = new Intent(activity, DetailButtonActivity.class);
			int[] location = new int[2];
			button.getLocationInWindow(location);
			Point epicenter = new Point(location[0] + button.getMeasuredWidth() / 2,location[1] + button.getMeasuredHeight() / 2);
			paramBinder.putExtra("position", epicenter);
			paramBinder.putExtra("param", mDataset.get(position));
			activity.startActivity(paramBinder, ActivityOptions.makeSceneTransitionAnimation(
					(Activity)activity,
					Pair.create((View)mText, "title"),
					Pair.create((View)button, "value"),
					Pair.create((View)sensor, "status")
					).toBundle());
			return;
		}
	}
}

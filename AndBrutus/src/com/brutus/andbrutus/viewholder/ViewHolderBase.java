package com.brutus.andbrutus.viewholder;
import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.brutus.andbrutus.ParamBinderItemActivity;
import com.brutus.andbrutus.R;
import com.brutus.andbrutus.adapter.AlarmBinder;
import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.MaskMap;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.view.DividerItemDecoration;
import com.brutus.andbrutus.view.FastAnimation;
import com.brutus.base.ParamAlarm;
import com.brutus.base.ParamExp;
import com.brutus.base.Parameters;
import com.camera.simplemjpeg.DoRead;
import com.camera.simplemjpeg.MjpegView;
import com.gc.materialdesign.views.Slider;
import com.gc.materialdesign.views.Slider.OnValueChangedListener;

abstract public class ViewHolderBase extends RecyclerView.ViewHolder implements Rest,OnClickListener,OnLongClickListener{
	public float limitMin ;
	public View mView;
	public TextView mText;
	public ImageView alarm ;
	public ImageView log ;
	public ImageView camera ;
	public ImageView setting ;
	public ImageView visibility ;
	public ImageView rw ;
	public ImageView percentShow ;
	public ImageView led ;
	public ImageView sensor ;
	public ImageView alarmEnable ;
	public ImageView eraseList ;
	public TextView unitMeasure;
	public Slider slider;
	public float limitMax;
	public RecyclerView recycle;
	public AlarmBinder mAdapter;
	public ParamExp param ;
	public RelativeLayout spaceAvaible;
	public RelativeLayout camLayout;
	public int listSize;
	public int position;
	public ArrayList<ParamExp> mDataset;
	public Context activity;
	MjpegView mjpeg;
public boolean firstTimeLoadCamera = true;
	
	public ViewHolderBase(View mView) {
		super(mView);
		this.mView = mView;
	}

	public ViewHolderBase(View view, Context activity) {
		super(view);
		this.mView = view;
		this.activity = activity;
		initView();
	}


	public void initView(){
		mText = (TextView) mView.findViewById(R.id.nameDef);
		setting = ((ImageView)mView.findViewById(R.id.setting));
		alarm = (ImageView)mView.findViewById(R.id.alarm);
		log = (ImageView)mView.findViewById(R.id.log);
		camera = (ImageView)mView.findViewById(R.id.camera);
		visibility = ((ImageView)mView.findViewById(R.id.visibility));
		rw = ((ImageView)mView.findViewById(R.id.rw));
		percentShow = (ImageView) mView.findViewById(R.id.percentShow);
		led = (ImageView) mView.findViewById(R.id.led);
		sensor = (ImageView) mView.findViewById(R.id.sensor);
		alarmEnable = (ImageView) mView.findViewById(R.id.alarmEnable);
		eraseList = (ImageView) mView.findViewById(R.id.eraseList);
		slider = ((Slider) mView.findViewById(R.id.sliderNumber));
		mjpeg = (MjpegView) mView.findViewById(R.id.cameraMjpeg);
		unitMeasure = (TextView) mView.findViewById(R.id.unitMeasure);
		camLayout = (RelativeLayout) mView.findViewById(R.id.camLayoutMjpeg);
		eraseList.setImageResource(R.drawable.ic_cancel_grey600_18dp);
		initRecycleViewAlarm(activity);
		camera.setOnClickListener(this);
		eraseList.setOnClickListener(this);
		alarm.setOnLongClickListener(this);
		alarm.setOnClickListener(this);
		percentShow.setOnClickListener(this);
		setting.setOnClickListener(this);
		visibility.setOnClickListener(this);
		rw.setOnClickListener(this);
		sensor.setOnClickListener(this);
		mText.setOnClickListener(this);
	}


	public void initRecycleViewAlarm(Context activity){
		recycle = (RecyclerView) mView.findViewById(R.id.recycler_view);
		spaceAvaible = (RelativeLayout) mView.findViewById(R.id.spaceAvaible);
		recycle.setLayoutManager(new GridLayoutManager(activity,1));
		recycle.setItemAnimator(new DefaultItemAnimator());
		recycle.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL_LIST));
		if(param == null ) param = new ParamExp();
		if(mAdapter == null){
			mAdapter = new AlarmBinder(param);
			recycle.setAdapter(mAdapter);
		}
	}
	
	
	
	abstract public void updateViewHolder(ArrayList<ParamExp> mDataset, Context activity,  int position);

	public void updateView( ArrayList<ParamExp> mDataset, Context activity,  int position){
		updateRecycleViewAlarm(mDataset, activity, position); //default update of view
		updateToolBar(mDataset, activity, position);		  //default update of view
		updateViewHolder(mDataset, activity, position);		  //method to implements
	}

	public void updateRecycleViewAlarm( ArrayList<ParamExp> mDataset,  Context activity,  int position){
		ParamExp temp = (ParamExp) MaskMap.getInstance().getFromTree(mDataset.get(position).getTag(), configParameters);
		if(temp == null ){
			if(param.getAlarm().size() > 0){//case remove all item
				param.getAlarm().clear();
			}
			if(listSize != listAlarmItemSize){
				mAdapter.setmDataset(param);
				listSize = listAlarmItemSize;
				mAdapter.notifyDataSetChanged();
			}
		}
		else if(temp != null && temp.getAlarm().size() > 0){//update case
			mAdapter.setmDataset(temp);
			mAdapter.notifyDataSetChanged();
			listSize = temp.getAlarm().size() * listAlarmItemSize;
		}
		ViewGroup.LayoutParams params = spaceAvaible.getLayoutParams();
		params.height = 150+listSize;
		spaceAvaible.requestLayout();
		recycle.setMinimumHeight(listSize);

	}

	@SuppressLint("ClickableViewAccessibility")
	public void updateToolBar(final ArrayList<ParamExp> mDataset, final Context activity,  final int position){
		this.position = position;
		this.mDataset = mDataset;
		this.activity = activity;

		String unitMeasurePref = Preferences.getString(mDataset.get(position).getTag()+_unit);
		limitMin = Preferences.getFloat(mDataset.get(position).getTag()+_limitMin);
		limitMax = Preferences.getFloat(mDataset.get(position).getTag()+_limitMax);
		if(limitMax == 0) limitMax = 1;//button case
		slider.setMin((int)limitMin);
		slider.setMax((int)limitMax);
		mText.setText(mDataset.get(position).getTag());

		if(unitMeasurePref != null && !unitMeasurePref.contentEquals("")){
			unitMeasure.setText(unitMeasurePref);
		}



		if(Preferences.getBoolean(mDataset.get(position).getTag()+_cameraEnable) && firstTimeLoadCamera
				) 
			if(Preferences.getBoolean(mDataset.get(position).getTag()+_openCamera)){
				firstTimeLoadCamera = false;
			camLayout.setVisibility(View.VISIBLE);
			new DoRead(mjpeg).execute(Preferences.getString(mDataset.get(position).getTag()+_camera));
//			new Thread(new DoReadRunnable(mjpeg, Preferences.getString(mDataset.get(position).getTag()+_camera))).start();
		}
			else{
				firstTimeLoadCamera = false;
				new DoRead(mjpeg).execute(Preferences.getString(mDataset.get(position).getTag()+_camera));
//				new Thread(new DoReadRunnable(mjpeg, Preferences.getString(mDataset.get(position).getTag()+_camera))).start();
			}

		if(!Preferences.getBoolean(mDataset.get(position).getTag()+_open)){
			spaceAvaible.setVisibility(View.GONE);
			alarm.setImageResource(R.drawable.ic_notifications_grey600_18dp);
		}
		else{
			spaceAvaible.setVisibility(View.VISIBLE);
			alarm.setImageResource(R.drawable.ic_notifications_grey600_18dp);
		}
		if(Preferences.getBoolean(mDataset.get(position).getTag()+_readOnly))
			rw.setImageResource(R.drawable.ic_create_black_18dp);

		if(mDataset.get(position).isAlarming())
			sensor.setImageResource(R.drawable.ic_error_red_24dp);
		else
			sensor.setImageResource(R.drawable.ic_equalizer_black_24dp);

		alarmEnable.setImageResource(R.drawable.ic_mode_comment_grey600_18dp);

		if(!mDataset.get(position).isAlarmEnable()){
			if(!Preferences.getBoolean(mDataset.get(position).getTag()+_open))
				alarm.setImageResource(R.drawable.ic_notifications_off_grey600_18dp);
			else
				alarm.setImageResource(R.drawable.ic_notifications_off_black_18dp);
		}
		else{
			if(!Preferences.getBoolean(mDataset.get(position).getTag()+_open))
				alarm.setImageResource(R.drawable.ic_notifications_grey600_18dp);
			else
				alarm.setImageResource(R.drawable.ic_notifications_black_18dp);
		}

		if(mDataset.get(position).getQuality() == 0)
			led.setImageResource(R.drawable.led_red);
		else 
			led.setImageResource(R.drawable.led_green);
		if(Preferences.getBoolean(mDataset.get(position).getTag()+_percent))
			percentShow.setImageResource(R.drawable.ic_trending_up_black_18dp);

		else
			percentShow.setImageResource(R.drawable.ic_trending_up_grey600_18dp);

		if(Preferences.getBoolean(mDataset.get(position).getTag()+_readOnly)){
			rw.setImageResource(R.drawable.ic_create_black_18dp);
			slider.setVisibility(View.GONE);
		} 
		else{
			rw.setImageResource(R.drawable.ic_create_grey600_18dp);
			slider.setVisibility(View.VISIBLE);
		}
		if(!Preferences.getBoolean(mDataset.get(position).getTag()+_disable))
			visibility.setImageResource(R.drawable.ic_block_grey600_18dp);

		else
			visibility.setImageResource(R.drawable.ic_block_black_18dp);


		slider.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					Parameters tempParams = new Parameters();
					ArrayList<ParamExp> tempList = new ArrayList<ParamExp>();
					ParamExp param = new ParamExp();
					param = mDataset.get(position);
					param.setValue(((Integer) slider.getTag()).intValue());
					tempList.add(param);
					tempParams.setParam(tempList);
//					new RestHtmlRequestAsyncTask().execute(Rest.write+
//							"?&tag="+param.getTag()+
//							"&value="+param.getValue());

					new Thread(new RestHtmlRunnable(Rest.write+
							"?&tag="+param.getTag()+
							"&value="+param.getValue())).start();
//					new RestPostRequestAsyncTask(tempParams,activity).execute(write);
				}
				return false;
			}
		});
		slider.setOnValueChangedListener(new OnValueChangedListener() {
			@Override
			public void onValueChanged(int value) {
				slider.setTag(value);	
			}
		}); 

	}

	@Override
	public void onClick(View v) {
		ParamExp temp = mDataset.get(position);
		if(v == sensor){
			if(temp.isAlarming()){
//				new RestHtmlRequestAsyncTask().execute(resetAlarm+"?&tag="+temp.getTag());
				new Thread(new RestHtmlRunnable(resetAlarm+"?&tag="+temp.getTag())).start();
				new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText("Alarm reset!")
				.show();
			}
		}

		else if (v == camera){
			if(Preferences.getBoolean(temp.getTag()+_cameraEnable)){
				if(!Preferences.getBoolean(temp.getTag()+_openCamera)){
					Preferences.putBoolean(temp.getTag()+_openCamera,true);
//					camLayout.setVisibility(View.VISIBLE);
					SlideIn();
				}
				else{
					Preferences.putBoolean(temp.getTag()+_openCamera,false);
//					camLayout.setVisibility(View.GONE);
					SlideOut();
				}
			}
			else{
				//do u want set camera url?
			}
		}
		else if(v == rw){
			if(Preferences.getBoolean(temp.getTag()+_readOnly)){
				Preferences.putBoolean(temp.getTag()+_readOnly, false);
				rw.setImageResource(R.drawable.ic_create_grey600_18dp);
			}
			else{
				Preferences.putBoolean(temp.getTag()+_readOnly, true);
				rw.setImageResource(R.drawable.ic_create_black_18dp);
			}
		}
		else if(v == visibility){
			if(Preferences.getBoolean(temp.getTag()+_disable)){
				Preferences.putBoolean(temp.getTag()+_disable, false);
				visibility.setImageResource(R.drawable.ic_block_grey600_18dp);
			}
			else{
				Preferences.putBoolean(temp.getTag()+_disable, true);
				visibility.setImageResource(R.drawable.ic_block_black_18dp);
			}
		}
		else if (v == setting){
			Intent paramBinder = new Intent(activity.getApplicationContext(), ParamBinderItemActivity.class);
			paramBinder.putExtra("param", temp);
			activity.startActivity(paramBinder);
		}
		else if(v == percentShow){
			if(Preferences.getBoolean(temp.getTag()+_percent)){
				Preferences.putBoolean(temp.getTag()+_percent,false);
				percentShow.setImageResource(R.drawable.ic_trending_up_black_18dp);
			}
			else {
				percentShow.setImageResource(R.drawable.ic_trending_up_grey600_18dp);
				Preferences.putBoolean(temp.getTag()+_percent,true);
			}
		}
		else if(v == alarm){
			if(!Preferences.getBoolean(temp.getTag()+_open)){
				Preferences.putBoolean(temp.getTag()+_open,true);
				if(!temp.isAlarmEnable())
					alarm.setImageResource(R.drawable.ic_notifications_off_black_18dp);
				else
					alarm.setImageResource(R.drawable.ic_notifications_black_18dp);
				new FastAnimation(recycle).cardInAnimationOuther( listSize,spaceAvaible,150);
			}
			else{
				Preferences.putBoolean(temp.getTag()+_open,false);
				if(!temp.isAlarmEnable())
					alarm.setImageResource(R.drawable.ic_notifications_off_grey600_18dp);
				else
					alarm.setImageResource(R.drawable.ic_notifications_grey600_18dp);
				new FastAnimation(recycle).cardOutAnimationOuther( listSize,spaceAvaible);
			}
		}
		else if(v == eraseList){
			if( ((ParamExp) MaskMap.getInstance().getFromTree(temp.getTag(), configParameters)) != null &&
					((ParamExp) MaskMap.getInstance().getFromTree(temp.getTag(), configParameters)).getAlarm().size()  > 0){				
				SweetAlertDialog dialog = new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
				.setTitleText("Are you sure?")
				.setContentText("Logs will erase!")
				.setCancelText("No,cancel!")
				.setConfirmText("Yes,delete!")
				.showCancelButton(true)
				.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText("Cancelled!")
						.setContentText("nothings changed!")
						.setConfirmText("OK")
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(null)
						.changeAlertType(SweetAlertDialog.ERROR_TYPE);
					}
				})
				.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
					@Override
					public void onClick(SweetAlertDialog sDialog) {
						sDialog.setTitleText("Deleted!")
						.setContentText("delete success!!")
						.setConfirmText("OK")
						.showCancelButton(false)
						.setCancelClickListener(null)
						.setConfirmClickListener(null)
						.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
						MaskMap.getInstance().removeFromTree(mDataset.get(position).getTag(), configParameters);
						MaskMap.getInstance().commit();
						//						param = (ParamExp) MaskMap.getInstance().getFromTree(mDataset.get(position).getTag(), configParameters);
						param = new ParamExp();
						param.setAlarm(new ArrayList<ParamAlarm>());
						mAdapter.notifyDataSetChanged();
					}
				});
				dialog.getProgressHelper().setRimWidth(225);
				dialog.show();
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if(v == alarm){
			if(mDataset.get(position).isAlarmEnable()){
				mDataset.get(position).setAlarmEnable(false);
//				new RestHtmlRequestAsyncTask().execute(Rest.alarm+
//						"?&tag="+mDataset.get(position).getTag()+
//						"&minAlarm="+mDataset.get(position).getMinAlarm()+
//						"&maxAlarm="+mDataset.get(position).getMaxAlarm()+
//						"&repeat="+ mDataset.get(position).getRepeat()+
//						"&enable="+false);
				new Thread(new RestHtmlRunnable(Rest.alarm+
						"?&tag="+mDataset.get(position).getTag()+
						"&minAlarm="+mDataset.get(position).getMinAlarm()+
						"&maxAlarm="+mDataset.get(position).getMaxAlarm()+
						"&repeat="+ mDataset.get(position).getRepeat()+
						"&enable="+false)).start();
				new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText("Send")
				.setContentText("Alarm disable!")
				.show();
				Vibrator vibrator = (Vibrator) this.activity.getSystemService(Context.VIBRATOR_SERVICE);
				long[] pattern = {50, 30, 10};
				vibrator.vibrate(pattern,-1);
			}
			else if(!mDataset.get(position).isAlarmEnable() && mDataset.get(position).getMaxAlarm() > 0){
				mDataset.get(position).setAlarmEnable(true);
//				new RestHtmlRequestAsyncTask().execute(Rest.alarm+
//						"?&tag="+mDataset.get(position).getTag()+
//						"&minAlarm="+mDataset.get(position).getMinAlarm()+
//						"&maxAlarm="+mDataset.get(position).getMaxAlarm()+
//						"&repeat="+ mDataset.get(position).getRepeat()+
//						"&enable="+true);
				new Thread(new RestHtmlRunnable(Rest.alarm+
						"?&tag="+mDataset.get(position).getTag()+
						"&minAlarm="+mDataset.get(position).getMinAlarm()+
						"&maxAlarm="+mDataset.get(position).getMaxAlarm()+
						"&repeat="+ mDataset.get(position).getRepeat()+
						"&enable="+true)).start();
				new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
				.setTitleText("Send")
				.setContentText("Alarm enable!")
				.show();
				Vibrator vibrator = (Vibrator) this.activity.getSystemService(Context.VIBRATOR_SERVICE);
				long[] pattern = {50, 30, 10};
				vibrator.vibrate(pattern,-1);
			}
		}
		return false;
	}
	
	public void SlideIn(){
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator translateY = ObjectAnimator.ofFloat(camLayout, "translationY", -650, 0.0f); 
		translateY.setDuration(550);
		set.playTogether( translateY);
		set.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {camLayout.setVisibility( View.VISIBLE );}
			@Override public void onAnimationRepeat(Animator animation) {}
			@Override public void onAnimationEnd(Animator animation) {  }
			@Override public void onAnimationCancel(Animator animation) {}});
		set.start();
	}

	public void SlideOut(){
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator translateY = ObjectAnimator.ofFloat(camLayout, "translationY",  0.0f,-camLayout.getHeight()); 
		translateY.setDuration(550).setInterpolator( new android.view.animation.AccelerateInterpolator());
		set.playTogether( translateY);
		translateY.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {}
			@Override public void onAnimationRepeat(Animator animation) {}
			@Override public void onAnimationEnd(Animator animation) { camLayout.setVisibility( View.GONE ); }
			@Override public void onAnimationCancel(Animator animation) {}});
		set.start();

	}
}
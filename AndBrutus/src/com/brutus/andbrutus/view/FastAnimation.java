package com.brutus.andbrutus.view;

import java.util.ArrayList;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

public class FastAnimation {
	long duration = 1000L;
	View viewGroup;
	long offset_duration = 400L;
	//AccelerateDecelerateInterpolator

	public FastAnimation(View viewGroup) {
		this.viewGroup = viewGroup;
	}

	public void listOutAnimation(){
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(duration-offset_duration-200);
		animatorSet.setInterpolator(new AccelerateInterpolator());
		ArrayList<Animator> animatorList=new ArrayList<Animator>();
		ObjectAnimator anim = ObjectAnimator.ofFloat(viewGroup, "scaleY", 1.0f, 0.0f );
		animatorList.add(anim);
		//		ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "translationY",  0.0f,-800f );
		//		animatorList.add(anim1);
		animatorSet.playTogether(animatorList);
		animatorSet.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {}
			@Override public void onAnimationRepeat(Animator animation) {}						
			@Override public void onAnimationEnd(Animator animation) {viewGroup.setVisibility(View.GONE);}
			@Override public void onAnimationCancel(Animator animation) {}
		});
		animatorSet.start();
	}

	public void listInAnimation(){
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.setDuration(duration);
		animatorSet.setInterpolator(new OvershootInterpolator());
		ArrayList<Animator> animatorList=new ArrayList<Animator>();
		ObjectAnimator anim = ObjectAnimator.ofFloat(viewGroup, "scaleY", 0.0f, 1.0f );
		animatorList.add(anim);
		//		ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "translationY", -800f, 0.0f );
		//		animatorList.add(anim1);
		animatorSet.playTogether(animatorList);
		animatorSet.addListener(new AnimatorListener() {
			@Override public void onAnimationStart(Animator animation) {viewGroup.setVisibility(View.VISIBLE);}
			@Override public void onAnimationRepeat(Animator animation) {}						
			@Override public void onAnimationEnd(Animator animation) {}
			@Override public void onAnimationCancel(Animator animation) {}
		});
		animatorSet.start();
	}


	public void cardInAnimationOuther(  int offset,final View parent,int constant){
		ValueAnimator anim = ValueAnimator.ofInt(0, offset);
		ValueAnimator anima = ValueAnimator.ofInt(0, constant+offset);
		parent.setVisibility(View.VISIBLE);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				viewGroup.setMinimumHeight(val);
			}
		});
		anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams params = parent.getLayoutParams();
				params.height = val;
				parent.requestLayout();
			}
		});
		long temp = (long)(duration-offset_duration);
		anim.setDuration(temp);
		anim.start(); 
		anima.setDuration(temp-offset_duration);
		anima.start(); 
	}

	public void cardOutAnimationOuther( final int offset,final View other){
		ValueAnimator anim = ValueAnimator.ofInt(offset,0);
		final int height = other.getHeight();
		ValueAnimator anima = ValueAnimator.ofInt(height, 0);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				viewGroup.setMinimumHeight(val);
			}
		});
		anima.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams params = other.getLayoutParams();
				params.height = val;
				other.requestLayout();
			}
		});

		long temp = (long)(duration-offset_duration);
		anim.setDuration(temp);
		anim.start(); 
		anima.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {}

			@Override
			public void onAnimationRepeat(Animator animation) {}

			@Override
			public void onAnimationEnd(Animator animation) {
				other.setVisibility(View.GONE); 
				ViewGroup.LayoutParams params = other.getLayoutParams();
				params.height = height;
				other.requestLayout();
				viewGroup.setMinimumHeight(offset);
				}

			@Override
			public void onAnimationCancel(Animator animation) {}
		});
		anima.setDuration(temp-offset_duration);
		anima.start(); 
	}

	public void cardOutAnimationSample(){
		ValueAnimator anim = ValueAnimator.ofInt(viewGroup.getHeight(),0);

		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
				layoutParams.height = val;
				viewGroup.setLayoutParams(layoutParams);
			}
		});
		long temp = (long)(duration-offset_duration);
		anim.setDuration(temp);
		anim.start(); 
	}

	public void cardInAnimationSample(int offset){
		ValueAnimator anim = ValueAnimator.ofInt(0,offset);

		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				int val = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
				layoutParams.height = val;
				viewGroup.setLayoutParams(layoutParams);
			}
		});

		long temp = (long)(duration-offset_duration);
		anim.setDuration(temp);
		anim.start(); 
	}

	//	public void cardInAnimation(final View viewGroup,int offset){
		//		 ValueAnimator anim = ValueAnimator.ofInt(viewGroup.getMeasuredWidth(), viewGroup.getMeasuredWidth()+offset);
		//		    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			//		        @Override
			//		        public void onAnimationUpdate(ValueAnimator valueAnimator) {
	//		            int val = (Integer) valueAnimator.getAnimatedValue();
	//		            ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
	//		            layoutParams.height = val;
	//		            viewGroup.setLayoutParams(layoutParams);
	//		        }
	//		    });
	//		    anim.setDuration(duration);
	//		    anim.start(); 
	//	}
	//	
	//	
	//	public void cardOutAnimation(final View viewGroup,int offset){
	//		 ValueAnimator anim = ValueAnimator.ofInt(viewGroup.getMeasuredHeight(), viewGroup.getMeasuredHeight()-offset);
	//		    anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	//		        @Override
	//		        public void onAnimationUpdate(ValueAnimator valueAnimator) {
	//		            int val = (Integer) valueAnimator.getAnimatedValue();
	//		            ViewGroup.LayoutParams layoutParams = viewGroup.getLayoutParams();
	//		            layoutParams.height = val;
	//		            viewGroup.setLayoutParams(layoutParams);
	//		        }
	//		    });
	//		    anim.setDuration(duration);
	//		    anim.start(); 
	//	}
}

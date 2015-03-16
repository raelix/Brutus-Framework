package com.brutus.andbrutus.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;

public class SimpleAnimation {


	public void translationY(View convertView,long durate) {
		ObjectAnimator anim1 = ObjectAnimator.ofFloat(convertView, View.TRANSLATION_Y, 400f, 0f);
		anim1.setDuration(durate);
		ObjectAnimator anim2 = ObjectAnimator.ofFloat(convertView, View.ALPHA, 0f, 1f);
		anim2.setDuration(durate);
		startAnimation(anim1, anim2);
	}

	private void startAnimation(Animator... items){
		AnimatorSet set = new AnimatorSet();
		set.playTogether(items);
		set.start();
	}

	public void createCircularRevealAnimation(View view,long post){//remember set view gone first 
		final View myView = view;
		myView.postDelayed(new Runnable() {
			@Override
			public void run() {
				int cx = myView.getLeft() + myView.getRight() / 2;
				int cy = myView.getTop() + myView.getBottom() / 2;
				int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
				Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
				myView.setVisibility(View.VISIBLE);
				anim.start();
			}
		}, post);
	}

	public void destroyCircularRevealAnimation(View view,long post){
		final View myView = view;
		myView.postDelayed(new Runnable() {
			@Override
			public void run() {
				int cx = (myView.getLeft() + myView.getRight()) / 2;
				int cy = (myView.getTop() + myView.getBottom()) / 2;
				int initialRadius = myView.getWidth();
				Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);
				anim.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						myView.setVisibility(View.INVISIBLE);
					}});
				anim.start();
			}},post);
	}
}

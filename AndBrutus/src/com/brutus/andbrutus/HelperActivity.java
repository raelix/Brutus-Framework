package com.brutus.andbrutus;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.view.RippleBackground;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.brutus.andbrutus.R;


public class HelperActivity extends ActionBarActivity {
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	PlaceholderFragment fragmentsFirst;
	PlaceholderFragment fragmentsSecond;
	PlaceholderFragment fragmentsThird;
	listener listen;
	int positionView;
	int positionOffsetPixelView;
	public static String host;
	public static String port;
	boolean block = false;
	float positionOffsetView;
	float offset;
	float offsetY;
	static Typeface myTypeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_helper_activity);
		myTypeface = Typeface.createFromAsset(this.getAssets(),"font/Roboto-Regular.ttf");
		listen = new listener();
		mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(listen);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setPageTransformer(false, new PageTransformer() {

			@Override
			public void transformPage(View view, float position) {
				int pageWidth = view.getWidth();
				int pageHeight = view.getHeight();
				if (position < -1) { // [-1,0
				} 
				else if (position <= 1) { // (0,1]
					if(position < 0){
						if( positionOffsetView > 0  ){
							if(positionView != 1){
								mViewPager.getChildAt(0).setTranslationY(( position) * 0.15f * pageHeight);
								mViewPager.getChildAt(0).setTranslationX(-( position) *  pageWidth);
								offset = mViewPager.getChildAt(0).getX();
								offsetY =  mViewPager.getChildAt(0).getY();

							}
							else {
								view.setTranslationY(-( position) * 0.75f * pageHeight);
								view.setTranslationX(-( position) *  pageWidth);
								mViewPager.getChildAt(0).setTranslationX(positionOffsetPixelView+offset  );
								mViewPager.getChildAt(0).setTranslationY(-positionOffsetPixelView+offsetY );

							}
						}

					}
				}
				else{
				}
			}
		});
		mViewPager.setOffscreenPageLimit(3);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//	@Override
	//	public boolean onOptionsItemSelected(MenuItem item) {
	//		int id = item.getItemId();
	//		if (id == R.id.action_settings) {
	//			return true;
	//		}
	//		return super.onOptionsItemSelected(item);
	//	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			if(position == 0){
				fragmentsFirst = new  PlaceholderFragment(0);
				return fragmentsFirst;
			}
			else if(position == 1){
				fragmentsSecond = new  PlaceholderFragment(1);
				return fragmentsSecond;
			}
			else if(position == 2){
				fragmentsThird = new  PlaceholderFragment(2);
				return fragmentsThird;
			}
			else return new PlaceholderFragment(position);
		}
		@Override
		public int getCount() {
			return 3; 
		}
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0|1|2:
				return "";
			}
			return "";
		}
	}

	public class listener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int item) {
		}

		@Override 
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			positionView = position;
			positionOffsetView = positionOffset;
			positionOffsetPixelView = positionOffsetPixels;
		}

		@Override
		public void onPageSelected(int item) {
		}

	}
	public static class PlaceholderFragment extends Fragment {
		Activity activity;
		TextView text_simple ;
		TextView text_welcome ;
		TextView text_easy;
		TextView text_start;
		EditText host;
		EditText port;
		boolean active;
		int index;
		RippleBackground rippleBackground ;
		public PlaceholderFragment(int index) {
			this.index = index;
		} 

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
			super.onCreateView(inflater, container, savedInstanceState);
			if(index == 0){
				View rootView = inflater.inflate(R.layout.first_fragment_main, container, false);
				text_simple = (TextView) rootView.findViewById(R.id.text_simple);
				text_welcome = (TextView) rootView.findViewById(R.id.text_welcome);
				//				text_welcome.setTypeface(myTypeface);
				//				text_simple.setTypeface(myTypeface);
				text_simple.setVisibility(View.GONE); 
				new anim().execute("");
				return rootView;
			}
			else if(index == 1) {
				View rootView = inflater.inflate(R.layout.first_fragment_step, container, false);
				text_easy = (TextView) rootView.findViewById(R.id.text_easy);
				host = (EditText) rootView.findViewById(R.id.host);
				port = (EditText) rootView.findViewById(R.id.port);
				HelperActivity.port = "8080";
				//				port.setText("8080");
				port.addTextChangedListener(new TextWatcher(){
					@Override
					public void afterTextChanged(Editable s) {
						HelperActivity.port = s.toString();
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after){}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count){}
				}); 
				host.addTextChangedListener(new TextWatcher(){
					@Override
					public void afterTextChanged(Editable s) {
						HelperActivity.host = s.toString();
					}
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after){}
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count){}
				}); 
				return rootView;
			}
			else{
				View rootView = inflater.inflate(R.layout.first_fragment_last, container, false);
				activity = getActivity();
				text_start = (TextView) rootView.findViewById(R.id.text_start);
				rippleBackground = (RippleBackground)rootView.findViewById(R.id.content);
				ImageView button=(ImageView)rootView.findViewById(R.id.centerImage);
				button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if(!active){
							rippleBackground.startRippleAnimation();
							active = true;
							YoYo.with(Techniques.Shake).duration(400).playOn(text_start);
							text_start.setText(getResources().getString(R.string.Connecting));
							if( HelperActivity.host != null && !  HelperActivity.host.contentEquals("")){
								new RestRequestAsyncTask().execute( HelperActivity.host+":"+HelperActivity.port);
							}
							else {
								Toast.makeText(activity, getResources().getString(R.string.invalid_host_address), Toast.LENGTH_LONG).show();
								rippleBackground.stopRippleAnimation();
							}
						}
						else{
							text_start.setText(getResources().getString(R.string.Connect_Now));
							active = false;
							YoYo.with(Techniques.Shake).duration(400).playOn(text_start);
							rippleBackground.stopRippleAnimation();
						}
					}
				});

				return rootView;
			}

		}

		public void foundDevice(View obj){
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.setDuration(400);
			animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
			ArrayList<Animator> animatorList=new ArrayList<Animator>();
			ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(obj, "ScaleX", 0f, 1.2f, 1f);
			animatorList.add(scaleXAnimator);
			ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(obj, "ScaleY", 0f, 1.2f, 1f);
			animatorList.add(scaleYAnimator);
			animatorSet.playTogether(animatorList);
			animatorSet.start();
		}

		@Override
		public void onResume(){
			super.onResume();
		}

		public class RestRequestAsyncTask extends AsyncTask<String, String, String> implements Rest{

			@Override
			protected void onPreExecute() {

			}
			@Override
			protected void onPostExecute(String param) {
				if(param.contentEquals("error")){
					active = false;
					Toast.makeText(activity, getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
					text_start.setText(getResources().getString(R.string. Connect_Now));
					rippleBackground.stopRippleAnimation();
				}
				else if(param.contentEquals("connected")){
					Preferences.putString(activity,"host", HelperActivity.host);
					Preferences.putString(activity,"port", HelperActivity.port);
					Preferences.putString(activity,"hostext", HelperActivity.host+":"+HelperActivity.port);

					Intent intent = new Intent(activity, MainActivity.class);
					startActivity(intent); 
					activity.finish();
				}
			}
			@Override
			protected String doInBackground(String... parames) {
				try {
					HttpGet uri = new HttpGet("http://"+parames[0] + configuration);    
					HttpParams httpParameters = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
					HttpConnectionParams.setSoTimeout(httpParameters, 3000);
					DefaultHttpClient client = new DefaultHttpClient(httpParameters); 
					uri.setHeader("Accept", "application/json");
					HttpResponse resp = client.execute(uri);
					StatusLine status = resp.getStatusLine();
					if (status.getStatusCode() != 200) {
						Log.d("Schema app", "HTTP error, invalid server status code: " + resp.getStatusLine());  
						return "error";
					}
					else return "connected";
				} catch (  Exception  e) {
					e.printStackTrace();
					return "error";
				}

			}

		}

		class anim extends AsyncTask<String, String, String>{
			@Override
			protected String doInBackground(String... params) {

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						foundDevice(text_welcome);
						text_simple.setVisibility(View.VISIBLE);
					}
				});
				try { 
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						YoYo.with(Techniques.DropOut)
						.duration(1200)
						.interpolate(new AccelerateInterpolator(1.2f))
						.playOn(text_simple);

					}
				});
				return null;
			}

		}
		@Override
		public void onStart(){
			super.onStart();
		}
	}

}

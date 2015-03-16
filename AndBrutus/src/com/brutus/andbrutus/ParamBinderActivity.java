package com.brutus.andbrutus;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.brutus.andbrutus.adapter.AdapterBinder;
import com.brutus.andbrutus.utils.SystemBarTintManager;
import com.brutus.andbrutus.view.SimpleAnimation;
import com.brutus.base.Parameters;

public class ParamBinderActivity extends ActionBarActivity implements OnRefreshListener{
	Parameters list;
	SwipeRefreshLayout swipeLayout;
	AdapterBinder  mAdapter;
	RecyclerView recyclerView ;
	Toolbar toolbar;
	@Override	
	protected void onCreate(Bundle savedInstanceState) {

		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		//		Transition ts = new android.transition.Fade();  //Slide(); //Explode(); //Fade();
		//		ts.setDuration(1500);
		//		getWindow().setSharedElementEnterTransition(ts);
		//		getWindow().setSharedElementExitTransition(ts);
		//		getWindow().setExitTransition(new Slide(Gravity.LEFT));
		//		getWindow().setEnterTransition(new Slide(Gravity.LEFT));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binder);
		list = (Parameters) getIntent().getExtras().getSerializable("list");
		initToolBar();
		initRecycleView();
		recyclerView.setVisibility(View.GONE);
		initPullToRefresh();
		new SimpleAnimation().createCircularRevealAnimation(recyclerView, 500);
	}


	public void endActivity(){
		new SimpleAnimation().destroyCircularRevealAnimation(recyclerView, 0);
		finishAfterTransition();
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		endActivity();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		endActivity();
		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("deprecation")
	public void initPullToRefresh(){
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, 
				android.R.color.holo_orange_light, 
				android.R.color.holo_red_light);

	}	

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		//		toolbar.setTitle("AndBrutus");
		toolbar.setTitle("Customize parameters");
		if (toolbar != null) { 
			setSupportActionBar(toolbar);    
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		toolbar.setOnMenuItemClickListener(
				new Toolbar.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						finish();
						return false;
					}
				}
				);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.light_blue_700);
	}

	public void initRecycleView(){
		recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//		LinearLayoutManager   mLayoutManager = new LinearLayoutManager(this);
//		recyclerView.setLayoutManager(mLayoutManager);
		mAdapter = new AdapterBinder(this,list);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(new GridLayoutManager(this,2));
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
	}





	@Override
	public void onRefresh() {
		Toast.makeText(this, "Refreshed Parameter Configuration", Toast.LENGTH_SHORT).show();
		mAdapter.notifyDataSetChanged();
		swipeLayout.setRefreshing(false);
		//		new Handler().postDelayed(new Runnable() {
		//			@Override public void run() {
		//				swipeLayout.setRefreshing(false);
		//			}
		//		}, 3000);
	}
}

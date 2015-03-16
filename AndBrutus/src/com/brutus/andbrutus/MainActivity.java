package com.brutus.andbrutus;
import java.util.ArrayList;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.brutus.andbrutus.adapter.Adapter;
import com.brutus.andbrutus.task.RestConfigRequestAsyncTask;
import com.brutus.andbrutus.utils.MaskMap;
import com.brutus.andbrutus.utils.Preferences;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.andbrutus.utils.SystemBarTintManager;
import com.brutus.andbrutus.view.FloatingActionButton;
import com.brutus.base.ParamExp;
import com.brutus.base.Parameters;
import com.gc.materialdesign.widgets.SnackBar;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends ActionBarActivity implements Rest,OnRefreshListener{
	public static Preferences preference;  
	SwipeRefreshLayout swipeLayout;
	RecyclerView recyclerView ;   
	Adapter   mAdapter ; 
	Toolbar toolbar; 
	DrawerLayout drawerLeft;
	Parameters data = new Parameters();
	Parameters temp = new Parameters();
	ListView drawerLeftListView;
	FloatingActionButton showConnected; 
	SnackBar snack;
	public static boolean active = false;
	//DA AGGIUNGERE SCELTA RAPIDA FRAGMENT E LATERAL PANEL 

	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
		//getWindow().setExitTransition(new Slide(Gravity.RIGHT));
		getWindow().setEnterTransition(new Slide(Gravity.LEFT));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main); 
		preference = new Preferences(this);
		new MaskMap(getApplicationInfo().dataDir+"/"+ dbName);//set db folder in MapDb
		initToolBar(); 
		((ViewGroup) toolbar.getParent()).setTransitionGroup( false );
		initPullToRefresh(); 
		initRecycleView();	
		initDrawerLeft();
		if(isFirstTime()){  
			Intent intent = new Intent(this, HelperActivity.class);
			startActivity(intent);  
			finishAfterTransition();
		}
		else{
			checkIdKey();
			//new RestRequestAsyncTask().execute(configuration);
		}
	}

	public void stopNotificationService(){
		stopService(new Intent(this, NotificationService.class));
	}

	public void startNotificationService(){
		NotificationService.handler = handler;
		Intent service = new Intent(this, NotificationService.class);
		startService(service);
	}

	public void checkIdKey(){ 
		if(GCMRegistrar.getRegistrationId(this) == null || GCMRegistrar.getRegistrationId(this).contentEquals("")){
			GCMRegistrar.register(this, "681925213640");
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
		}
	}
	

	public boolean isFirstTime(){
		if(Preferences.getString(this, "host") == null || Preferences.getString(this, "host").contentEquals("") ||
				Preferences.getString(this, "port") == null || Preferences.getString(this, "port").contentEquals("")){
			return true;
		} 
		else return false;
	}

	public void initDrawerLeft(){
		drawerLeft = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerLeftListView = (ListView) findViewById(R.id.left_drawer);
		//drawerLeft.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		String[] array = new String[]{"Mostra Contatori","Mostra Bottoni"};
		drawerLeftListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, array));
		drawerLeftListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position,view);	
			}
		});
		ActionBarDrawerToggle  actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLeft, R.string.app_name, R.string.app_name);
		drawerLeft.setDrawerListener(actionBarDrawerToggle);

	}


	private void selectItem(int position,View view) {
		Toast.makeText(this, "You click element "+position, Toast.LENGTH_SHORT).show();
		//listView.setItemChecked(position, true);
		drawerLeft.closeDrawer(drawerLeftListView);
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finishAfterTransition();
	}

	public void initToolBar() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);   
		getSupportActionBar().setDisplayUseLogoEnabled(true);
		toolbar.setOnMenuItemClickListener(
				new Toolbar.OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						int id = item.getItemId();
						if(id == R.id.action_edit){
							Intent paramBinder = new Intent(getApplicationContext(), ParamBinderActivity.class);
							paramBinder.putExtra("list", data);
							startActivity(paramBinder, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,toolbar,"toolbar").toBundle());
							return true; 
						}
						if(id == R.id.action_option){
							Intent paramBinder = new Intent(getApplicationContext(), OptionActivity.class);
							startActivity(paramBinder, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,toolbar,"toolbar").toBundle());
							return true;
						}
						else return true;
					}
				});

		toolbar.setNavigationIcon(R.drawable.abc_ic_menu_cut_mtrl_alpha);
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.light_blue_700);

		//bottom toolbar
		//		showConnected = (FloatingActionButton) findViewById(R.id.floatbutton);
		//		showConnected.setOnClickListener(new OnClickListener() {
		//			@Override
		//			public void onClick(View v) {
		//				if(Preferences.getBoolean("showConnected")){
		//					Preferences.putBoolean("showConnected",false);
		//				}
		//				else Preferences.putBoolean("showConnected",true);
		//			}
		//		});
	}

	public void initRecycleView(){
		recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		LinearLayoutManager   mLayoutManager = new LinearLayoutManager(this);
		//StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL); // (int spanCount, int orientation)
		temp = new Parameters();
		temp.setParam(new ArrayList<ParamExp>());
		mAdapter = new Adapter(this,temp);
		recyclerView.setAdapter(mAdapter);
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	//Handle recive message from service instance
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			if(msg.obj != null){
				if(msg.obj instanceof Parameters){
					data = (Parameters) msg.obj;
					if(data != null){
						temp.getParam().clear();
						for(int k = 0; k < data.getParam().size(); k++){
							if(!Preferences.getBoolean(data.getParam().get(k).getTag()+_disable)){
								if(Preferences.getBoolean("showConnected")){
									if(data.getParam().get(k).getQuality() != 0)
										temp.getParam().add(data.getParam().get(k));
								}
								else
									temp.getParam().add(data.getParam().get(k));
							}
						}
						mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
					}
				}
				else if(msg.obj instanceof String){
					MainActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(snack == null || !snack.isShowing() && MainActivity.active){
								snack = new SnackBar(	MainActivity.this,"Connection problem! Slide to refresh","Ok",new OnClickListener() 
								{@Override public void onClick(View v) {snack.dismiss();}});
								snack.setBackgroundSnackBar(MainActivity.this.getResources().getColor(R.color.blue_500));
								snack.setCancelable(false);
								snack.setIndeterminate(true);
								snack.setColorButton(Color.WHITE);
								snack.show();
							}
						}
					});	
				}
			}
			return true;
		}
	});

	@Override
	public void onRefresh() {
		new RestConfigRequestAsyncTask(this,swipeLayout,handler).execute(read);

	}

	@Override
	public void setTitle(CharSequence title) {
		getSupportActionBar().setTitle(title);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		//	    actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		//	    actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public void onStart() {
		super.onStart();
		active = true;
		if(Preferences.getBoolean("notificationEnable"))
			stopNotificationService(); 
		startNotificationService();
	} 

//	@Override
//	public void onResume() {
//		super.onResume();
//		active = true;
//		if(Preferences.getBoolean("notificationEnable"))
//			stopNotificationService(); 
//		startNotificationService();
//	} 
//	@Override
//	public void onPause() {
//		super.onPause();
//		active = false;
//		if(!Preferences.getBoolean("notificationEnable"))
//			stopNotificationService(); 
//	}
	
	@Override
	public void onStop() {
		super.onStop();
		active = false;
		if(!Preferences.getBoolean("notificationEnable"))
			stopNotificationService(); 
	}
	

	@SuppressWarnings("deprecation")
	public void initPullToRefresh(){
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
	}	

}

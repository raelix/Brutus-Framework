package com.brutus.andbrutus.details;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.brutus.andbrutus.R;
import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.Rest;

public class DetailButtonActivity extends BaseDetailActivity implements OnClickListener{

	Button value;
	int result;

	@Override   
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_button_base); 
		value = (Button) findViewById(R.id.valueDef);
		value.setOnClickListener(this);
		init();

	}

	@Override
	public void update(int result){
		this.result = result;
		if(result == 1){
			value.setTag(1);
			value.setText(getResources().getString(R.string.On));
			desc.setText(getResources().getString(R.string.status_On));
		}
		else{
			value.setTag(0);
			value.setText(getResources().getString(R.string.Off));
			desc.setText(getResources().getString(R.string.status_Off));
		}
	}

	@Override
	View getCustomView() {
		return value;
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(v == value){
			if(result == 1) 
//				new RestHtmlRequestAsyncTask().execute(Rest.write+
//						"?&tag="+param.getTag()+
//						"&value=0");
			new Thread(new RestHtmlRunnable(Rest.write+
					"?&tag="+param.getTag()+
					"&value=0")).start();
			else
				new Thread(new RestHtmlRunnable(Rest.write+
						"?&tag="+param.getTag()+
						"&value=1")).start();
		}
	}

}

package com.brutus.andbrutus;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.brutus.andbrutus.task.RestHtmlRunnable;
import com.brutus.andbrutus.utils.Rest;
import com.brutus.base.ParamExp;
import com.brutus.base.Parameters;

public  class NotificationButtonReciver extends BroadcastReceiver  implements Rest{
	@Override
	public void onReceive(Context context, Intent intent) {
		ParamExp param = (ParamExp) intent.getSerializableExtra("param");
		Parameters params = (Parameters) intent.getSerializableExtra("params");
		Double switchMode = (Double) intent.getDoubleExtra("switch",-1);
		if (switchMode == 0){//Turn on
			params.getParamByTagName(param.getTag()).setValue(1d);
			param.setValue(1);
//			new RestHtmlRequestAsyncTask().execute(Rest.write+
//					"?&tag="+param.getTag()+
//					"&value="+param.getValue());
			new Thread(new RestHtmlRunnable(Rest.write+
					"?&tag="+param.getTag()+
					"&value="+param.getValue())).start();
//			new RestPostRequestAsyncTask(new Parameters(param),null).execute(write);
		}
		 else if(switchMode > 0){//Turn off
				params.getParamByTagName(param.getTag()).setValue(0d);
				param.setValue(0);
//				new RestHtmlRequestAsyncTask().execute(Rest.write+
//						"?&tag="+param.getTag()+
//						"&value="+param.getValue());
				new Thread(new RestHtmlRunnable(Rest.write+
						"?&tag="+param.getTag()+
						"&value="+param.getValue())).start();
//				new RestPostRequestAsyncTask(new Parameters(param),null).execute(write);
		 }
//		 new CustomNotification(context,true).refreshNotification(params);
		Log.d("TAG", "test"+intent.getAction()+" nome: "+param.getTag());
	}

}
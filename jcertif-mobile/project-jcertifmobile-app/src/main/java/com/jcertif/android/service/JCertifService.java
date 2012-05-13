package com.jcertif.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * The Service is needed to do background task, getting data from Web services.  
 * The business code is in the Binder.
 *  
 * @author Yakhya DABO
 *
 */
public class JCertifService<T> extends Service {

	private LocalServiceBinder<T> binder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(this.getClass().getSimpleName(),"on create");
		binder = new LocalServiceBinder<T>(this);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.i(this.getClass().getSimpleName(),"binding ...");
		return binder;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		binder.onDestroy();		
	}

}

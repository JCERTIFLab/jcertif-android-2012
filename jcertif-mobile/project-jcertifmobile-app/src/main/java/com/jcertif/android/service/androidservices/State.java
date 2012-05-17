package com.jcertif.android.service.androidservices;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * This class is used to save the configuration of the view when it's getting destroyed
 * or going to change configuration. It's also used as a bridge between Activities 
 * and Services to abstract the calls to web services through the Service binder.
 * It's created and instantiated in the Activity
 * 
 * @author yakhya.dabo
 *
 * @param <T>
 */
public abstract class State<T> {
	/**
	 * The binder 
	 */
	private LocalServiceBinder<T> binder = null;
	private StateListener<T> listener = null;
	private Operation operation = Operation.LOAD;
	private boolean showProgressDialog=true;
	
	private ServiceConnection svcConn = new ServiceConnection() {
		
		@SuppressWarnings("unchecked")
		public void onServiceConnected(ComponentName className,IBinder rawBinder) {
			Log.i(State.this.getClass().getSimpleName(), "State : connecting ...");
			setBinder((LocalServiceBinder<T>)rawBinder);						
			listener.onServiceConnected();			
		}

		public void onServiceDisconnected(ComponentName className) {
			setBinder(null);
		}
	};
	
	public void attach(StateListener<T> listener) {
		Log.i(this.getClass().getSimpleName(), "State : attaching activity's adapter ...");
		this.listener = listener;
	}

	public ServiceConnection getConn() {
		return svcConn;
	}

	public void setBinder(LocalServiceBinder<T> binder) {
		Log.i(this.getClass().getSimpleName(),"State : binding ..." + binder);	
		this.binder = binder;			
	}

	public LocalServiceBinder<T> getBinder() {
		return binder;
	}

	public StateListener<T> getStateListener() {
		return listener;
	}
	
	public void setShowProgressDialog(boolean value){
		this.showProgressDialog=value;
	}
	
	public boolean isShowProgressDialog(){
		return showProgressDialog;
	}

	public abstract T getData() throws Exception;
	
	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}

	public static enum Operation{
		LOAD,ADD,REMOVE
	}
}

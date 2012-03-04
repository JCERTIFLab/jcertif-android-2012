package com.jcertif.android.service;

import com.jcertif.android.view.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public abstract class StateAdapter<T> implements StateListener<T> {
	protected Activity context;
	
	public StateAdapter(){
		
	}
	
	public StateAdapter(Activity context){
		this.context = context;
	}
	

	public void onDataAvailable(T data) {
		
	}	
	

	public void onServiceConnected() {

		
	}

	public void onDataChangeSuccess(String param) {
		
	}

	public void setContext(Activity context) {
		this.context = context;
	}
	
	public void onError(Throwable t) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.alertDialogTitle)
				.setMessage(getMessageToDisplay(t.getMessage().trim()))
				.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
				      		public void onClick(DialogInterface dialog, int which) {
				      			context.onBackPressed();	    				    			
			      } })    			
				.show();    	
    }	
	
	protected int getMessageToDisplay(String message){
		return 0;
//		if ("NO_PROJECT".equalsIgnoreCase(message)){
//			return R.string.noProjectErrorMessage;
//		}else if ("UNKNOWN_USER".equalsIgnoreCase(message)){
//			return R.string.unknownUserError;
//		}else if ("NO_ESTABLISHMENT".equalsIgnoreCase(message)){
//			return R.string.noEstablishmentErrorMessage;
//		}else{
//			return R.string.unknownError;
//		}
//			
	}
}

package com.jcertif.android.service.androidservices;

import android.app.Activity;

/**
 * This interface is implemented by activities that need to display data
 * 
 * @author yakhya.dabo
 *
 * @param <T> Data type to display. It can be a simple DTO or a list of DTO
 */
public interface StateListener<T> {
	/**
	 * This method is called when data from Web Service are available 
	 *  to be displayed on the UI components
	 * @param data Data from the Web Service 
	 */
	public void onDataAvailable(T data);
	
	/**
	 * This method is called when the binding of the Service is established 
	 */
	public void onServiceConnected();
	
	/**
	 * This method is called when an exception happens. We have tree types of exception :
	 * - "No Available data" Exception
	 * - "Not authorized" Exception
	 * - "Unknown user " Exception
	 * @param t Throwable
	 */
	public void onError(Throwable t);
	
	/**
	 * 
	 * @param param
	 */
	public void onDataChangeSuccess(String param);
	
	/**
	 * 
	 * @param context
	 */
	public void setContext(Activity context);
	
}

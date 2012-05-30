/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.threadtraitements.registration</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 */
package com.jcertif.android.service.threadtraitements.registration;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.service.threadtraitements.generic.BasicBackgroundCallBack;
import com.jcertif.android.service.threadtraitements.generic.BasicBackgroundThread;

/**
 * @author Mathias Seguy (Android2EE)
 * @author Yakhya DABO 
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class RegistrationThread extends BasicBackgroundThread {
	

	/**
	 * @param mCallBack
	 */
	public RegistrationThread(RegistrationThreadCallBack mCallBack) {
		super(mCallBack);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.service.threadtraitements.generic.BasicBackgroundThread#workingMethod
	 * (android.os.Handler)
	 */
	@Override
	public void workingMethod(Handler handler) {
		// TODO Yakhya Pas de parametre ? J'ai raté quelque chose ou il manque un truc ?
		String responseString = null;
		int responseStatus=STATUS_NOTSET;
		String url = JCApplication.getInstance().getUrlFactory().getRegisterUrl();
		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.POST);
			responseString = client.getResponse();
			responseStatus=client.getResponseCode();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
		}
		Message mess = handler.obtainMessage();
		Bundle data = new Bundle();
		data.putString(RESPONSE_KEY, responseString);
		data.putInt(STATUS_KEY, responseStatus);
		mess.setData(data);
		handler.sendMessage(mess);
	}

}

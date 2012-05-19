/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.threadtraitements</li>
 * <li>19 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.jcertif.android.service.threadtraitements.authent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.service.threadtraitements.BasicBackgroundThread;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class AuthentificationThread extends BasicBackgroundThread {
	/******************************************************************************************/
	/** Constant **************************************************************************/
	/******************************************************************************************/

	/**
	 * The key to use to pass the email as parameter in the Bundle
	 */
	public static final String EMAIL_KEY="email";
	/**
	 * The key to use to pass the password as parameter in the Bundle
	 */
	public static final String PASSWORD_KEY="email";
	/**
	 * The key to use to retrieve the response
	 */
	public static final String RESPONSE_KEY="response";
	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * Constructor
	 */
	public AuthentificationThread(AuthentThreadCallBack callBack) {
		super(callBack);
	}

	
	/******************************************************************************************/
	/** Implementation of BasicBackgroundThread ***********************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.threadtraitements.BasicBackgroundThread#workingMethod()
	 */
	@Override
	public void workingMethod(Handler handler) {
		// instantiate the values
		String responseString = null;
		String email = params.getString(EMAIL_KEY);
		String password = params.getString(PASSWORD_KEY);
		Log.w(this.getClass().getSimpleName(), "Trying to authenticate user" + email + "," + password);
		// Define the URL
		StringBuilder url = new StringBuilder(JCApplication.getInstance().getUrlFactory().getAuthenticationUrl());
		url.append("/");
		url.append(email);
		url.append("/");
		url.append(password);
		url.append("/2");
		// Instantiate the client
		RestClient client = new RestClient(url.toString());
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), "The answer from RestService is" + responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
		}
		Message mess=handler.obtainMessage();
		Bundle data=new Bundle();
		data.putString(responseString, responseString);
		mess.setData(data);
		handler.sendMessage(mess);
	}
}

/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.transverse.url</li>
 * <li>19 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
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
package com.jcertif.android.transverse.url;

import android.content.Context;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to define getters to retrieve the Url of the application
 */
public class UrlFactory {
	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * Base URL
	 */
	private String baseUrl;
	/**
	 * Base URL for picture
	 */
	private String basePictureUrl;
	/**
	 * URL for speaker
	 */
	private String speakerUrl;
	/**
	 * Url for event
	 */
	private String eventUrl;
	/**
	 * Url for authentification
	 */
	private String authenticationUrl;
	/**
	 * Url for registration
	 */
	private String registerUrl;
	/**
	 * The Application object
	 */
	private JCApplication jcApplication;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/
	/**
	 * Constructor
	 * 
	 * @param jCApplication
	 *            The Application
	 */
	public UrlFactory(JCApplication jCApplication) {
		super();
		jcApplication = jCApplication;
		Context ctx = jCApplication.getApplicationContext();
		baseUrl = ctx.getString(R.string.url_base);
		basePictureUrl = ctx.getString(R.string.url_base_picture);
		speakerUrl = baseUrl + ctx.getString(R.string.url_suffixe_speaker);
		eventUrl = baseUrl + ctx.getString(R.string.url_suffixe_event);
	}

	/******************************************************************************************/
	/** GETTERS **************************************************************************/
	/******************************************************************************************/

	/**
	 * @return the baseUrl
	 */
	public final String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @return the basePictureUrl
	 */
	public final String getBasePictureUrl() {
		return basePictureUrl;
	}

	/**
	 * @return the speakerUrl
	 */
	public final String getSpeakerUrl() {
		return speakerUrl;
	}

	/**
	 * @return the eventUrl
	 */
	public final String getEventUrl() {
		return eventUrl;
	}

	/**
	 * @return the authenticationUrl
	 */
	public final String getAuthenticationUrl(String email, String password) {
		Context ctx = jcApplication.getApplicationContext();
		authenticationUrl = baseUrl
				+ String.format(ctx.getString(R.string.url_suffixe_authentification), email, password);
		return authenticationUrl;
	}

	/**
	 * @return the registerUrl
	 */
	public final String getRegisterUrl() {
		Context ctx = jcApplication.getApplicationContext();
		registerUrl = baseUrl + ctx.getString(R.string.url_suffixe_register);

		return registerUrl;
	}
	
	/**
	 * @return the list of stared events of the user
	 */
	public final String getGetStaredEventsURL(String email) {
		Context ctx = jcApplication.getApplicationContext();
		return baseUrl
				+ String.format(ctx.getString(R.string.url_get_stared_event), email);
		
	}
	
	/**
	 * @return the list of stared events of the user
	 */
	public final String getAddStaredEventURL(String email,String eventId) {
		Context ctx = jcApplication.getApplicationContext();
		return baseUrl
				+ String.format(ctx.getString(R.string.url_add_stared_event),eventId, email);
	}
	
	/**
	 * @return the list of stared events of the user
	 */
	public final String getRemoveStaredEventURL(String email,String eventId) {
		Context ctx = jcApplication.getApplicationContext();
		return baseUrl
				+ String.format(ctx.getString(R.string.url_remove_stared_event),eventId, email);
	}

}

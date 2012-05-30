package com.jcertif.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jcertif.android.transverse.model.User;
import com.jcertif.android.transverse.url.UrlFactory;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy
 */
public class JCApplication extends Application {

	/**
	 * The url factory to access to url
	 */
	private UrlFactory urlFactory;
	/**
	 * The User that currently uses the application
	 */
	private User user;

	/******************************************************************************************/
	/** Access Every Where **************************************************************************/
	/******************************************************************************************/
	/**
	 * instance of this
	 */
	private static JCApplication instance;
	/**
	 * @return The instance of the application
	 */
	public static JCApplication getInstance() {
		return instance;
	}

	/******************************************************************************************/
	/** Managing LifeCycle **************************************************************************/
	/******************************************************************************************/

	@Override
	public void onCreate() {
		super.onCreate();
		initializeUser();
		instance = this;
		urlFactory = new UrlFactory(this);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
	/******************************************************************************************/
	/** URL **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the urlFactory
	 */
	public final UrlFactory getUrlFactory() {
		return urlFactory;
	}

	/******************************************************************************************/
	/** User **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the user
	 */
	public final User getUser() {
		return user;
	}

	/**
	 * This method load the last user profile that used the application
	 * 
	 * @param user
	 *            The user to store
	 */
	private void initializeUser() {
		// instantiate the user
		user = new User();
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Load user's informations
		// to enable multi-users we add the user hash code to the key
		// Load the hashcode of the last user connected
		final String hash = preferences.getString(getString(R.string.shhash), "");
		userUpdate(preferences, hash);
	}

	/**
	 * This method update the user profile that used the application
	 * Using the DefaultSharedPreference
	 */
	public void updateUser(int hashCode) {
		// instantiate the user
		user = new User();
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Load user's informations
		// to enable multi-users we add the user hash code to the key
		// Load the hashcode of the last user connected
		final String hash = Integer.toString(hashCode);
		// set the user according to its last known attributes
		userUpdate(preferences, hash);
	}

	/**
	 * @param preferences
	 * @param hash
	 */
	private void userUpdate(SharedPreferences preferences, final String hash) {
		user.setCivilite(preferences.getString(hash + getString(R.string.shCivilite), ""));
		user.setCompagnie(preferences.getString(hash + getString(R.string.shCompagnie), ""));
		user.setNom(preferences.getString(hash + getString(R.string.shNom), ""));
		user.setPays(preferences.getString(hash + getString(R.string.shPays), ""));
		user.setPrenom(preferences.getString(hash + getString(R.string.shPrenom), ""));
		user.setRole(preferences.getString(hash + getString(R.string.shRole), ""));
		user.setSiteWeb(preferences.getString(hash + getString(R.string.shSiteweb), ""));
		user.setTelFixe(preferences.getString(hash + getString(R.string.shTelFixe), ""));
		user.setTelMobile(preferences.getString(hash + getString(R.string.shTelMobile), ""));
		user.setType(preferences.getString(hash + getString(R.string.shType), ""));
		user.setVille(preferences.getString(hash + getString(R.string.shVille), ""));
		user.setEmail(preferences.getString(hash + getString(R.string.shEmail), ""));
		user.setPassword(preferences.getString(hash + getString(R.string.shPassword), ""));
	}

	/**
	 * To know if the user is still valid
	 * 
	 * @param email
	 *            user's email
	 * @param password
	 *            user's password
	 * @return if this user is valid
	 */
	public boolean isValidUser() {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Build the email and password hashcode
		int hash = (user.getEmail().hashCode() + user.getPassword().hashCode()) / 2;
		return preferences.getBoolean(hash + getString(R.string.shValidUser), false);
	}

}

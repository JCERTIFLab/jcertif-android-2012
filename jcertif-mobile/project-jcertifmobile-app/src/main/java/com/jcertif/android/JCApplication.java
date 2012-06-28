package com.jcertif.android;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.jcertif.android.transverse.model.User;
import com.jcertif.android.transverse.url.UrlFactory;
import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.generic.BaseActivityIntf;
import com.jcertif.android.ui.view.generic.BaseActivityLegacy;
import com.jcertif.android.ui.view.main.FragmentsSwitcherLegacy;

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
	/**
	 * The one that as in charge the fragment switching and displays
	 */
	FragmentsSwitcherLegacy fragmentSwitcher;
	/**
	 * The baseActivity to manage top and bottom bar
	 */
	BaseActivityIntf baseActivity;
	/**
	 * The boolean to know if the ServiceUpdater is updating
	 */
	private Boolean isServiceUpdaterUpdating = false;

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
	 * @param user
	 *            the user to set
	 */
	public final void setUser(User user) {
		this.user = user;
		saveUser();
	}

	/**
	 * This method reset the default user
	 * No Default user are known after that call
	 */
	public void clearDefaultUser() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();
		// to reload the email/password of the last connected person
		editor.putString(getString(R.string.shhash), "0");
		// And commit
		editor.commit();
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
		user.id = preferences.getInt(hash + getString(R.string.shID), -1);
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
	 * This method save the user profile within the defaultSharedPreference
	 * TODO Async||Thread
	 * it should write to the disk using a thread (asyncTask for example)
	 * 
	 * @param user
	 *            The user to store
	 */
	private void saveUser() {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();
		// Store user's informations
		// to enable multi-users we add the user hash code to the key
		// int hash =(email.hashcode+password.hashcode)/2.
		int hash = (user.getEmail().hashCode() + user.getPassword().hashCode()) / 2;
		editor.putInt(hash + getString(R.string.shID), user.id);
		editor.putString(hash + getString(R.string.shCivilite), user.getCivilite());
		editor.putString(hash + getString(R.string.shCompagnie), user.getCompagnie());
		editor.putString(hash + getString(R.string.shNom), user.getNom());
		editor.putString(hash + getString(R.string.shPays), user.getPays());
		editor.putString(hash + getString(R.string.shPrenom), user.getPrenom());
		editor.putString(hash + getString(R.string.shRole), user.getRole());
		editor.putString(hash + getString(R.string.shSiteweb), user.getSiteWeb());
		editor.putString(hash + getString(R.string.shTelFixe), user.getTelFixe());
		editor.putString(hash + getString(R.string.shTelMobile), user.getTelMobile());
		editor.putString(hash + getString(R.string.shType), user.getType());
		editor.putString(hash + getString(R.string.shVille), user.getVille());
		// Store the user password and email
		editor.putString(hash + getString(R.string.shEmail), user.getEmail());
		editor.putString(hash + getString(R.string.shPassword), user.getPassword());
		// Store validity
		editor.putBoolean(hash + getString(R.string.shValidUser), true);
		// and store the hashcode of the last user connected
		// to reload the email/password of the last connected person
		editor.putString(getString(R.string.shhash), Integer.toString(hash));
		// And commit
		editor.commit();

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

	/**
	 * To know if the default user is still valid
	 * 
	 * @return if this default user is valid
	 */
	public boolean isDefaultValidUser() {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Build the email and password hashcode of the default user
		String hash = preferences.getString(getString(R.string.shhash), "");
		return preferences.getBoolean(hash + getString(R.string.shValidUser), false);
	}

	/******************************************************************************************/
	/** Fragment Management **************************************************************************/
	/******************************************************************************************/
	/**
	 * @return the fragmentSwitcher
	 */
	public final FragmentsSwitcherLegacy initialise(FragmentActivity activity, Boolean twoFragmentsVisible,
			Boolean recreationMode) {
		if (fragmentSwitcher == null) {
			fragmentSwitcher = new FragmentsSwitcherLegacy(activity, twoFragmentsVisible);
		} else {
			fragmentSwitcher.setMainActivity(activity);
			fragmentSwitcher.setTwoFragmentsVisible(twoFragmentsVisible);
		}
		fragmentSwitcher.showMain(recreationMode);
		return fragmentSwitcher;
	}

	/******************************************************************************************/
	/** Managing the base Activity **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param baseActivity
	 *            the baseActivity to set
	 */
	public final void setBaseActivity(BaseActivityIntf baseActivity) {
		this.baseActivity = baseActivity;
		//if the ServiceUpdater is updating then update GUI
		if(isServiceUpdaterUpdating) {
			onDataUpdate();
		}
	}

	/**
	 * This method will update the JCertif header displaying the progressBar for updaterService
	 * and hide the update icon
	 * Is Called by the ServiceUpdater
	 */
	public final void onDataUpdate() {
		isServiceUpdaterUpdating=true;
		if (null != baseActivity) {
			baseActivity.showRefreshingDataProgressBar(true);
		}
	}

	/**
	 * This method will update the JCertif header hiding the progressBar for updaterService
	 * and show the update icon
	 * Is Called by the ServiceUpdater
	 */
	public final void onDataUpdateOver() {
		isServiceUpdaterUpdating=false;
		if (null != baseActivity) {
			baseActivity.showRefreshingDataProgressBar(false);
		}
	}
}

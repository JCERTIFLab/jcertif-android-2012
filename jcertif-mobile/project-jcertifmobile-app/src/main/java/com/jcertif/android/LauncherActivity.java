/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android</li>
 * <li>18 mai 2012</li>
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
package com.jcertif.android;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.jcertif.android.service.androidservices.UpdaterService;
import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.connection.ConnectionActivityLegacy;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to Launch the application depending on the Android version installed on
 *        the device.
 *        There is a big gap between Post and Pre HoneyComb versions, according to fragments
 *        management.
 *        This is the reason of the existence of that class.
 *        It also launch the first load of the database when the application is first runs
 */
public class LauncherActivity extends Activity {
	/**
	 * The HoneyComb version level
	 */
	private static boolean shinyNewAPIS = android.os.Build.VERSION.SDK_INT >= 11;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = new Intent(this, UpdaterService.class);
		startService(intent);
		
		//First launch
		firstLaunchInit();
		//Then do the rest
		Intent startActivityIntent = null;

		// First test if the user exists
		if (((JCApplication) getApplication()).isValidUser()) {
			// if the user is valid, launch the mainActivity
			// TODO Update MainActivity to call it here using Legacy or HC
			if (!shinyNewAPIS) {
				startActivityIntent = new Intent(this, MainActivityLegacy.class);
			} else {
				startActivityIntent = new Intent(this, MainActivityLegacy.class);
			}
		} else {
			// else if there is no user, launch the Connection Activity
			//It doens't need HC compatibility
			startActivityIntent = new Intent(this, ConnectionActivityLegacy.class);
		}
		startActivity(startActivityIntent);
		finish();
		
	}

	/**
	 * Launch the first load of the database
	 */
	private void firstLaunchInit() {
		Log.w("firstLaunchInit","firstLaunchInit called");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Boolean init=preferences.getBoolean(getString(R.string.initialisation), false);
		if(!init) {
			Log.w("firstLaunchInit","Init Launched");
			//Launch the database first update
			Intent intent = new Intent(this, UpdaterService.class);
			startService(intent);
			//Store the init			
			Editor editor = preferences.edit();
			// Store initialisation
			editor.putBoolean(getString(R.string.initialisation), false);
			//and commit
			editor.commit();
		}
	}
}

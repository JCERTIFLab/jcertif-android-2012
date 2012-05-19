/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android</li>
 * <li>18 mai 2012</li>
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
package com.jcertif.android;

import com.jcertif.android.ui.connection.ConnectionActivityHC;
import com.jcertif.android.ui.connection.ConnectionActivityLegacy;
import com.jcertif.android.ui.view.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to Launch the application depending on the Android version installed on
 *        the device.
 *        There is a big gap between Post and Pre HoneyComb versions, according to fragments
 *        management.
 *        This is the reason of the existence of that class.
 */
public class LauncherActivity extends Activity {
	/**
	 * The HoneyComb version level
	 */
	private static boolean shinyNewAPIS = android.os.Build.VERSION.SDK_INT >= 11;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent startActivityIntent = null;
		
		// First test if the user exists
		if (((JCApplication) getApplication()).isValidUser()) {
			//if the user is valid, launch the mainActivity
			// TODO UpdateMainActivity to call it here using Legacy or HC
			if (!shinyNewAPIS) {
				startActivityIntent = new Intent(this, MainActivity.class);
			} else {
				startActivityIntent = new Intent(this, MainActivity.class);
			}
		} else {
			//else if there is no user, launch the Connection Activity
			if (!shinyNewAPIS) {
				startActivityIntent = new Intent(this, ConnectionActivityLegacy.class);
			} else {
				startActivityIntent = new Intent(this, ConnectionActivityHC.class);
			}
		}
		startActivity(startActivityIntent);
		finish();
	}

}

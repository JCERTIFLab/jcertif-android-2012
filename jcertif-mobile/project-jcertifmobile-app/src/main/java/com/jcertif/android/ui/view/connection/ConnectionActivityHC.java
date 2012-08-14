/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.connection</li>
 * <li>14 août 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.connection;

import com.jcertif.android.R;

import android.os.Bundle;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class ConnectionActivityHC extends ConnectionActivityLegacy {
	// Do othing it's just a trick for the manifest to change the theme
	// because <activity
	// android:name="com.jcertif.android.ui.view.connection.ConnectionActivityLegacy"
	// android:enabled="@bool/postHC"
	// android:theme="@style/Theme.Light.JCertif" />
	// don't work
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//change the action bar logo
				getActionBar().setLogo(getResources().getDrawable(R.drawable.logo));
	}
}

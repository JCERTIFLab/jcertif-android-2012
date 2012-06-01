/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.generic</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 */
package com.jcertif.android.ui.view.generic;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.jcertif.android.JCApplication;
import com.jcertif.android.LauncherActivity;
import com.jcertif.android.service.androidservices.UpdaterService;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        Gérer les menus
 *        Gérer la thread qui fait bouger les sponsors
 *        A faire
 */
public class BaseActivityLegacy extends FragmentActivity {

	/******************************************************************************************/
	/** Menu Management **************************************************************************/
	/******************************************************************************************/

	/**
	 * the Menu of the activity
	 */
	private Menu theMenu = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// instanciation du menu via le fichier XML
		new MenuInflater(getApplication()).inflate(R.menu.menu, menu);
		// the menu et menu sont le même objet
		theMenu = menu;

		// Création du menu comme avant
		return (super.onCreateOptionsMenu(menu));
	}

	// Instanciation de l'action associé à la selectionde l'item du menu
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// open browser on the web pages
		switch (item.getItemId()) {
		case R.id.menu_forceupdate:
			// Launch the database update
			Intent updateServiceIntent = new Intent(this, UpdaterService.class);
			startService(updateServiceIntent);
			return true;
		case R.id.menu_disconnect_user:
			//reset user
			((JCApplication)getApplication()).clearDefaultUser();
			//restart
			Intent startActivityIntent = new Intent(this, LauncherActivity.class);
			startActivity(startActivityIntent);
			finish();
			return true;
		default:
			//Pour le ventiler sur les fragments
			return super.onOptionsItemSelected(item);
		}

	}
}

/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.generic</li>
 * <li>28 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.generic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.jcertif.android.JCApplication;
import com.jcertif.android.LauncherActivity;
import com.jcertif.android.R;
import com.jcertif.android.service.androidservices.UpdaterService;
import com.jcertif.android.transverse.model.User;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class BaseActivityHC extends Activity implements BaseActivityIntf {
	/**
	 * The refresh button in the JCertifHeader panel
	 */
	ImageButton btnRefresh = null;
	/**
	 * the search button in the JCertifHeader panel
	 */
	ImageButton btnSearch = null;
	/**
	 * The connect button in the JCertifHeader panel
	 */
	ImageButton btnConnect = null;
	/**
	 * the disconnect button in the JCertifHeader panel
	 */
	ImageButton btnDisconnect = null;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// change the action bar logo
		getActionBar().setLogo(getResources().getDrawable(R.drawable.logo));
		getActionBar().setTitle(getResources().getString(R.string.main_htitle));
		// Then register this in the JCertifMobil Application object
		((JCApplication) getApplication()).setBaseActivity(this);
		// Glue the btn_refresh_button with the updateMethod
		if (null == btnRefresh) {
			btnRefresh = ((ImageButton) findViewById(R.id.btn_refresh_button));
			btnRefresh.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					refreshData();
				}
			});
		}
		if (null == btnSearch) {
			btnSearch = ((ImageButton) findViewById(R.id.btn_search_button));
			btnSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					search();
				}
			});
		}
		if (null == btnDisconnect) {
			btnDisconnect = ((ImageButton) findViewById(R.id.btn_disconnect_button));
			btnDisconnect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					disconnect();
				}
			});
		}
		if (null == btnConnect) {
			btnConnect = ((ImageButton) findViewById(R.id.btn_connect_button));
			btnConnect.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					connect();
				}
			});
		}
		// see what icon connect or disconnect to display
		User user = ((JCApplication) getApplication()).getUser();
		if (null == user || null == user.getEmail() || user.getEmail().length() == 0) {
			btnDisconnect.setVisibility(View.GONE);
		} else {
			btnConnect.setVisibility(View.GONE);
		}
	}

	/******************************************************************************************/
	/** TopBar Management **************************************************************************/
	/******************************************************************************************/

	/**
	 * Launch the UpdaterService and show the progressBar
	 */
	public void refreshData() {
		// Launch the UpdaterService
		Intent updateServiceIntent = new Intent(this, UpdaterService.class);
		this.startService(updateServiceIntent);
		// Update the value of the last update time
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Long now = System.currentTimeMillis();
		// update the preferences
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong(this.getString(R.string.shLastUpdateTime), now);
		editor.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.ui.view.generic.BaseActivityIntf#showRefreshingDataProgressBar(boolean)
	 */
	@Override
	public void showRefreshingDataProgressBar(boolean show) {
		if (show) {
			theMenu.findItem(R.id.menu_forceupdate).setIcon(
					getResources().getDrawable(R.drawable.ic_action_refreshing_data));
		} else {
			theMenu.findItem(R.id.menu_forceupdate).setIcon(getResources().getDrawable(R.drawable.navigation_refresh));
		}

	}

	/**
	 * Launch The search activity
	 */
	public void search() {
		Toast.makeText(this, "La recherche n'est pas encore implémentée :o)", Toast.LENGTH_SHORT).show();

	}

	/**
	 * Disconnect
	 */
	public void disconnect() {
		// reset user
		((JCApplication) getApplication()).clearDefaultUser();
		// restart
		connect();
	}

	/**
	 * Restart
	 */
	public void connect() {
		// restart
		Intent startActivityIntent = new Intent(this, LauncherActivity.class);
		startActivity(startActivityIntent);
		finish();
	}

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
		// see what icon connect or disconnect to display
		// see what icon connect or disconnect to display
		User user = ((JCApplication) getApplication()).getUser();
		if (null == user || null == user.getEmail() || user.getEmail().length() == 0) {
			menu.findItem(R.id.menu_disconnect_user).setVisible(false);
		} else {
			menu.findItem(R.id.menu_connect_user).setVisible(false);
		}
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
			// reset user
			((JCApplication) getApplication()).clearDefaultUser();
		case R.id.menu_connect_user:
			// restart
			connect();
			return true;
		case R.id.menu_search:
			// reset user
			search();
			return true;
		default:
			// Pour le ventiler sur les fragments
			return super.onOptionsItemSelected(item);
		}

	}

}

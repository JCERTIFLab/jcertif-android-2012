/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.braodcastreceiver</li>
 * <li>1 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.braodcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jcertif.android.service.androidservices.UpdaterService;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class BootAndWifiReceiver extends BroadcastReceiver {

	/**
	 * The min Delay between two updates
	 * 1000*60*60*6
	 */
	private final Long minDelay = 21600000l;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.w("BootAndWifiReceiver", "Intet received :" + intent.getAction());
		// Here we are because we receive either the boot completed event
		// either the connection changed event
		// either the wifi state changed event
		// So if we are connected to WIFI and if 6h has been spent before the last update (forth a
		// day so the minDelay constant)
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (null != networkInfo) {
			boolean isConnected = networkInfo.isConnected();
			boolean isWifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
			if (isConnected && isWifi) {
				Log.w("BootAndWifiReceiver", "isConnected && isWifi : true");
				// test if the update have been done 6h ago or not
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				Long lastUpdateTime = prefs.getLong(context.getString(R.string.shLastUpdateTime), 0l);
				Long now = System.currentTimeMillis();
				Log.w("BootAndWifiReceiver", "lastUpdateTime"+lastUpdateTime+" && now : now"+now+" now - lastUpdateTime"+(now - lastUpdateTime));
				if (minDelay < (now - lastUpdateTime)) {
					Log.e("BootAndWifiReceiver", "Launching update service");
					// Launch the database update
					Intent updateServiceIntent = new Intent(context, UpdaterService.class);
					context.startService(updateServiceIntent);
					// update the preferences
					SharedPreferences.Editor editor = prefs.edit();
					editor.putLong(context.getString(R.string.shLastUpdateTime), now);
					editor.commit();
				}
			}
		}

	}
}

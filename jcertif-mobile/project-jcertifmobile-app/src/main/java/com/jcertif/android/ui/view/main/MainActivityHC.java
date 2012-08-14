/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.main</li>
 * <li>22 mai 2012</li>
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
package com.jcertif.android.ui.view.main;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.transverse.model.User;
import com.jcertif.android.ui.view.generic.BaseActivityHC;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public class MainActivityHC extends BaseActivityHC {

	/**
	 * The one that as in charge the fragment switching and displays
	 */
	FragmentsSwitcherHC fragmentSwitcher;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("LifeCycle MainActivityLeg", "onCreate");
		setContentView(R.layout.main_activity);
		findViewById(R.id.titleLayout).setVisibility(View.GONE);
		
		fragmentSwitcher = ((JCApplication) getApplication()).initialise(this, findViewById(R.id.isLandscape) != null,
				savedInstanceState != null);
		// add the action on icon to display the account owner
		findViewById(R.id.logo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayAccountOwner();

			}
		});
	}

	/**
	 * @return the fragmentSwitcher
	 */
	public final FragmentsSwitcherHC getFragmentSwitcher() {
		return fragmentSwitcher;
	}

	private void displayAccountOwner() {

		// Build the view using the file R.layout.toast_layout using the R.id.toast_layout_root
		// element as the root view
		View layout = getLayoutInflater().inflate(R.layout.toast_account, null);
		// then create the Toast
		Toast toast = new Toast(this);
		// Define the gravity can be all the gravity constant and can be associated using |
		// (exemple: Gravity.TOP|Gravity.LEFT)
		// the xOffset and yOffSet moves the Toast (in pixel)
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// define the time duration of the Toast
		toast.setDuration(Toast.LENGTH_LONG);
		// Set the layout of the toast
		toast.setView(layout);
		// update the name of the user
		TextView message = (TextView) toast.getView().findViewById(R.id.text_toast_account);
		User user = ((JCApplication) getApplication()).getUser();
		if ( (null != user)&&(user.nom.length()!=0) ){
			String name = user.nom;
			message.setText(String.format(getString(R.string.accountToastMessage), name));
		} else {
			message.setText(getString(R.string.accountToastMessageNoUser));
		}
		// And display it
		toast.show();
	}

	/******************************************************************************************/
	/** Unused just to track activity life cycle to delete when dev is over **************************************************************************/
	/******************************************************************************************/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.w("LifeCycle MainActivityLeg", "onDestroy");
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		Log.w("LifeCycle MainActivityLeg", "onPause");
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.w("LifeCycle MainActivityLeg", "onResume");
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		Log.w("LifeCycle MainActivityLeg", "onStart");
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		Log.w("LifeCycle MainActivityLeg", "onStop");
		super.onStop();
	}
}

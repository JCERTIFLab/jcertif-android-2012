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

import android.os.Bundle;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.ui.view.generic.BaseActivityLegacy;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be the main activity that displays the whole application using fragments
 */
public class MainActivityLegacy extends BaseActivityLegacy  {
	
	
	/**
	 * The one that as in charge the fragment switching and displays
	 */
	FragmentsSwitcherLegacy fragmentSwitcher;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.w("LifeCycle MainActivityLeg","onCreate");
		setContentView(R.layout.main_activity);
		fragmentSwitcher=((JCApplication)getApplication()).initialise(this, findViewById(R.id.isLandscape)!=null , savedInstanceState != null);
		// recreation mode ==(savedInstanceState != null)
		//ragmentSwitcher.showMain(savedInstanceState != null);
	}
	
	/**
	 * @return the fragmentSwitcher
	 */
	public final FragmentsSwitcherLegacy getFragmentSwitcher() {
		return fragmentSwitcher;
	}
	
	
/******************************************************************************************/
/** Unused just to track activity life cycle to delete when dev is over **************************************************************************/
/******************************************************************************************/
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		Log.w("LifeCycle MainActivityLeg","onDestroy");
		super.onDestroy();
	}





	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		Log.w("LifeCycle MainActivityLeg","onPause");
		super.onPause();
	}





	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		Log.w("LifeCycle MainActivityLeg","onResume");
		super.onResume();
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		Log.w("LifeCycle MainActivityLeg","onStart");
		super.onStart();
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		Log.w("LifeCycle MainActivityLeg","onStop");
		super.onStop();
	}
	
}

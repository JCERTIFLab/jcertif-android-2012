/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.info</li>
 * <li>13 août 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertify Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.info;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.ui.view.main.MainActivityHC;
import com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class InfoFragmentHC extends Fragment {
	/**
	 * The parent
	 */
	MainFragmentCallBack parent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		parent = (MainFragmentCallBack) ((MainActivityHC) activity).getFragmentSwitcher();
		super.onAttach(activity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.info_fragment, container, false);

		// insure the fragment not being destroyed when activity destroyed because else memory leaks
		// is generated and null pointerExceptions too (when rotating the device)
		setRetainInstance(true);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.i("LifeCycle MainFragment", "onResume");
		// Define header title
		getActivity().getActionBar().setTitle(getResources().getString(R.string.info_htitle));
		// ensure the panel is displayed using the whole space
		if(null==parent) {
			parent = (MainFragmentCallBack) ((MainActivityHC) getActivity()).getFragmentSwitcher();
			
		}
		parent.fillSpace(true);
		// update the current shown fragment
		super.onResume();
	}

}

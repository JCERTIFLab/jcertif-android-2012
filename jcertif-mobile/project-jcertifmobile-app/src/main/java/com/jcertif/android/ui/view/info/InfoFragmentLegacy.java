/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.info</li>
 * <li>14 août 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.ui.view.info;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.ui.view.main.MainActivityHC;
import com.jcertif.android.ui.view.main.MainActivityLegacy;
import com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public class InfoFragmentLegacy extends Fragment {
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
		parent = (MainFragmentCallBack) ((MainActivityLegacy) activity).getFragmentSwitcher();
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
		TextView headerTitle = (TextView) getActivity().findViewById(R.id.header_title);
		headerTitle.setText(R.string.info_htitle);
		// ensure the panel is displayed using the whole space
		parent.fillSpace(true);

		// update the current shown fragment
		super.onResume();
	}
}
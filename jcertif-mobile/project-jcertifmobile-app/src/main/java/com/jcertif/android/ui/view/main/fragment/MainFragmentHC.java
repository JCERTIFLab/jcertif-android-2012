/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.main.fragment</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 */
package com.jcertif.android.ui.view.main.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.ui.view.main.MainActivityHC;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to display the main fragment with all the buttons Events, Calendar, ...
 */
public class MainFragmentHC extends Fragment {

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
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		addListeners(view);
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
		getActivity().getActionBar().setTitle(getResources().getString(R.string.main_htitle));
		// ensure the panel is displayed using the whole space
		parent.fillSpace(true);

		// update the current shown fragment
		super.onResume();
	}

	/**
	 * Add the buttons listeners to elements
	 * 
	 * @param view
	 */
	private void addListeners(View view) {
		if (parent == null) {
			parent = ((MainActivityHC) getActivity()).getFragmentSwitcher();
		}
		// To each button showw*** call the parent show***() method
		Button btnSpeaker = (Button) view.findViewById(R.id.btnSpeaker);
		btnSpeaker.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				parent.showSpeakers();
			}
		});

		Button btnSession = (Button) view.findViewById(R.id.btnSession);
		btnSession.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				parent.showEvents();
			}
		});

		Button btnAgenda = (Button) view.findViewById(R.id.btnAgenda);
		btnAgenda.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				parent.showAgenda();
			}
		});

		Button btnCalendrier = (Button) view.findViewById(R.id.btnCalendrier);
		btnCalendrier.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				parent.showCalendar();
			}
		});

		Button btnInfo = (Button) view.findViewById(R.id.btnInfo);
		btnInfo.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				parent.showInfo();
			}
		});
	}

}

/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.speaker</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 */
package com.jcertif.android.ui.view.speaker.list;

import java.sql.SQLException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.adapter.SpeakerAdapter;
import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to displays the list of speakers within a fragment
 */
public class SpeakersListFragment extends ListFragment {
	/**
	 * The parent to callback
	 */
	SpeakersListCallBack callback;
	/**
	 * The speaker list
	 */
	private List<Speaker> speakers;
	/**
	 * The speaker list adapter
	 */
	SpeakerAdapter speakerAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.w("LifeCycle SpeakersListFragment", "onCreateView");
		View view = inflater.inflate(R.layout.speaker_list_frgmt, container, false);
		// retrieve data in the database
		SpeakerProvider sp;
		try {
			sp = new SpeakerProvider();
			speakers = sp.findAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// set the adapter using SpeakerAdapter
		speakerAdapter = new SpeakerAdapter(getActivity(), speakers);
		this.setListAdapter(speakerAdapter);
		// insure the fragment not being destroyed when activity destroyed because else memory leaks
		// is generated and null pointerExceptions too (when rotating the device)
		setRetainInstance(true);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getListView().setTextFilterEnabled(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Speaker selectedSpeaker = speakerAdapter.getItem(position);
		getCallBack().showSelectedSpeaker(selectedSpeaker.id);
		super.onListItemClick(l, v, position, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.w("LifeCycle SpeakersListFragment", "onResume");
		// Define header title
		TextView headerTitle = (TextView) getActivity().findViewById(R.id.header_title);
		headerTitle.setText(R.string.speaker_list_htitle);
		super.onResume();
	}

	/**
	 * To be sure that the callBack is instantiate
	 */
	public SpeakersListCallBack getCallBack() {
		if (callback == null) {
			callback = (SpeakersListCallBack) ((MainActivityLegacy) getActivity()).getFragmentSwitcher();
		}
		return callback;
	}

	/******************************************************************************************/
	/** Usunused To track fragment lifecycle **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.w("LifeCycle SpeakersListFragment", "onDestroy");
		// Then the backStack is called
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#startActivity(android.content.Intent)
	 */
	@Override
	public void startActivity(Intent intent) {
		Log.w("LifeCycle SpeakersListFragment", "startActivity");
		super.startActivity(intent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#startActivityForResult(android.content.Intent, int)
	 */
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		Log.w("LifeCycle SpeakersListFragment", "startActivityForResult");
		super.startActivityForResult(intent, requestCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.w("LifeCycle SpeakersListFragment", "onCreate");
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.w("LifeCycle SpeakersListFragment", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		Log.w("LifeCycle SpeakersListFragment", "onStart");
		super.onStart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.w("LifeCycle SpeakersListFragment", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause() {
		Log.w("LifeCycle SpeakersListFragment", "onPause");
		super.onPause();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onStop()
	 */
	@Override
	public void onStop() {
		Log.w("LifeCycle SpeakersListFragment", "onStop");
		super.onStop();
	}

}

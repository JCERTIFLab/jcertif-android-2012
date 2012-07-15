/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.event.list</li>
 * <li>30 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.event.list;

import java.sql.SQLException;
import java.util.List;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.ui.adapter.EventAdapter;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public class EventsListFragment extends ListFragment {
	/**
	 * The parent to callback
	 */
	EventsListCallBack callBack;
	/**
	 * The speaker list
	 */
	private List<Event> events;
	/**
	 * The speaker list adapter
	 */
	EventAdapter eventAdapter;
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.w("LifeCycle EventsListFragment", "onCreateView");
		View view = inflater.inflate(R.layout.event_list_fragment, container, false);
		// retrieve data in the database
		EventProvider ep;
		try {
			ep = new EventProvider();
			events = ep.getAllEvents();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// set the adapter using SpeakerAdapter
		eventAdapter = new EventAdapter(getActivity(), events);
		this.setListAdapter(eventAdapter);
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
		Event selectedEvent = eventAdapter.getItem(position);
		getCallBack().showSelectedEvent(selectedEvent.id,true);
		super.onListItemClick(l, v, position, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.w("LifeCycle EventsListFragment", "onResume");
		// Define header title
		TextView headerTitle = (TextView) getActivity().findViewById(R.id.header_title);
		headerTitle.setText(R.string.sessions_list_htitle);
		super.onResume();
	}
	
	/**
	 * To be sure that the callBack is instantiate
	 */
	public EventsListCallBack getCallBack() {
		if (callBack == null) {
			callBack = (EventsListCallBack) ((MainActivityLegacy) getActivity()).getFragmentSwitcher();
		}
		return callBack;
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
			Log.w("LifeCycle EventsListFragment", "onDestroy");
			//Then the backStack is called
			super.onDestroy();
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#startActivity(android.content.Intent)
		 */
		@Override
		public void startActivity(Intent intent) {
			Log.w("LifeCycle EventsListFragment", "startActivity");
			super.startActivity(intent);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#startActivityForResult(android.content.Intent, int)
		 */
		@Override
		public void startActivityForResult(Intent intent, int requestCode) {
			Log.w("LifeCycle EventsListFragment", "startActivityForResult");
			super.startActivityForResult(intent, requestCode);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			Log.w("LifeCycle EventsListFragment", "onCreate");
			super.onCreate(savedInstanceState);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
		 */
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			Log.w("LifeCycle EventsListFragment", "onActivityCreated");
			super.onActivityCreated(savedInstanceState);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#onStart()
		 */
		@Override
		public void onStart() {
			Log.w("LifeCycle EventsListFragment", "onStart");
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
			Log.w("LifeCycle EventsListFragment", "onConfigurationChanged");
			super.onConfigurationChanged(newConfig);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#onPause()
		 */
		@Override
		public void onPause() {
			Log.w("LifeCycle EventsListFragment", "onPause");
			super.onPause();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.app.Fragment#onStop()
		 */
		@Override
		public void onStop() {
			Log.w("LifeCycle EventsListFragment", "onStop");
			super.onStop();
		}
}

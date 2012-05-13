package com.jcertif.android.view;

import java.sql.SQLException;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.jcertif.android.adapter.EventAdapter;
import com.jcertif.android.app.Application;
import com.jcertif.android.data.ormlight.EventProvider;
import com.jcertif.android.model.Event;
import com.jcertif.android.parsing.jackson.service.EventsController;
import com.jcertif.android.service.JCertifService;
import com.jcertif.android.service.State;
import com.jcertif.android.service.StateAdapter;
import com.jcertif.android.service.StateListener;

/**
 * 
 * @author Yakhya DABO
 * 
 */
public class EventsListActivity extends ListActivity {

	private State<List<Event>> state = null;
	/**
	 * The view configuration state (changed or not)
	 */
	private boolean isConfigurationChanging = false;
	public boolean OK_WEB = true;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.event_list);

		initState();
		
		// Define header title
		TextView headerTitle = (TextView) findViewById(R.id.header_title);
		headerTitle.setText(R.string.session_list_title);

	}

	private void initState(){
		state = (EventState) getLastNonConfigurationInstance();

		if (state == null) {
			state = new EventState();
			getApplicationContext().bindService(
					new Intent(this, JCertifService.class), state.getConn(),
					BIND_AUTO_CREATE);
		} 

		stateAdapter.setContext(EventsListActivity.this);
		state.attach(stateAdapter);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		if (!isConfigurationChanging) {
			getApplicationContext().unbindService(state.getConn());
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		isConfigurationChanging = true;

		return (state);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// // Get the item that was clicked
		// Object o = this.getListAdapter().getItem(position);
		// String keyword = o.toString();
		// Toast.makeText(this, "You selected: " + keyword, Toast.LENGTH_LONG)
		// .show();
	}

	private void setData(List<Event> events) {
		this.setListAdapter(new EventAdapter(this, events));
	}

	public class EventState extends State<List<Event>> {

		@Override
		public List<Event> getData() throws Exception {
			List<Event> events = null;
			EventProvider eventsProvider =  new EventProvider(getBinder().getService().getBaseContext());
			
			if (OK_WEB) {
				OK_WEB = false;
				Log.i(Application.NAME, "EventListActivity : getting data from WS ");
				
				String json = getBinder().getEventList();
				EventsController eventsController = new EventsController(json);
				eventsController.init();
				events = eventsController.findAll();
				eventsProvider.saveEvents(events);
				
				Log.i(Application.NAME, "EventListActivity : " + events.toString());
			}else{
				Log.e(Application.NAME, "EventListActivity : getting data from DB");
				events = eventsProvider.getAllEvents();
				Log.i(Application.NAME, "EventListActivity : " + events.toString());
			}
			Log.i(Application.NAME, "EventListActivity : " + events.toString());
			
			return events;
		}
	}

	private StateListener<List<Event>> stateAdapter = new StateAdapter<List<Event>>() {

		@Override
		public void onServiceConnected() {
			state.getBinder().getWebServiceData(state, EventsListActivity.this);
		}

		@Override
		public void onDataAvailable(List<Event> events) {
			// projects = data;
			setData(events);
		}

		@Override
		public void onError(Throwable t) {
			Log.e(Application.NAME, t.getMessage());
			AlertDialog.Builder builder = new AlertDialog.Builder(
					EventsListActivity.this);
			builder.setTitle(R.string.alertDialogTitle)
					.setMessage(getMessageToDisplay(t.getMessage().trim()))
					.setPositiveButton("OK", null).show();
		}

	};

}

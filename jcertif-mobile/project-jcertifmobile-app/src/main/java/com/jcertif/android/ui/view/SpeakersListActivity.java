package com.jcertif.android.ui.view;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.jcertif.android.Application;
import com.jcertif.android.com.parsing.jackson.service.SpeakersController;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.service.androidservices.JCertifService;
import com.jcertif.android.service.androidservices.State;
import com.jcertif.android.service.androidservices.StateAdapter;
import com.jcertif.android.service.androidservices.StateListener;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.adapter.SpeakerAdapter;

/**
 * This activity displays the list of speakers
 * @author mouhamed_diouf
 *
 */
public class SpeakersListActivity extends ListActivity {

	private State<List<Speaker>> state = null;
	/**
	 * The view configuration state (changed or not)
	 */
	private boolean isConfigurationChanging = false;
	public boolean OK_WEB = true;
	
	private Speaker selectedSpeaker;
	private List<Speaker> speakers;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker_list);

		initState();
		
      // Define header title
		TextView headerTitle = (TextView) findViewById(R.id.header_title);
		headerTitle.setText(R.string.speaker_list_header_title);					

	}
	
	private void initState(){
		state = (SpeakerState) getLastNonConfigurationInstance();

		if (state == null) {
			state = new SpeakerState();
			getApplicationContext().bindService(
					new Intent(this, JCertifService.class), state.getConn(),
					BIND_AUTO_CREATE);
		} 

		stateAdapter.setContext(SpeakersListActivity.this);
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

	private void setData(final List<Speaker> speakers) {
		
		this.setListAdapter(new SpeakerAdapter(this, speakers));
		
		getListView().setTextFilterEnabled(true);
		final Intent intentForDisplay = new Intent(getApplicationContext(), SpeakerActivity.class);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int _index,
					long arg3) {

				selectedSpeaker = speakers.get(_index);
				intentForDisplay.putExtra("speakerId", selectedSpeaker.id);
				startActivity(intentForDisplay);
				Log.i(Application.NAME + this.getClass(), "Selected Speaker : " + selectedSpeaker.id);
			}
			
		});
	}

	public class SpeakerState extends State<List<Speaker>> {

		@Override
		public List<Speaker> getData() throws Exception {
			List<Speaker> speakers = null;
			SpeakerProvider speakersProvider =  new SpeakerProvider(getBinder().getService().getBaseContext());
			
			if (OK_WEB) {
				OK_WEB = false;
				Log.i(Application.NAME, "SpeakerListActivity : getting data from WS ");
				
				String json = getBinder().getSpeakerList();
				SpeakersController speakersController = new SpeakersController(json);
				speakersController.init();
				speakers = speakersController.findAll();
				speakersProvider.saveAll(speakers);
				
				Log.i(Application.NAME, "SpeakerListActivity : " + speakers.toString());
			}else{
				Log.e(Application.NAME, "SpeakerListActivity : getting data from DB");
				speakers = speakersProvider.findAll();
				Log.i(Application.NAME, "SpeakerListActivity : " + speakers.toString());
			}
			Log.i(Application.NAME, "SpeakerListActivity : " + speakers.toString());
			
			return speakers;
		}
	}

	private StateListener<List<Speaker>> stateAdapter = new StateAdapter<List<Speaker>>() {

		@Override
		public void onServiceConnected() {
			state.getBinder().getWebServiceData(state, SpeakersListActivity.this);
		}

		@Override
		public void onDataAvailable(List<Speaker> speakers) {
			setData(speakers);
		}

		@Override
		public void onError(Throwable t) {
			Log.e(Application.NAME, t.getMessage());
			AlertDialog.Builder builder = new AlertDialog.Builder(
					SpeakersListActivity.this);
			builder.setTitle(R.string.alertDialogTitle)
					.setMessage(getMessageToDisplay(t.getMessage().trim()))
					.setPositiveButton("OK", null).show();
		}

	};

}
    
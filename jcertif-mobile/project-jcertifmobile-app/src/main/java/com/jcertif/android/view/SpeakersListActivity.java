package com.jcertif.android.view;

import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.jcertif.android.adapter.SpeakerAdapter;
import com.jcertif.android.data.SpeakerProvider;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.JCertifLocalService;
import com.jcertif.android.service.JCertifLocalService.LocalBinder;

/**
 * This activity is responsible for displaying list of speakers
 * @author mouhamed_diouf
 *
 */
public class SpeakersListActivity extends ListActivity {
	
	/**
	 * Contains speakers data 
	 */
	private List<Speaker> speakers;
	private SpeakerAdapter adapter;
	private Speaker selectedSpeaker;
	
	private JCertifLocalService mService;
    boolean mBound = false;

	private LayoutInflater mInflater;
	protected SpeakerProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker);

        // Define header title
        TextView headerTitle = (TextView) findViewById(R.id.header_title);
        headerTitle.setText(R.string.speaker_list_header_title);

        Intent intent = new Intent(this, JCertifLocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService

        
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    /** Called when a button is clicked (the button in the layout file attaches to
      * this method with the android:onClick attribute) */
    public void onButtonClick(View v) {
        if (mBound) {
            Toast.makeText(this, "number: ", Toast.LENGTH_SHORT).show();
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName arg0, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            
            provider = new SpeakerProvider(mService);
            try {
    			speakers = provider.getAllSpeakers();
    			adapter = new SpeakerAdapter(binder.getService().getBaseContext(), R.layout.speakerslist, speakers,
    					mInflater);
    			setListAdapter(adapter);
    			getListView().setTextFilterEnabled(true);
    			final Intent intentForDisplay = new Intent(getApplicationContext(), SpeakerActivity.class);
    			getListView().setOnItemClickListener(new OnItemClickListener() {

    				public void onItemClick(AdapterView<?> arg0, View arg1, int _index,
    						long arg3) {

    					selectedSpeaker = speakers.get(_index);
    					intentForDisplay.putExtra("speakerId", selectedSpeaker.id);
    					startActivity(intentForDisplay);
    					
    				}
    				
    			});
    		} catch (Exception e) {
    			// TODO Handle Exception
    			e.printStackTrace();
    		}
			
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
    	
    };
}
    
//}
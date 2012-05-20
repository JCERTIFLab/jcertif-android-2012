package com.jcertif.android.ui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author Yakhya DABO
 *
 */
public class MainActivity extends Activity{

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        		
        setContentView(R.layout.main_activity);
        
        Button btnSpeaker = (Button) findViewById(R.id.btnSpeaker);
        btnSpeaker.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), SpeakersListActivity.class);					
				startActivityForResult(intent, 0);				
			}
		});
        
        Button btnSession = (Button) findViewById(R.id.btnSession);
        btnSession.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), EventsListActivity.class);					
				startActivityForResult(intent, 0);				
			}
		});
        
    }
}
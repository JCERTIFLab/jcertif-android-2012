package com.jcertif.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jcertif.android.Application;

public class ConnectionActivity extends Activity{
	/**
	 * Login key for user preferences.
	 */
	private static final String EMAIL = "email";
	
	/**
	 * Password key for user preferences.
	 */
	private static final String PASSWORD = "password";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        		
        setContentView(R.layout.connection);
        
        Button btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
        btnSubscribe.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				displayAccountProfileDialog();			
			}
		});

        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        Button btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if ((txtEmail.getText().length()>0)&& ((txtPassword.getText().length()>0))){
					saveCredentials();				
					Application.EMAIL = txtEmail.getText().toString();
					Application.PASSWORD = txtPassword.getText().toString();
					
					// TODO call web service
					Intent intent = new Intent(getApplicationContext(), MenuView.class);					
					startActivityForResult(intent, 0);	
				}						
			}

		});
        
        Button btnQuit = (Button) findViewById(R.id.btnQuit);
        btnQuit.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});
        
        loadCredentials();

    }
    
    private boolean loadCredentials() {
    	//  Get preferences
		SharedPreferences preferences = getSharedPreferences(Application.NAME, MODE_PRIVATE);
		boolean loaded = false;
		
		//	Load email
		final String email = preferences.getString(EMAIL, null);
		if (email != null){
			final TextView loginView = (TextView)this.findViewById(R.id.txtEmail);
			loginView.setText(email);
		}
		
		//	Load password
		final String password = preferences.getString(PASSWORD, null);
		if (password != null){
			final TextView passwordView = (TextView)this.findViewById(R.id.txtPassword);
			passwordView.setText(password);
			
			loaded = true;
		}
		
		return loaded;
		
	}

    private void displayAccountProfileDialog(){
        final Dialog dialog = new Dialog(ConnectionActivity.this);
        dialog.setContentView(R.layout.account_profile_dialog);
        dialog.setTitle(R.string.accountProfileLabel);
        dialog.setCancelable(true);
        
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
        
            public void onClick(View v) {
        		dialog.cancel();
            }
        });
        
        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new OnClickListener() {
        
            public void onClick(View v) {
        		displayAccountInformationDialog();
        		dialog.cancel();
            }

        });
        
        dialog.show();
    }
    
	private void displayAccountInformationDialog() {
		 final Dialog dialog = new Dialog(ConnectionActivity.this);
	        dialog.setContentView(R.layout.account_information_dialog);
	        dialog.setTitle(R.string.accountInformationLabel);
	        dialog.setCancelable(true);
	        
	        // displays civility spinner
	        Spinner spnCivility = (Spinner) dialog.findViewById(R.id.spnCivility);
	        ArrayAdapter<CharSequence> civilityAdapter = ArrayAdapter.createFromResource(
	                this, R.array.civility_array, android.R.layout.simple_spinner_item);
	        civilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spnCivility.setAdapter(civilityAdapter);

	        // displays role spinner
	        Spinner spnRole = (Spinner) dialog.findViewById(R.id.spnRole);
	        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
	                this, R.array.role_array, android.R.layout.simple_spinner_item);
	        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spnRole.setAdapter(roleAdapter);
	        
	        // displays type spinner
	        Spinner spnType = (Spinner) dialog.findViewById(R.id.spnType);
	        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
	                this, R.array.type_array, android.R.layout.simple_spinner_item);
	        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        spnType.setAdapter(typeAdapter);
	        
	        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
	        btnCancel.setOnClickListener(new OnClickListener() {
	        
	            public void onClick(View v) {
	        		dialog.cancel();
	            }
	        });
	        
	        Button btnSave = (Button) dialog.findViewById(R.id.btnSave);
	        btnSave.setOnClickListener(new OnClickListener() {
	    
	            public void onClick(View v) {
	        		// TODO call web service to save data
	            }

	        });
	        
	        dialog.show();
		
	}
	
	private void saveCredentials() {
		//  Get preferences
    		SharedPreferences preferences = getSharedPreferences(Application.NAME, MODE_PRIVATE);
    		Editor editor = preferences.edit();
    		
    	//	Store email
    		final TextView txtEmail = (TextView)this.findViewById(R.id.txtEmail);
    		final String email = txtEmail.getText().toString();
    		editor.putString(EMAIL, email);
    		
    	//	Store password
    		final TextView txtPassword = (TextView)this.findViewById(R.id.txtPassword);
    		final String password = txtPassword.getText().toString();
    		editor.putString(PASSWORD, password);
    		
    		editor.commit();
		
	}
}
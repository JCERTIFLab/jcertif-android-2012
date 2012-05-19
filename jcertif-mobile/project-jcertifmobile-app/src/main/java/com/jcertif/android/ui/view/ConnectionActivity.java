package com.jcertif.android.ui.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.service.androidservices.JCertifService;
import com.jcertif.android.service.androidservices.State;
import com.jcertif.android.service.androidservices.StateAdapter;
import com.jcertif.android.service.androidservices.StateListener;
import com.jcertif.android.transverse.model.User;


/**
 * 
 * @author Yakhya DABO
 */
public class ConnectionActivity extends Activity {
	/**
	 * Login key for user preferences.
	 */
	private static final String EMAIL = "email";
	/**
	 * Password key for user preferences.
	 */
	private static final String PASSWORD = "password";

	private State<String> state = null;
	
	boolean mBound = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);
		
		initState();
		initGUI();
	}

	private void initState(){
		state = (LoginState) getLastNonConfigurationInstance();

		if (state == null) {
			state = new LoginState();
			getApplicationContext().bindService(
					new Intent(this, JCertifService.class), state.getConn(),
					BIND_AUTO_CREATE);
		} 

		stateAdapter.setContext(ConnectionActivity.this);
		state.attach(stateAdapter);
	}
	
	/**
	 * Displays buttons, editText, ...
	 */
	private void initGUI() {
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
				if ((txtEmail.getText().length() > 0) && ((txtPassword.getText().length() > 0))) {
//					JCApplication.getInstance().getUser(). = txtEmail.getText().toString();
//					JCApplication.PASSWORD = txtPassword.getText().toString();
					
					// ProgressDialog doesn't like getApplicationContext(). 
					// That's why we use LoginView.this as the context parameter
					state.getBinder().getWebServiceData(state, ConnectionActivity.this);
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

	private void displayMenuView() {
		Intent intentView = new Intent(getApplicationContext(), MainActivity.class);
		startActivityForResult(intentView, 0);
	}

	final User user = new User();

	private void displayAccountProfileDialog() {
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

				String email1 = ((EditText) dialog.findViewById(R.id.txtEmail)).getText()
						.toString();
				String email2 = ((EditText) dialog.findViewById(R.id.txtEmailConfirmation))
						.getText().toString();

				String password1 = ((EditText) dialog.findViewById(R.id.txtPassword)).getText()
						.toString();
				String password2 = ((EditText) dialog.findViewById(R.id.txtPasswordConfirmation))
						.getText().toString();

				Log.i(JCApplication.NAME, "pwd1 : " + password1);
				Log.i(JCApplication.NAME, "pwd2 : " + password2);

				Log.i(JCApplication.NAME, "email1 : " + email1);
				Log.i(JCApplication.NAME, "email2 : " + email2);

				if (password1.equals(password2) && email1.equals(email2)) {
					user.setEmail(email1);
					user.setPassword(password1);
					displayAccountInformationDialog();
					dialog.cancel();
				}
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
		final Spinner spnCivility = (Spinner) dialog.findViewById(R.id.spnCivility);
		ArrayAdapter<CharSequence> civilityAdapter = ArrayAdapter.createFromResource(this,
				R.array.civility_array, android.R.layout.simple_spinner_item);
		civilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCivility.setAdapter(civilityAdapter);

		// displays role spinner
		final Spinner spnRole = (Spinner) dialog.findViewById(R.id.spnRole);
		ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,
				R.array.role_array, android.R.layout.simple_spinner_item);
		roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnRole.setAdapter(roleAdapter);

		// displays type spinner
		final Spinner spnType = (Spinner) dialog.findViewById(R.id.spnType);
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
				R.array.type_array, android.R.layout.simple_spinner_item);
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
				user.setCivilite(spnCivility.getSelectedItem().toString());
				user.setPrenom(((EditText) dialog.findViewById(R.id.txtFirstName)).getText()
						.toString());
				user.setNom(((EditText) dialog.findViewById(R.id.txtLastName)).getText().toString());
				user.setRole(spnRole.getSelectedItem().toString());
				user.setType(spnType.getSelectedItem().toString());
				registerUser(user);
				Log.i(JCApplication.NAME, user.toString());
				dialog.cancel();
			}

		});

		dialog.show();

	}

	private void registerUser(User user) {
		// Web service call
		try {
//			String data = mService.registerUser(user);
//
//			Log.i(JCApplication.NAME, "User data : " + data);

			/*
			 * if (!"null".equalsIgnoreCase(data)){ saveCredentials();
			 * displayMenuView(); }else{ AlertDialog.Builder builder = new
			 * AlertDialog.Builder(ConnectionActivity.this);
			 * builder.setTitle(R.string.alertDialogTitle)
			 * .setMessage(R.string.authenticationErrorMessage)
			 * .setPositiveButton("OK", null) .show(); }
			 */
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean loadCredentials() {
		// Get preferences
		SharedPreferences preferences = getSharedPreferences(JCApplication.NAME, MODE_PRIVATE);
		boolean loaded = false;

		// Load email
		final String email = preferences.getString(EMAIL, null);
		if (email != null) {
			final TextView loginView = (TextView) this.findViewById(R.id.txtEmail);
			loginView.setText(email);
		}

		// Load password
		final String password = preferences.getString(PASSWORD, null);
		if (password != null) {
			final TextView passwordView = (TextView) this.findViewById(R.id.txtPassword);
			passwordView.setText(password);

			loaded = true;
		}

		return loaded;

	}

	private void saveCredentials() {
		// Get preferences
		SharedPreferences preferences = getSharedPreferences(JCApplication.NAME, MODE_PRIVATE);
		Editor editor = preferences.edit();

		// Store email
		final TextView txtEmail = (TextView) this.findViewById(R.id.txtEmail);
		final String email = txtEmail.getText().toString();
		editor.putString(EMAIL, email);

		// Store password
		final TextView txtPassword = (TextView) this.findViewById(R.id.txtPassword);
		final String password = txtPassword.getText().toString();
		editor.putString(PASSWORD, password);

		editor.commit();

	}

	public class LoginState extends State<String> {

		@Override
		public String getData() throws Exception {
			String data = getBinder().authenticateUser();
			Log.i(JCApplication.NAME,"Login : " +  data);
			return data;
		}
	}
	
	
	private StateListener<String> stateAdapter = new StateAdapter<String> (){
        
        @Override
        public void onDataAvailable(String data) {
        	Log.i(JCApplication.NAME, "Login result : [" + data +"]");

        	if  (!"null".equalsIgnoreCase(data)){
        		saveCredentials();
        		displayMenuView();
        	}else{
        		AlertDialog.Builder builder = new AlertDialog.Builder(ConnectionActivity.this);
        		builder.setTitle(R.string.alertDialogTitle)
        				.setMessage(R.string.authenticationErrorMessage)
        				.setPositiveButton("OK", null)
        				.show();  
        	}
        }
    };    	
    
}
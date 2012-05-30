/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.connection</li>
 * <li>18 mai 2012</li>
 */
package com.jcertif.android.ui.view.connection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.service.threadtraitements.authent.AuthentThreadCallBack;
import com.jcertif.android.service.threadtraitements.authent.AuthentificationThread;
import com.jcertif.android.transverse.model.User;
import com.jcertif.android.transverse.tools.HttpTools;
import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.connection.accountdialogs.AccountDialog;
import com.jcertif.android.ui.view.connection.accountdialogs.AccountDialogParentIntf;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        Display the connection activity for Device which have a version level < HoneyComb
 */
public class ConnectionActivityLegacy extends Activity implements AccountDialogParentIntf, AuthentThreadCallBack {
	// TODO MSE Reflechir à la persistence de l'utilisateur, mais il manque la reponse de l'authent et de la registration
	/**
	 * The dialogs to display to the user for registration
	 */
	private AccountDialog accountDialog;
	/**
	 * The thread to call webservices to know is the user is authenticated
	 */
	private AuthentificationThread authThread;
	/**
	 * The boolean to know if the authThread is running or not
	 */
	private boolean isAuthThreadRunning = false;

	/******************************************************************************************/
	/** LifeCycle Methods **************************************************************************/
	/******************************************************************************************/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);
		accountDialog = new AccountDialog(this, this);
		// Restore the Authentification thread
		Object lastNonConf = getLastNonConfigurationInstance();
		if (lastNonConf != null) {
			authThread = (AuthentificationThread) lastNonConf;
			authThread.bindCallBack(this);
		} else {
			authThread = new AuthentificationThread(this);
		}
		// Load the last user credentials
		loadCredentials();
		// Add listeners to the buttons
		addListeners();
		// Define header title
		TextView headerTitle = (TextView) findViewById(R.id.header_title);
		headerTitle.setText(R.string.connexion_htitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Dismiss the dialog
		accountDialog.dismissDialog();
		accountDialog = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		// Retain the AuthThreadObject if it exists
		if (isAuthThreadRunning) {
			// unbind the thread and the activity
			authThread.unbindCallBack();
			// return it
			return authThread;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	/******************************************************************************************/
	/** Buttons Listeners and associated methods **********************************************/
	/******************************************************************************************/

	/**
	 * Displays buttons, editText, ...
	 */
	private void addListeners() {
		// Instantiate the btnSubscribe's listener
		Button btnSubscribe = (Button) findViewById(R.id.btnSubscribe);
		btnSubscribe.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				accountDialog.showAccountDialog();
			}
		});
		// Instantiate the btnSubscribe's listener
		Button btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onConnectButtonClicked();
			}
		});
		// Instantiate the btnQuit's listener
		Button btnQuit = (Button) findViewById(R.id.btnQuit);
		btnQuit.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				finish();
			}
		});

	}

	/******************************************************************************************/
	/** Managing Authentification *************************************************************/
	/******************************************************************************************/
	/**
	 * Method called when the connect button is clicked
	 * 
	 * @param edtEmail
	 * @param edtPassword
	 */
	private void onConnectButtonClicked() {
		// Retrieve the value of the email and password
		final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
		final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
		String email = edtEmail.getText().toString();
		String password = edtPassword.getText().toString();
		if (isValidUser(email, password)) {
			// go to the next activity
			// TODO MSE to centralize within a same method
			displayMainView();
		} else {

			if ((email.length() > 0) && ((password.length() > 0))) {
				// Launch the thread
				// authThread.execute(email, password);
				Bundle params = new Bundle();
				params.putString(AuthentificationThread.EMAIL_KEY, email);
				params.putString(AuthentificationThread.PASSWORD_KEY, password);
				authThread.execute(this, params);

			} else {
				// show error to the user
				TextView txvError = (TextView) findViewById(R.id.txvConnectionError);
				txvError.setText(getString(R.string.connectionEmptyElementErrorMessage));
				txvError.setVisibility(View.VISIBLE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.service.threadtraitements.BasicBackgroundCallBack#handleMessage(android
	 * .os.Message)
	 */
	@Override
	public void handleMessage(Message message) {
		String httpGetResponse = message.getData().getString(AuthentificationThread.RESPONSE_KEY);
		int httpGetRespStatus = message.getData().getInt(AuthentificationThread.STATUS_KEY);
		// Update what is need to be updated
		Log.w("ConnectionActivityLegacy", "Authentification httpGetResponse : |" + httpGetResponse+"|");
		//Be carefull HttpResponse = "null\r" ou "null\n" un truc du genre
		Log.w("ConnectionActivityLegacy", "Authentification HttpTools.isValidHttpResponseCode(httpGetRespStatus) : " + HttpTools.isValidHttpResponseCode(httpGetRespStatus));
		if (httpGetResponse != null && (!httpGetResponse.contains("null"))
				&& (HttpTools.isValidHttpResponseCode(httpGetRespStatus))) {
			Log.w("ConnectionActivityLegacy", "httpGetResponse : is ok");
			saveCredentials();
			// TODO here I should be able to update the user
			// saveUser();
			// JCApplication.updateUser
			// displayMenuView();
		} else {
			Log.w("ConnectionActivityLegacy", "Authentification httpGetResponse : is ko");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.alertDialogTitle).setMessage(R.string.authenticationErrorMessage)
					.setPositiveButton("OK", null).show();
		}
		// TODO MSE to replace in the if
		displayMainView();
	}

	/******************************************************************************************/
	/** Display the Menu ************************************/
	/******************************************************************************************/

	/**
	 * Display the Menu View
	 */
	private void displayMainView() {
		Intent intentView = new Intent(getApplicationContext(), MainActivityLegacy.class);
		startActivityForResult(intentView, 0);
	}
	/******************************************************************************************/
	/** Managing Registration *************************************************************/
	/******************************************************************************************/
	
	/* (non-Javadoc)
	 * @see com.jcertif.android.ui.view.connection.accountdialogs.AccountDialogParentIntf#onRegisterCallBack(com.jcertif.android.transverse.model.User)
	 */
	public void onRegisterCallBack(User user) {
		String email = user.getEmail();
		String password = user.getPassword();
		// Update the login/password
		if (email != null) {
			final EditText loginView = (EditText) this.findViewById(R.id.edtEmail);
			loginView.setText(email);
		}
		if (password != null) {
			final EditText passwordView = (EditText) this.findViewById(R.id.edtPassword);
			passwordView.setText(password);
		}
		// Save the user in the preference
		saveUser(user);
		// And update the user in the application
		int hash = (email.hashCode() + password.hashCode()) / 2;
		((JCApplication) getApplication()).updateUser(hash);
		//And a last by pass the connection
		//TODO Doit-on quand la regsitration est ok by-passé la connection ou pas???
		//i.e. doit faire  onConnectButtonClicked() ou displayMenuView ?o? ou rien ?
	}
	/******************************************************************************************/
	/** User Management With SharedPreference *************************************************/
	/******************************************************************************************/

	/**
	 * This methods loads from DefaultSahredPreference the user data
	 * 
	 * @return
	 */
	private void loadCredentials() {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Load the hashcode of the last user connected
		final String hash = preferences.getString(getString(R.string.shhash), "");
		// Load email
		final String email = preferences.getString(hash + getString(R.string.shEmail), null);
		// Load password
		final String password = preferences.getString(hash + getString(R.string.shPassword), null);
		// Update screen
		if (email != null) {
			final EditText loginView = (EditText) this.findViewById(R.id.edtEmail);
			loginView.setText(email);
		}
		if (password != null) {
			final EditText passwordView = (EditText) this.findViewById(R.id.edtPassword);
			passwordView.setText(password);
		}
	}

	/**
	 * To know if the user is still valid
	 * 
	 * @param email
	 *            user's email
	 * @param password
	 *            user's password
	 * @return if this user is valid
	 */
	public boolean isValidUser(String email, String password) {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Build the email and password hashcode
		int hash = (email.hashCode() + password.hashCode()) / 2;
		return preferences.getBoolean(hash + getString(R.string.shValidUser), false);
	}

	/**
	 * Call this method to store the user password.
	 * This method should be called only once when the Activity is destroyed
	 * And only if the user is valid
	 * And it should write to the disk using a thread (asyncTask for example)
	 */
	private void saveCredentials() {
		// find email
		final EditText edtEmail = (EditText) this.findViewById(R.id.edtEmail);
		final String email = edtEmail.getText().toString();
		// find password
		final EditText edtPassword = (EditText) this.findViewById(R.id.edtPassword);
		final String password = edtPassword.getText().toString();
		// Set the user to be accessible in the whole application
		User user = ((JCApplication) getApplication()).getUser();
		user.setEmail(email);
		user.setPassword(password);
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();

		// The Hashcode of the user
		// needed to have multi users account for the application
		int hash = (email.hashCode() + password.hashCode()) / 2;
		// Store the user password and email
		editor.putString(hash + getString(R.string.shEmail), email);
		editor.putString(hash + getString(R.string.shPassword), password);
		// Store validity
		editor.putBoolean(hash + getString(R.string.shValidUser), true);
		// and store the hashcode of the last user connected
		// to reload the email/password of the last connected person
		editor.putString(getString(R.string.shhash), Integer.toString(hash));
		editor.commit();

	}

	
	/**
	 * This method save the user profile within the defaultSharedPreference
	 * it should write to the disk using a thread (asyncTask for example)
	 * /!\Warning Email and Password are store in the method saveCredentials not in that one
	 * 
	 * @param user
	 *            The user to store
	 */
	private void saveUser(User user) {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();
		// Store user's informations
		// to enable multi-users we add the user hash code to the key
		// int hash =(email.hashcode+password.hashcode)/2.
		int hash = (user.getEmail().hashCode() + user.getPassword().hashCode()) / 2;
		editor.putString(hash + getString(R.string.shCivilite), user.getCivilite());
		editor.putString(hash + getString(R.string.shCompagnie), user.getCompagnie());
		editor.putString(hash + getString(R.string.shNom), user.getNom());
		editor.putString(hash + getString(R.string.shPays), user.getPays());
		editor.putString(hash + getString(R.string.shPrenom), user.getPrenom());
		editor.putString(hash + getString(R.string.shRole), user.getRole());
		editor.putString(hash + getString(R.string.shSiteweb), user.getSiteWeb());
		editor.putString(hash + getString(R.string.shTelFixe), user.getTelFixe());
		editor.putString(hash + getString(R.string.shTelMobile), user.getTelMobile());
		editor.putString(hash + getString(R.string.shType), user.getType());
		editor.putString(hash + getString(R.string.shVille), user.getVille());
		// And commit
		editor.commit();

	}
}

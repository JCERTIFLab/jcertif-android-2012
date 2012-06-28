/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.connection</li>
 * <li>18 mai 2012</li>
 */
package com.jcertif.android.ui.view.connection;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.com.parsing.jackson.service.UsersController;
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
		Log.w("ConnectionActivityLegacy", "Authentification httpGetResponse : |" + httpGetResponse + "|");
		// Be carefull HttpResponse = "null\r" ou "null\n" un truc du genre
		Log.w("ConnectionActivityLegacy", "Authentification HttpTools.isValidHttpResponseCode(httpGetRespStatus) : "
				+ HttpTools.isValidHttpResponseCode(httpGetRespStatus));
		if (httpGetResponse != null && (!httpGetResponse.contains("null"))
				&& (HttpTools.isValidHttpResponseCode(httpGetRespStatus))) {
			Log.w("ConnectionActivityLegacy", "httpGetResponse : is ok");
			// Demander la liste des speakers (chaque speaker est complet)
			try {
				// reconstruire les données
				UsersController userCont = new UsersController(httpGetResponse);
				Log.i("onUpdate", httpGetResponse);
				Boolean initOk = userCont.init();
				if (initOk) {
					User user = userCont.get();
					if (null!=user) {
						// Enregistre l'utilisateur comme valide
						onUserValidated(user);
					} else {
						// Show the error to user
						ShowAuthentFailedDialog();
					}
				}else {
					// Show the error to user
					ShowAuthentFailedDialog();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO When the bypass of the Authent is not required for dev anymore uncomment
			// displayMenuView();
		} else {
			ShowAuthentFailedDialog();
		}
		// TODO to replace in the if when authent is finished to be coded
		displayMainView();
	}

	/**
	 * Show the Dialog authentification failed
	 */
	public void ShowAuthentFailedDialog() {
		Log.w("ConnectionActivityLegacy", "Authentification httpGetResponse : is ko");
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.alertDialogTitle).setMessage(R.string.authenticationErrorMessage)
				.setPositiveButton("OK", null).show();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.ui.view.connection.accountdialogs.AccountDialogParentIntf#onRegisterCallBack
	 * (com.jcertif.android.transverse.model.User)
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
		// Save the user in the application
		onUserValidated(user);
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
	 * This method update the email and Password fields
	 * Update the current user in the application (and doing that store the user information in the
	 * DefaultSharedPreference)
	 * 
	 * @param user
	 *            The user to store
	 */
	private void onUserValidated(User user) {
		// find email
		final EditText edtEmail = (EditText) this.findViewById(R.id.edtEmail);
		final String email = edtEmail.getText().toString();
		// find password
		final EditText edtPassword = (EditText) this.findViewById(R.id.edtPassword);
		final String password = edtPassword.getText().toString();
		// Update the Application current user
		// The user will be stored in the SharedPreference also
		((JCApplication) getApplication()).setUser(user);
	}

}

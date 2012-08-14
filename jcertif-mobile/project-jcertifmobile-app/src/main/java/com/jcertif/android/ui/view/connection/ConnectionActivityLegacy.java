/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.connection</li>
 * <li>18 mai 2012</li>
 */
package com.jcertif.android.ui.view.connection;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.com.parsing.jackson.service.UsersController;
import com.jcertif.android.service.threadtraitements.authent.AuthentThreadCallBack;
import com.jcertif.android.service.threadtraitements.authent.AuthentificationThread;
import com.jcertif.android.transverse.model.User;
import com.jcertif.android.transverse.tools.HttpTools;
import com.jcertif.android.ui.view.connection.accountdialogs.AccountDialog;
import com.jcertif.android.ui.view.connection.accountdialogs.AccountDialogParentIntf;
import com.jcertif.android.ui.view.main.MainActivityHC;
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
	/**
	 * The HoneyComb version level
	 */
	boolean postHC;
	/**
	 * The map that links valid email with their password
	 */
	Map<String, String> emailPassword;
	/**
	 * To know when we are updating password
	 */
	boolean updatingPassword = false;

	/******************************************************************************************/
	/** LifeCycle Methods **************************************************************************/
	/******************************************************************************************/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);
		postHC = getResources().getBoolean(R.bool.postHC);
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
		// if postHC hide the Header
		if (postHC) {
			findViewById(R.id.titleLayout).setVisibility(View.GONE);
		}
		// SharedPreferences preferences =
		// PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		//
		// Editor editor = preferences.edit();
		// // to reload the email/password of the last connected person
		// editor.putString(getString(R.string.shValidEmailsList), "");
		// editor.commit();
		// create the AutoCompleteTextView
		manageAutoCompleteEmail();

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
		// Instantiate the btnSkip's listener
		Button btnSkip = (Button) findViewById(R.id.btnSkip);
		btnSkip.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				onSkipButtonClicked();
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

	/**
	 * Manage the autocomplete to display to the user its email after 3 letters
	 * This method also manage the associated password (it will be autocomplete after 3 letters
	 * also)
	 */
	private void manageAutoCompleteEmail() {
		// find the component
		AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.edtEmail);
		// retrieve the emails list
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		// Load the valid emails list
		String rawEmailsPasswordList = preferences.getString(getString(R.string.shValidEmailsList), "");
		String[] emailsPasswordsList = rawEmailsPasswordList.split(";");
		String[] emailsList;
		emailPassword = new HashMap<String, String>(emailsPasswordsList.length);
		if (rawEmailsPasswordList.length() != 0) {
			emailsList = new String[emailsPasswordsList.length];
			String current;
			String[] currentEmailPass = new String[2];
			for (int i = 0; i < emailsPasswordsList.length; i++) {
				current = emailsPasswordsList[i];
				if (current.contains(",")) {
					currentEmailPass = current.split(",");
					emailsList[i] = currentEmailPass[0];
					emailPassword.put(currentEmailPass[0], currentEmailPass[1]);
				}
			}
		} else {
			// just give something to the arrayAdapter
			emailsList = new String[1];
			emailsList[0] = " ";
		}
		// Define the Adapter (Context, ListView Ressource, The items to display)
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
				emailsList);
		autoCompleteTextView.setAdapter(arrayAdapter);
		manageEdtPassword();
	}

	/**
	 * This method manage the EdtPassword, if the email is known, the user has just to give the
	 * three first letter of its pass word to have an autocomplete
	 */
	private void manageEdtPassword() {
		final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
		edtPassword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (start > 2) {
					autocompletePassWord(s);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * AutoComplete the Password field if the first three letters are the 3 letters of the expected
	 * password
	 * 
	 * @param s
	 *            the value of the editText password
	 */
	private void autocompletePassWord(CharSequence s) {
		if (!updatingPassword) {
			String currentEmail = ((EditText) findViewById(R.id.edtEmail)).getText().toString();
			String associatedPassword = emailPassword.get(currentEmail);
			if (null != associatedPassword) {
				if (s.charAt(0) == associatedPassword.charAt(0) && s.charAt(1) == associatedPassword.charAt(1)
						&& s.charAt(2) == associatedPassword.charAt(2)) {
					((EditText) findViewById(R.id.edtPassword)).setText(associatedPassword);
					updatingPassword = true;
					findViewById(R.id.btnConnect).performClick();
				}
			}
		} else {
			updatingPassword = false;
		}
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

	/**
	 * Skip login and display main activity
	 */
	private void onSkipButtonClicked() {
		// go to the next activity
		displayMainView();
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
					if (null != user) {
						// Enregistre l'utilisateur comme valide
						onUserValidated(user);
						// TODO When the bypass of the Authent is not required for dev anymore
						// uncomment
						// displayMenuView();
					} else {
						// Show the error to user
						ShowAuthentFailedDialog();
					}
				} else {
					// Show the error to user
					ShowAuthentFailedDialog();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

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
		Intent startActivityIntent;
		// launch the Activity according to the version
		if (postHC) {
			startActivityIntent = new Intent(this, MainActivityHC.class);
		} else {
			startActivityIntent = new Intent(this, MainActivityLegacy.class);
		}
		startActivity(startActivityIntent);
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
		// Add that user to the list of valid email for the autoComplete
		// if he is not already in it
		if (!emailPassword.containsKey(user.email)) {
			// Get preferences
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			// Load the valid emails list and update it with the current email
			StringBuilder rawEmailsPass = new StringBuilder(preferences.getString(
					getString(R.string.shValidEmailsList), ""));
			rawEmailsPass.append(user.email);
			rawEmailsPass.append(",");
			// find the password and add it
			EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
			rawEmailsPass.append(edtPassword.getText().toString());
			rawEmailsPass.append(";");

			// Store the new value
			Editor editor = preferences.edit();
			// to reload the email/password of the last connected person
			editor.putString(getString(R.string.shValidEmailsList), rawEmailsPass.toString());
			// editor.putString(getString(R.string.shValidEmailsList), "");
			editor.commit();
		}
		// Update the Application current user
		// The user will be stored in the SharedPreference also
		((JCApplication) getApplication()).setUser(user);
	}

}

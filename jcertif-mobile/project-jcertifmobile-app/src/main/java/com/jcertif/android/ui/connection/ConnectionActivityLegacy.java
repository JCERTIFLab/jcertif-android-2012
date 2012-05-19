/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.connection</li>
 * <li>18 mai 2012</li>
 */
package com.jcertif.android.ui.connection;

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
import com.jcertif.android.ui.connection.accountdialogs.AccountDialog;
import com.jcertif.android.ui.connection.accountdialogs.AccountDialogParent;
import com.jcertif.android.ui.view.MainActivity;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        Display the connection activity for Device which have a version level < HoneyComb
 */
public class ConnectionActivityLegacy extends Activity implements AccountDialogParent, AuthentThreadCallBack {
	// TODO MSE mettre en place les fragments... ou pas...
	// TODO MSE Reflechir à la persistence de l'utilisateur, mais il manque la reponse de l'authent
	// TODO MSE Finir l'authentification (freeze des elements et mise à jour)
	/**
	 * The dialogs to display to the user for registration
	 */
	private AccountDialog accountDialog;
	/**
	 * The thread to call webservices to know is the user is authenticated
	 */
	private AuthentificationThread authThread;

	/******************************************************************************************/
	/** LifeCycle Methods **************************************************************************/
	/******************************************************************************************/

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connection);
		accountDialog = new AccountDialog(this, this);
		authThread = new AuthentificationThread(this);
		// initState();
		loadCredentials();
		addListeners();
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
	 * @param txtEmail
	 * @param txtPassword
	 */
	private void onConnectButtonClicked() {
		// Retrieve the value of the email and password
		final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
		final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		String email = txtEmail.getText().toString();
		String password = txtPassword.getText().toString();
		if (isValidUser(email, password)) {
			// go to the next activity
			// TODO MSE to centralize within a same method
			displayMenuView();
		} else {

			if ((email.length() > 0) && ((password.length() > 0))) {

				// ProgressDialog doesn't like getApplicationContext().
				// That's why we use LoginView.this as the context parameter
				// TODO MSE implement that stuff
				// state.getBinder().getWebServiceData(state, ConnectionActivity.this);
				// save the user's credentials if only it's valid ones/
				// Faut faire attention, il faut sauver les info de l'utilisateur pour les storer
				// dans le defaultshared pref
				// surtout si cet user n'est pas passé par la cas
				// TODO MSE Freeze the GUI and display a infinteprogressBar

				// Launch the thread
				// authThread.execute(email, password);
				Bundle params = new Bundle();
				params.putString(AuthentificationThread.EMAIL_KEY, email);
				params.putString(AuthentificationThread.PASSWORD_KEY, password);
				authThread.execute(this,params);

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
		// Update what is need to be updated
		// !"null".equalsIgnoreCase(httpGetResponse)
		if (httpGetResponse!=null&&(!httpGetResponse.equalsIgnoreCase("null"))) {
			saveCredentials();
			// TODO here I should be able to update the user
			// saveUser();
			//JCApplication.updateUser
			// displayMenuView();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.alertDialogTitle).setMessage(R.string.authenticationErrorMessage)
					.setPositiveButton("OK", null).show();
		}
		// TODO MSE to replace in the if
		displayMenuView();
	}

	/******************************************************************************************/
	/** Display the Menu ************************************/
	/******************************************************************************************/

	/**
	 * Display the Menu View
	 */
	private void displayMenuView() {
		Intent intentView = new Intent(getApplicationContext(), MainActivity.class);
		startActivityForResult(intentView, 0);
	}

	/******************************************************************************************/
	/** SharedPreference Management **************************************************************************/
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
			final TextView loginView = (TextView) this.findViewById(R.id.txtEmail);
			loginView.setText(email);
		}
		if (password != null) {
			final TextView passwordView = (TextView) this.findViewById(R.id.txtPassword);
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
		final TextView txtEmail = (TextView) this.findViewById(R.id.txtEmail);
		final String email = txtEmail.getText().toString();
		// find password
		final TextView txtPassword = (TextView) this.findViewById(R.id.txtPassword);
		final String password = txtPassword.getText().toString();
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
	public void saveUser(User user) {
		// Get preferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Editor editor = preferences.edit();

		// Store user's informations
		// to enable multi-users we add the user hash code to the key
		// int hash = user.hashCode();<-Pose probleme pour les données revenant d'ailleurs, a
		// reflechir encore.
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
		// And update the user in the application
		((JCApplication) getApplication()).updateUser(hash);
	}
	
	
}

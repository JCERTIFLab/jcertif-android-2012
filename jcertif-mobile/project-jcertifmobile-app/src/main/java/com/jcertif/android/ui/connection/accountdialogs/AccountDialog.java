/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.connection.accountdialogs</li>
 * <li>18 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.jcertif.android.ui.connection.accountdialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.transverse.model.User;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to create and displays the subscribe dialogs to the user.
 *        There are two dialogs: The first one register email and password
 *        The second one register sex/First and Last name/ and role and type
 */
public class AccountDialog {
	//TODO MSE finir cette classe

	/**
	 * The calling activity context
	 */
	Context context = null;
	/**
	 * The createAccountFirstDialog
	 * It displays the Email and the Password
	 */
	Dialog createAccountFirstDialog = null;

	/**
	 * The createAccountSecondDialog
	 * It displays The sex/First and Last name/ and role and type of the account
	 */
	Dialog createAccountSecondDialog = null;
	/**
	 * The user that will be create according to the data gave to the dialogs
	 */
	User user = null;
	/**
	 * The parent that has in charge the saveUser(User) method
	 */
	AccountDialogParent parent=null;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * The constructor aims to save the context to be used when creating the Dialog
	 * 
	 * @param context
	 */
	public AccountDialog(Context context,AccountDialogParent parent) {
		super();
		this.context = context;
		this.parent=parent;
		user=new User();
	}

	/******************************************************************************************/
	/** Public Methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * Show the Dialog to register an user (create its account)
	 */
	public void showAccountDialog() {
		// insure the Dialog is built
		if (createAccountFirstDialog == null) {
			
			buildAccountFirstDialog();
			// Hide the textError (juts in case)
			TextView txvError = ((TextView) createAccountFirstDialog.findViewById(R.id.textError));
			txvError.setVisibility(View.GONE);
		}
		// then show it:
		createAccountFirstDialog.show();
	}

	/**
	 * Call this method when you're sure that the dialog won't be reused:
	 * When the user go in the main stream of the application
	 */
	public void dismissDialog() {
		createAccountFirstDialog.dismiss();
		createAccountSecondDialog.dismiss();
		createAccountFirstDialog = null;
		createAccountSecondDialog = null;
	}

	/******************************************************************************************/
	/** First Dialogs Methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * BuildTheAccountProfile Dialog
	 */
	private void buildAccountFirstDialog() {
		// Create the dialog : Define its main caracteristics
		createAccountFirstDialog = new Dialog(context);
		createAccountFirstDialog.setContentView(R.layout.account_profile_dialog);
		createAccountFirstDialog.setTitle(R.string.accountProfileLabel);

		// Manage the cancel button
		createAccountFirstDialog.setCancelable(true);
		// TODO MSE Ne sert a rien a vérifier quand ca marche puis a detruire
		Button btnCancel = (Button) createAccountFirstDialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createAccountFirstDialog.cancel();
			}
		});

		// Manage the next button:Display
		Button btnNext = (Button) createAccountFirstDialog.findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				firstDialogOnNextCalled();
			}
		});
	}

	/**
	 * Called when the next button of the first dialog is clic
	 * This method check the email/password oif the user and displays the Second Dialog
	 */
	private void firstDialogOnNextCalled() {
		// Retrieve the Email and Password elements:
		String email1 = ((EditText) createAccountFirstDialog.findViewById(R.id.txtEmail)).getText().toString();
		String email2 = ((EditText) createAccountFirstDialog.findViewById(R.id.txtEmailConfirmation)).getText()
				.toString();
		String password1 = ((EditText) createAccountFirstDialog.findViewById(R.id.txtPassword)).getText().toString();
		String password2 = ((EditText) createAccountFirstDialog.findViewById(R.id.txtPasswordConfirmation)).getText()
				.toString();
		// Do someLog
		logEmailPassword(email1, email2, password1, password2);
		// Manage the constraints of equality
		if (password1.equals(password2) && email1.equals(email2)) {
			// set the user email and password
			user.setEmail(email1);
			user.setPassword(password1);
			// display the next dialog
			if (createAccountSecondDialog == null) {
				buildCreateAccountSecondDialog();
			}
			createAccountSecondDialog.show();
			// hide that one
			createAccountFirstDialog.hide();
		} else {
			// display an error to the user
			StringBuilder strB = new StringBuilder();
			// Find the message to display
			if (!password1.equals(password2)) {
				strB.append(context.getString(R.string.accountPasswordErrorMessage));
			} else {
				strB.append(context.getString(R.string.accountEmailErrorMessage));
			}
			TextView txvError = ((TextView) createAccountFirstDialog.findViewById(R.id.textError));
			txvError.setText(strB.toString());
			txvError.setVisibility(View.VISIBLE);
		}
	}

	/******************************************************************************************/
	/** Second Dialogs Methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * This method build the second dialog that displays a from for the user to fill it gender/first
	 * and last name/type and role
	 */
	private void buildCreateAccountSecondDialog() {
		// Build the second dialog and its main characteristics
		createAccountSecondDialog = new Dialog(context);
		createAccountSecondDialog.setContentView(R.layout.account_information_dialog);
		createAccountSecondDialog.setTitle(R.string.accountInformationLabel);

		// Manage the cancel button
		createAccountSecondDialog.setCancelable(true);
		// TODO MSE Ne sert a rien a vérifier quand ca marche puis a detruire
		Button btnCancel = (Button) createAccountSecondDialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				createAccountSecondDialog.cancel();
			}
		});

		// Manage the spinners

		// displays civility spinner
		final Spinner spnCivility = (Spinner) createAccountSecondDialog.findViewById(R.id.spnCivility);
		ArrayAdapter<CharSequence> civilityAdapter = ArrayAdapter.createFromResource(context, R.array.civility_array,
				android.R.layout.simple_spinner_item);
		civilityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCivility.setAdapter(civilityAdapter);

		// displays role spinner
		final Spinner spnRole = (Spinner) createAccountSecondDialog.findViewById(R.id.spnRole);
		ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(context, R.array.role_array,
				android.R.layout.simple_spinner_item);
		roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnRole.setAdapter(roleAdapter);

		// displays type spinner
		final Spinner spnType = (Spinner) createAccountSecondDialog.findViewById(R.id.spnType);
		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(context, R.array.type_array,
				android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnType.setAdapter(typeAdapter);

		// Manage the save button

		Button btnSave = (Button) createAccountSecondDialog.findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Retrieve the civility role and type
				String civility = spnCivility.getSelectedItem().toString();
				String role = spnRole.getSelectedItem().toString();
				String type = spnType.getSelectedItem().toString();
				// update the user
				updateUser(civility, role, type);
				// hide the dialog
				createAccountSecondDialog.hide();
			}
		});
	}

	/**
	 * Update the user object accordin to the SecondDialog elements and the parameter
	 * @param civility User's civility
	 * @param role User's role
	 * @param type User's type
	 */
	private void updateUser(String civility, String role, String type) {
		//Set the user attributes:
		user.setCivilite(civility);
		user.setPrenom(((EditText) createAccountSecondDialog.findViewById(R.id.txtFirstName)).getText().toString());
		user.setNom(((EditText) createAccountSecondDialog.findViewById(R.id.txtLastName)).getText().toString());
		user.setRole(role);
		user.setType(type);
		//Register the user using WebServices
		registerUser();
		//Make some log
		Log.i(JCApplication.NAME, user.toString());
	}

	/**
	 * Register user using a webServices
	 */
	private void registerUser() {
		//TODO implement this method
		// Web service call
		try {
			// String data = mService.registerUser(user);
			//
			// Log.i(JCApplication.NAME, "User data : " + data);

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

	/******************************************************************************************/
	/** Log methods **************************************************************************/
	/******************************************************************************************/

	/**
	 * Log the following parameters
	 * @param email1
	 * @param email2
	 * @param password1
	 * @param password2
	 */
	private void logEmailPassword(String email1, String email2, String password1, String password2) {
		Log.i(JCApplication.NAME, "pwd1 : " + password1);
		Log.i(JCApplication.NAME, "pwd2 : " + password2);

		Log.i(JCApplication.NAME, "email1 : " + email1);
		Log.i(JCApplication.NAME, "email2 : " + email2);
	}

}

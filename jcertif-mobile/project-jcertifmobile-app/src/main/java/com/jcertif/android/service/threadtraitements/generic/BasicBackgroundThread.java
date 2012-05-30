/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.threadtraitements</li>
 * <li>19 mai 2012</li>
 * 
 * <li>======================================================</li>
 */
package com.jcertif.android.service.threadtraitements.generic;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public abstract class BasicBackgroundThread {
	/******************************************************************************************/
	/** Abstract Methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * The method to implement
	 */
	public abstract void workingMethod(Handler handler);

	/**
	 * The key to use to retrieve the response
	 */
	public static final String RESPONSE_KEY = "response";
	/**
	 * The key to use to retrieve the response's status
	 */
	public static final String STATUS_KEY = "status";
	/**
	 * The value set to a status when the status doesn't exist
	 */
	public static final int STATUS_NOTSET = -1;
	/******************************************************************************************/
	/** Managing the Handler and the Thread *************************************************/
	/******************************************************************************************/
	/**
	 * The Handler
	 */
	private Handler handler;
	/**
	 * The thread
	 */
	protected Thread backgroundThread;
	/**
	 * The parameters to do the work
	 */
	protected Bundle params;
	/**
	 * The progressBar
	 */
	ProgressDialog progressDialog;
	/******************************************************************************************/
	/** Others attributes **************************************************************************/
	/******************************************************************************************/
	/**
	 * The string for the log
	 */
	private final static String TAG = "BasicBackgroundThread";
	/**
	 * The call back object (the caller and the one to prevent)
	 */
	private BasicBackgroundCallBack callBack;

	/******************************************************************************************/
	/** Constructor **************************************************************************/
	/******************************************************************************************/

	/** Called when the activity is first created. */
	public BasicBackgroundThread(BasicBackgroundCallBack mCallBack) {
		callBack = mCallBack;
		// handler definition
		handler = new Handler() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.Handler#handleMessage(android.os.Message)
			 */
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Log.d(TAG, "handle message called ");
				if (getCallBack() != null) {
					getCallBack().handleMessage(msg);
				}
				if(progressDialog!=null) {
					progressDialog.dismiss();
				}
			}
		};

	}

	/**
	 * launch the backgroundThread
	 * Use the bundle to pass parameters
	 * 
	 * @param mParams
	 *            Parameters to use for the method
	 */
	public void execute(Bundle mParams) {
		executeWorkingMethod(mParams);
		progressDialog=null;
	}

	/**
	 * launch the backgroundThread
	 * Use the bundle to pass parameters
	 * Display a DialogProgressBar
	 * 
	 * @param mParams
	 *            Parameters to use for the method
	 */
	public void execute(Context activity, Bundle mParams) {
		executeWorkingMethod(mParams);
		// Affichage de la fenêtre d'attente
		buildProgressBarInde(activity).show();
	}

	/**
	 * launch the backgroundThread
	 * 
	 * @param mParams
	 *            Parameters to use for the method
	 */
	private void executeWorkingMethod(Bundle mParams) {
		// The params to be used during the threatment
		this.params = mParams;
		// use a random double to give a name to the thread
		final double random = Math.random();
		// Define the Thread and the link with the handler
		backgroundThread = new Thread(new Runnable() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				try {
					Log.v(TAG, "NewThread " + random);
					workingMethod(handler);
				} catch (Throwable t) {
					// just end the background thread
				}
			}
		});
		backgroundThread.setName("BasicBackgroundThread " + random);
		// start the thread
		backgroundThread.start();
	}

	/**
	 * @return a ProgressDialog indeterminate
	 */
	private Dialog buildProgressBarInde(Context context) {
		// instanciate it
		progressDialog = new ProgressDialog(context);
		// define it style and its title and message
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setTitle(JCApplication.getInstance().getString(R.string.waitingDialogTitle));
		progressDialog.setMessage(JCApplication.getInstance().getString(R.string.wiatingDialogMessage));
		// can be killed by the user using the back touch
		// Else you need to close it when the treatment is finished
		progressDialog.setCancelable(true);
		// then you can manage the progress bar using:
		progressDialog.setIndeterminate(true);
		// but in fact you should use an Handler or an AsyncTask to do that
		return progressDialog;
	}

	/******************************************************************************************/
	/** Manage CallBack **************************************************************************/
	/******************************************************************************************/

	/**
	 * When an activity or a service is destroyed it have to call this method on the
	 * BackgroundThread
	 * 
	 * @param callBack
	 */
	public void unbindCallBack() {
		this.callBack = null;
	}

	/**
	 * When an activity is recreate (not create but recreate) and has launch a backgroundThread
	 * It can use this method to bind it again to the thread
	 * 
	 * @param callBack
	 */
	public void bindCallBack(BasicBackgroundCallBack mCallBack) {
		this.callBack = mCallBack;
	}

	/**
	 * @return the callback
	 */
	protected BasicBackgroundCallBack getCallBack() {
		return callBack;
	}
}

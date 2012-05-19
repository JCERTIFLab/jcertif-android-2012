/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices.webcalls</li>
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
package com.jcertif.android.service.androidservices.webcalls;

import com.jcertif.android.JCApplication;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.ui.view.R;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to establish a call to the webServer to authenticate the user
 */
public class TOTOService extends Service {
	/******************************************************************************************/
	/** Managing the Communication between the service and its listeners Constants *************/
	/******************************************************************************************/
	/**
	 * The key of the Intent to communicate with the service's users
	 */
	// TODO MSE extract that string
	public static final String AUTHENTIFICATOR_SERVICE_INTENT = "com.android2ee.service.MyUniqueItentServiceKey";
	
	/******************************************************************************************/
	/** Managing LifeCycle **************************************************************************/
	/******************************************************************************************/
	/**
	 * Managing initialization and death of the service
	 */
	public void onCreate() {
		super.onCreate(); // initialize your service here
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	public void onDestroy() {
		super.onDestroy(); // destroy your service here
	}

	/******************************************************************************************/
	/** Managing the binder to this service *************************************************/
	/******************************************************************************************/

	/**
	 * The binder that glues to my service
	 */
	private final Binder binder = new LocalBinder();

	/**
	 * @goals This class aims to define the binder to use for my service
	 */
	public class LocalBinder extends Binder {
		/*** @return the service you want to bind to : i.e. this */
		public TOTOService getService() {
			return (TOTOService.this);
		}
	}

	/**
	 * return the binder that glues to my service
	 */
	public IBinder onBind(Intent intent) {
		return binder;
	}

	/******************************************************************************************/
	/** Managing the backgroundTask *************************************************/
	/******************************************************************************************/

	/**
	 * This class aims to make a task in a separated thread
	 */
	class MyTaskInAnOtherThread extends AsyncTask<Location, Void, Void> {
		protected Void doInBackground(Location... locs) {
//			Intent broadcastIntent = new Intent(AUTHENTIFICATOR_SERVICE_INTENT);
//			String name=getString(R.string.servAuthentIntentExtraName);
//			String responseString = null;
////			String url = JCApplication.AUTHENTICATION_URL + "/" + JCApplication.EMAIL + "/" + JCApplication.PASSWORD + "/2";
//			RestClient client = new RestClient(url);
//			try {
//				client.Execute(RequestMethod.GET);
//				responseString = client.getResponse();
//				Log.i(this.getClass().getSimpleName(), responseString);
//				broadcastIntent.putExtra(name, responseString);
//			} catch (Exception e) {
//				broadcastIntent.putExtra(name, "An error occurs");
//				Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
//			}finally {
//				sendBroadcast(broadcastIntent);
//			}
			return (null);
		}

		protected void onProgressUpdate(Void... unused) {
			// not needed here
		}

		protected void onPostExecute(Void unused) {
			// not needed here
		}
	}
}

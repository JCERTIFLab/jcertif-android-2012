package com.jcertif.android.service.androidservices;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Binder;
import android.util.Log;

import com.jcertif.android.Application;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.service.androidservices.State.Operation;
import com.jcertif.android.transverse.model.User;
import com.jcertif.android.ui.view.JCertifDialog;

/**
 * The Binder abstracts all calls to web services It is instantiated when the
 * Activity is binding a Service
 * 
 * @author yakhya.dabo
 * 
 */
public class LocalServiceBinder<T> extends Binder {

	private JCertifService<T> service = null; 
	
	public LocalServiceBinder(JCertifService<T> service){
		this.setService(service);
	}
	
	void onCreate(Context ctxt) {
		// client=new DefaultHttpClient();
		// format=ctxt.getString(R.string.url);
	}

	void onDestroy() {
		// client.getConnectionManager().shutdown();
	}

	public void getWebServiceData(State<T> state, Context context) {
		new JCertifAsyncTask<T>(state, context).execute();
	}

	class JCertifAsyncTask<E> extends AsyncTask<Void, Void, E> {

		private JCertifDialog jCertifDialog;
		protected Context applicationContext;
		State<E> state = null;
		Exception e = null;

		public JCertifAsyncTask(State<E> state, Context context) {
			this.state = state;
			this.applicationContext = context;
		}

		@Override
		protected void onPreExecute() {
			Log.i(Application.NAME, this.getClass().getSimpleName()
					+ " : pre executing ...");
			if (state.getOperation().equals(Operation.LOAD)) {
				this.jCertifDialog = JCertifDialog.show(applicationContext,
						Application.NAME, "Getting data...", true);
			}
		}

		@Override
		protected E doInBackground(Void... params) {
			Log.i(this.getClass().getSimpleName(), " doing in background ...");
			try {
				//if (state.getOperation().equals(Operation.LOAD)) {
					return (E) state.getData();
//				} else if (state.getOperation().equals(Operation.REMOVE)) {
//					return null;
//				} else {
//					return null;
//				}
			} catch (Exception e) {
				this.e = e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(E result) {
			if (e == null) {
				//if (state.getOperation().equals(Operation.LOAD)) {
					state.getStateListener().onDataAvailable(result);
//				} else {
//					state.getStateListener().onDataChangeSuccess(
//							state.getParam());
//				}
			} else {
				state.getStateListener().onError(e);
			}
			if (this.jCertifDialog != null) {
				this.jCertifDialog.cancel();
			}
		}
	}

	// Web services call
	
    public String authenticateUser() throws Exception{
    	String responseString = null;
		String url = Application.AUTHENTICATION_URL + "/" + Application.EMAIL + "/" + Application.PASSWORD + "/2";
		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
    } 
    
    public String registerUser(User user) throws Exception{
		String responseString = null;
		String url = Application.REGISTER_URL;

		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.POST);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
    } 
    
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getEventList() throws Exception {
		String responseString = null;
		String url = Application.EVENT_URL;

		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSpeakerList() throws Exception {
		String responseString = null;
		String url = Application.SPEAKER_URL;

		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}
	
	public JCertifService<T> getService() {
		return service;
	}

	public void setService(JCertifService<T> service) {
		this.service = service;
	}
	
}
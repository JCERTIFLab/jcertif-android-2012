package com.jcertif.android.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Binder;
import android.util.Log;

import com.jcertif.android.app.Application;
import com.jcertif.android.net.RestClient;
import com.jcertif.android.net.RestClient.RequestMethod;
import com.jcertif.android.service.State.Operation;
import com.jcertif.android.view.JCertifDialog;

/**
 * The Binder abstracts all calls to web services It is instantiated when the
 * Activity is binding a Service
 * 
 * @author yakhya.dabo
 * 
 */
public class LocalServiceBinder<T> extends Binder {

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
	
	// private final RestClient client = new RestClient();
	//
	// public String getProjectDetail(String projectName) throws Exception{
	// String responseString = null;
	// String url = Application.PROJECT_DETAIL;
	//
	// RestClient client = new RestClient(url);
	// client.AddParam("projectName", projectName);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : " +
	// e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String addInMyProject(String projectName) throws Exception{
	// String responseString = null;
	// String url = Application.MY_PROJECT_ADD;
	//
	// RestClient client = new RestClient(url);
	// client.AddParam("projectName", projectName);
	//
	// try {
	// client.Execute(RequestMethod.POST);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String removeFromMyProject(String projectName) throws Exception{
	// String responseString = null;
	// String url = Application.MY_PROJECT_REMOVE;
	//
	// RestClient client = new RestClient(url);
	// client.AddParam("projectName", projectName);
	//
	// try {
	// client.Execute(RequestMethod.POST);
	// responseString = client.getResponse();
	//
	//
	// Log.i(this.getClass().getSimpleName(),"Removing project, " + projectName
	// + " " + responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String getPerformanceData() throws Exception{
	// String url = Application.PERFORMANCE_URL ;
	// String responseString = getDataFromURL(url);
	// return responseString;
	// }
	//
	// public String getMainFactData() throws Exception{
	// String url = Application.MAIN_FACT_URL ;
	// String responseString = getDataFromURL(url);
	// return responseString;
	// }
	//
	// public String getClosedProjectData() throws Exception{
	// String url = Application.CLOSED_PROJECT_URL ;
	// String responseString = getDataFromURL(url);
	// return responseString;
	// }
	//
	// public String getNewProjectData() throws Exception{
	// String url = Application.NEW_PROJECT_URL ;
	// String responseString = getDataFromURL(url);
	// return responseString;
	// }
	//
	// public String getCurrentRAOData() throws Exception{
	// String url = Application.CURRENT_RAO;
	// String responseString = getDataFromURL(url);
	// return responseString;
	// }
	//
	// private String getDataFromURL(String url) throws Exception{
	// String responseString = null;
	//
	// RestClient client = new RestClient(url);
	//
	// if (!Application.zoneName.equalsIgnoreCase(Application.WORLD)){
	// client.appendPath(Application.zonePath);
	// client.AddParam(Application.zoneParam , Application.zoneName);
	// }
	//
	// Log.i(Application.NAME, "Service.getDataFromURL : zoneParam : " +
	// Application.zoneParam);
	// Log.i(Application.NAME, "Service.getDataFromURL : zonePath : " +
	// Application.zonePath);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : "+
	// e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String getWorldProjectData() throws Exception{
	// String responseString = null;
	//
	// String url = Application.WORLD_PROJECT;
	//
	// RestClient client = new RestClient(url);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String getSearchData(String searchParam) throws Exception{
	// String responseString = null;
	//
	// String url = Application.SEARCH;
	//
	// RestClient client = new RestClient(url);
	//
	// client.AddParam("searchParam", searchParam);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String getMyProjectData() throws Exception{
	// String responseString = null;
	//
	// String url = Application.MY_PROJECT_LIST;
	//
	// RestClient client = new RestClient(url);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	// public String getGeographicZoneData() throws Exception{
	// String responseString = null;
	//
	// String url = Application.GEOGRAPHIC_ZONE_URL;
	//
	// RestClient client = new RestClient(url);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString;
	// }
	//
	//
	//
	// public String authenticateUser() throws Exception{
	// String responseString = null;
	//
	// String url = Application.LOGIN_URL;
	//
	// RestClient client = new RestClient(url);
	//
	// try {
	// client.Execute(RequestMethod.GET);
	// responseString = client.getResponse();
	// Log.i(this.getClass().getSimpleName(), responseString);
	// } catch (Exception e) {
	// Log.e(this.getClass().getSimpleName(),
	// "LocalServiceBinder : "+e.getMessage());
	// throw e;
	// }
	//
	// return responseString.trim();
	// }
}
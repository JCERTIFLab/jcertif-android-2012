/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices.staredevents</li>
 * <li>10 juil. 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : Mathias Seguy Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.service.androidservices.staredevents;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.dao.StaredEventsDaoFactory;
import com.jcertif.android.dao.intf.StaredEventsDaoIntf;
import com.jcertif.android.service.androidservices.UpdaterServiceElementIntf;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class StaredEventsUpdater implements UpdaterServiceElementIntf {
	/**
	 * The updaterService parent
	 */
	Handler parent;

	/*
	 * Pour rattracher gérer les events, tu as des services pour ça.
	 */

	/**
	 * @param parent
	 */
	public StaredEventsUpdater(Handler parent) {
		super();
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#onUpdate()
	 */
	@Override
	public void onUpdate() {
		// FindUserEmail
		String email = JCApplication.getInstance().getUser().getEmail();
		// find the Dao
		StaredEventsDaoIntf dao = StaredEventsDaoFactory.getDao();
		Log.w("onUpdate StaredEvents", "onUpdate StaredEvents called");
		Log.w("onUpdate StaredEvents", "onUpdate email=" + email);
		// Demander la liste des speakers (chaque speaker est complet)
		try {
			// find the delta between the local and the server list
			// that occurs since the last update
			List<Integer> serverKnowStaredEvt = dao.getServerKnownStaredEvents();
			List<Integer> localStaredEvt = dao.getStaredEvents();
			List<Integer> eventsToAdd = findEventToAdd(localStaredEvt, serverKnowStaredEvt);
			List<Integer> eventsToRemove = findEventToRemove(localStaredEvt, serverKnowStaredEvt);
			// add elements to add
			for (Integer eventToAdd : eventsToAdd) {
				addStaredEvents(email, eventToAdd.toString());
			}
			// remove elements to remove
			for (Integer eventsToRem : eventsToRemove) {
				removeStaredEvents(email, eventsToRem.toString());
			}
			// retrieve the server list and store elements in prefs
			String serverEvtsList = getStaredEventsList(email);
			// and stored it as local and server stared List (this is the trick)
			String listStaredEvents=fromJson(serverEvtsList);
			dao.setServerStaredEvents(listStaredEvents);
			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// The threatment is over, callBack the parent to tell it
			parent.sendEmptyMessage(0);
			Log.w("onUpdate StaredEvents", "onUpdate StaredEvents finished");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#getName()
	 */
	@Override
	public String getName() {
		return JCApplication.getInstance().getApplicationContext().getString(R.string.staredEventsUpdater);
	}

	/******************************************************************************************/
	/** Delta mthods **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param localList
	 * @param serverList
	 * @return
	 */
	private List<Integer> findEventToAdd(List<Integer> localList, List<Integer> serverList) {
		List<Integer> evtToAdd = new ArrayList<Integer>();
		// There are elements that belongs to the localList and not to the server list
		for (Integer localEvt : localList) {
			if (!serverList.contains(localEvt)) {
				evtToAdd.add(localEvt);
			}
		}
		return evtToAdd;
	}

	/**
	 * @param localList
	 * @param serverList
	 * @return
	 */
	private List<Integer> findEventToRemove(List<Integer> localList, List<Integer> serverList) {
		List<Integer> evtToRemove = new ArrayList<Integer>();
		// There are elements that belongs to the serverList and not to the local list
		for (Integer srvEvt : serverList) {
			if (!localList.contains(srvEvt)) {
				evtToRemove.add(srvEvt);
			}
		}
		return evtToRemove;
	}

	/******************************************************************************************/
	/** HTTP calls **************************************************************************/
	/******************************************************************************************/

	/**
	 * @return the Http response of the web services
	 * @throws Exception
	 */
	private String getStaredEventsList(String email) throws Exception {
		String responseString = null;
		String url = JCApplication.getInstance().getUrlFactory().getGetStaredEventsURL(email);
		Log.e(this.getClass().getSimpleName(), "Calling url : " + url);
		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.e(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

	/**
	 * @return the Http response of the web services
	 * @throws Exception
	 */
	private String addStaredEvents(String email, String eventId) throws Exception {
		String responseString = null;
		String url = JCApplication.getInstance().getUrlFactory().getAddStaredEventURL(email, eventId);
		Log.e(this.getClass().getSimpleName(), "Calling url : " + url);
		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName()+"addStaredEvents : ", responseString);
		} catch (Exception e) {
			Log.e(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

	/**
	 * @return the Http response of the web services
	 * @throws Exception
	 */
	private String removeStaredEvents(String email, String eventId) throws Exception {
		String responseString = null;
		String url = JCApplication.getInstance().getUrlFactory().getRemoveStaredEventURL(email, eventId);
		Log.e(this.getClass().getSimpleName(), "Calling url : " + url);
		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.e(this.getClass().getSimpleName()+"removeStaredEvents: ", responseString);
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

	/**
	 * @param json
	 * @return
	 */
	private String fromJson(String json) {
		try {
			JSONObject jsonobj = new JSONObject(json);
			String ret=jsonobj.getString("value");
			Log.e(this.getClass().getSimpleName() + "fromJson", "return string:" + jsonobj.getString("value"));
			return ret;
		} catch (JSONException e) {
			Log.e(this.getClass().getSimpleName() + "fromJson", "exception occurs" + e);
		}
		return "";
	}

	/**
	 * @param json
	 * @return
	 */
	private JSONObject toJson(String listOfStaredEvts) {
		JSONObject jsonObjects = new JSONObject();
		try {
			jsonObjects.put("value", listOfStaredEvts);
		} catch (JSONException e) {
			Log.e(this.getClass().getSimpleName() + "toJson", "exception occurs" + e);
		}
		return jsonObjects;
	}

}

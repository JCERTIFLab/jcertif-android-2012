/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.dao.prefs</li>
 * <li>10 juil. 2012</li>
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
package com.jcertif.android.dao.prefs;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.dao.intf.StaredEventsDaoIntf;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to store and retrieve the stared events.
 *        This Dao stores this information inside the SharedPrefs object.
 *        If you want to change, just implements StaredEventsDaoIntf
 *        and change the StaredEventsDaoFactory
 */
public class StaredEventsDaoPrefs implements StaredEventsDaoIntf {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.dao.intf.StaredEventsDaoIntf#getStaredEvents()
	 */
	@Override
	public List<Integer> getStaredEvents() {
		JCApplication app = JCApplication.getInstance();
		String userKey=app.getUserKey();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
		String evtList = preferences.getString(userKey+app.getString(R.string.shLocalStaredEvents), "-1");
		Log.e("StaredEventsDaoPrefs:getStaredEvents", evtList);
		if (evtList.length()==0 || "-1".equals(evtList)) {
			return new ArrayList<Integer>();
		} else {
			// unmarshall the value
			return fromStringToList(evtList);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.jcertif.android.dao.intf.StaredEventsDaoIntf#getServerKnownStaredEvents()
	 */
	@Override
	public List<Integer> getServerKnownStaredEvents(){
		JCApplication app = JCApplication.getInstance();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
		String userKey=app.getUserKey();
		String evtList = preferences.getString(userKey+app.getString(R.string.shServerStaredEvents), "-1");
		Log.e("StaredEventsDaoPrefs:getServerKnownStaredEvents", evtList);
		if ("-1".equals(evtList)) {
			return new ArrayList<Integer>();
		} else {
			// unmarshall the value
			return fromStringToList(evtList);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.jcertif.android.dao.intf.StaredEventsDaoIntf#setServerStaredEvents(java.lang.String)
	 */
	@Override
	public Boolean setServerStaredEvents(String staredEventsList) {
		JCApplication app = JCApplication.getInstance();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
		String userKey=app.getUserKey();
		SharedPreferences.Editor editor = preferences.edit();
		Log.e("StaredEventsDaoPrefs:setServerStaredEvents", staredEventsList);
		editor.putString(userKey+app.getString(R.string.shServerStaredEvents), staredEventsList);
		//and the trick, as this method is only called by the updater and as the updater has just done the add and remove operation
		//the server list and the local list are equal:
		editor.putString(userKey+app.getString(R.string.shLocalStaredEvents), staredEventsList);
		Boolean commitOk= editor.commit();
		//then be sure the application known an update occurs
		JCApplication.getInstance().setStaredEventsUpdatedFromServer();
		//
		return commitOk;
	}

	/**
	 * Stroe the new events list in the sharedPrefs
	 * 
	 * @return
	 */
	public Boolean setStaredEvents(List<Integer> staredEvts) {
		JCApplication app = JCApplication.getInstance();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
		String userKey=app.getUserKey();
		SharedPreferences.Editor editor = preferences.edit();
		Log.e("StaredEventsDaoPrefs:setStaredEvents", fromListToString(staredEvts));
		editor.putString(userKey+app.getString(R.string.shLocalStaredEvents), fromListToString(staredEvts));
		return editor.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.dao.intf.StaredEventsDaoIntf#removeFromStaredElement(java.lang.Integer)
	 */
	@Override
	public boolean removeFromStaredElement(Integer eventId) {
		boolean ret = false;
		// update the current list of stared elements
		List<Integer> stEvents = getStaredEvents();
		if (stEvents.contains(eventId)) {
			// first remove the element
			stEvents.remove(eventId);
			// then persist the information
			ret = setStaredEvents(stEvents);
		}
		// first retrieve the list of stared element
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.dao.intf.StaredEventsDaoIntf#addToStaredElement(java.lang.Integer)
	 */
	@Override
	public boolean addToStaredElement(Integer eventId) {
		boolean ret = false;
		// update the current list of stared elements
		List<Integer> stEvents = getStaredEvents();
		if (!stEvents.contains(eventId)) {
			// first add the element
			stEvents.add(eventId);
			// then persist the information
			ret = setStaredEvents(stEvents);
		}
		// first retrieve the list of stared element
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.dao.intf.StaredEventsDaoIntf#removeFromStaredElements(java.util.List)
	 */
	@Override
	public boolean removeFromStaredElements(List<Integer> eventsId) {
		boolean ret = false;
		// update the current list of stared elements
		List<Integer> stEvents = getStaredEvents();
		for (Integer evtToRemove : eventsId) {
			if (stEvents.contains(evtToRemove)) {
				// first remove the element
				stEvents.remove(evtToRemove);
			}
		}
		// then persist the information
		ret = setStaredEvents(stEvents);
		// first retrieve the list of stared element
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.dao.intf.StaredEventsDaoIntf#addToStaredElements(java.util.List)
	 */
	@Override
	public boolean addToStaredElements(List<Integer> eventsId) {
		boolean ret = false;
		// update the current list of stared elements
		List<Integer> stEvents = getStaredEvents();
		for (Integer evtToAdd : eventsId) {
			if (!stEvents.contains(evtToAdd)) {
				// first add the element
				stEvents.add(evtToAdd);
			}
		}// then persist the information
		ret = setStaredEvents(stEvents);
		// first retrieve the list of stared element
		return ret;
	}

	/******************************************************************************************/
	/** Marshall/unsmarshall methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * The token to use to split the string
	 */
	private final String token = ",";

	/**
	 * @param staredElements
	 * @return the listOf stared events
	 */
	private List<Integer> fromStringToList(String staredElements) {
		// Split the string
		String[] splittedString = staredElements.split(token);
		// instanciate the list
		List<Integer> retList = new ArrayList<Integer>(splittedString.length);
		// build the list
		
		for (int i = 0; i < splittedString.length; i++) {
			Log.e("StaredEventsDaoPrefs:fromStringToList", "element["+i+"]"+splittedString[i]);
			try{
			retList.add(Integer.decode(splittedString[i]));
			}catch(NumberFormatException e) {
				Log.e("StaredEventsDaoPrefs:fromStringToList", "NumberFormatException with"+splittedString[i]);
			}
		}
		// return
		return retList;
	}

	/**
	 * @param staredElements
	 * @return the String stared events
	 */
	private String fromListToString(List<Integer> staredElements) {
		// Split the string
		StringBuilder strB = new StringBuilder();
		for (Integer elm : staredElements) {
			strB.append(elm.toString());
			strB.append(token);
		}
		// return
		return strB.toString();
	}

}

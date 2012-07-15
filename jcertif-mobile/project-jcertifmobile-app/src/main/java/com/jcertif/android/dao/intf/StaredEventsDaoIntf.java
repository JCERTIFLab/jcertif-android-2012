/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.dao.intf</li>
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
package com.jcertif.android.dao.intf;

import java.util.List;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public interface StaredEventsDaoIntf {
	/**
	 * @return the list of stared events
	 */
	public List<Integer> getStaredEvents();
	/**
	 * @return the list of stared events known as the server ones
	 * Don't make http call
	 * Just retrieve the list of known stared events from server stored in the prefs at the last http Update
	 */
	public List<Integer> getServerKnownStaredEvents();

	/**
	 * Save the list of the stared events that are on the server
	 * Only UpdaterService should call this
	 * @param staredEventsList the list of stared events on the server
	 * @return true if operation succeeds
	 */
	public Boolean setServerStaredEvents(String staredEventsList);
	
	/**
	 * Remove the Event to the stared events set
	 * @param eventId the id of event to remove
	 * @return true if the operation succeed
	 */
	public boolean removeFromStaredElement(Integer eventId);
	
	/**
	 * Add the Event to the stared events set
	 * @param eventId the id of event to add
	 * @return true if the operation succeed
	 */
	public boolean addToStaredElement(Integer eventId);
	
	/**
	 * Remove the Event to the stared events set
	 * @param eventsId the list of id of event to remove
	 * @return true if the operation succeed
	 */
	public boolean removeFromStaredElements(List<Integer> eventsId);
	
	/**
	 * Add the Event to the stared events set
	 * @param eventsId the list of id of event to add
	 * @return true if the operation succeed
	 */
	public boolean addToStaredElements(List<Integer> eventsId);

}

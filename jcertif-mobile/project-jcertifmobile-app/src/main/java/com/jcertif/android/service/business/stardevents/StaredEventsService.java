/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.business.stardevents</li>
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
package com.jcertif.android.service.business.stardevents;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.jcertif.android.JCApplication;
import com.jcertif.android.dao.StaredEventsDaoFactory;
import com.jcertif.android.dao.intf.StaredEventsDaoIntf;
import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.transverse.model.Event;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to be the business service for the stared events of the user
 */
public enum StaredEventsService {
	instance;

	// TODO pattern singleton
	/**
	 * The list of stared events
	 */
	private List<Integer> staredEvents;
	/**
	 * The Dao to store and retreive elements
	 */
	private StaredEventsDaoIntf seDao;
	
	/**
	 * Synchronize the list with the one of the Dao if needed
	 */
	public void synchronize() {
		if(JCApplication.getInstance().isStaredEventsUpdated()) {
			seDao = StaredEventsDaoFactory.getDao();
			staredEvents = seDao.getStaredEvents();
			JCApplication.getInstance().setStaredEventsUpdatesTookIntoAccount();
		}
		
	}

	/**
	 * Constructor
	 */
	private StaredEventsService() {
		seDao = StaredEventsDaoFactory.getDao();
		staredEvents = seDao.getStaredEvents();

	}

	/**
	 * @param eventId
	 * @return true if the element is a stared session
	 */
	public boolean isStared(int eventId) {
		synchronize();
		return staredEvents.contains(eventId);
	}

	/**
	 * @param eventId
	 *            the eventId
	 * @param toStar
	 *            the boolean to know if the element has to be stared or unstared
	 */
	public boolean staredEventsStatusChanged(int eventId, boolean toStar) {
		synchronize();
		// First case add the element to the stared elements list
		if (toStar) {
			// add it to the list
			staredEvents.add(eventId);
			// add it to the dao
			return seDao.addToStaredElement(eventId);
		}
		// second case remove the element from the stared elements list
		else {
			// remove it from the list
			int eventIndex = staredEvents.indexOf(eventId);
			staredEvents.remove(eventIndex);
			// remove it from the dao
			return seDao.removeFromStaredElement(eventId);
		}
	}

	/**
	 * Remove the Event to the stared events set
	 * 
	 * @param eventsId
	 *            the list of id of event to remove
	 * @return true if the operation succeed
	 */
	public boolean removeFromStaredElements(List<Integer> eventsId) {
		synchronize();
		staredEvents.removeAll(eventsId);
		return seDao.removeFromStaredElements(eventsId);
	}

	/**
	 * Add the Event to the stared events set
	 * 
	 * @param eventsId
	 *            the list of id of event to add
	 * @return true if the operation succeed
	 */
	public boolean addToStaredElements(List<Integer> eventsId) {
		synchronize();
		staredEvents.addAll(eventsId);
		return seDao.addToStaredElements(eventsId);
	}
	
	/**
	 * 
	 * @param speakerId
	 * @return true if all the events of the speaker is starred
	 */
	public boolean isStaredSpeaker(int speakerId) {
		synchronize();
		boolean ret=true;
		for(Integer eventId:getEventsId(speakerId)) {
			ret=ret&&isStared(eventId);
			//a few optimisation
			if(ret==false) {
				break;
			}
		}
		return ret;
	}

	/**
	 * Add all the events of a speaker as star elements
	 * @param speakerId the speaker id
	 * @return if the operation succeeds
	 */
	public boolean addStaredSpeaker(int speakerId) {
		synchronize();
		return addToStaredElements(getEventsId(speakerId));
	}

	/**
	 * Remove all the events of a speaker as star elements
	 * @param speakerId the speaker id
	 * @return if the operation succeeds
	 */
	public boolean removeStaredSpeaker(int speakerId) {
		synchronize();
		return removeFromStaredElements(getEventsId(speakerId));
	}

	/**
	 * Retrieve all the events of a speaker
	 * @param speakerId the id of the speaker
	 * @return that list
	 */
	private List<Integer> getEventsId(int speakerId) {
		try {
			EventProvider eventProvider = new EventProvider();

			List<Event> allEvent = eventProvider.getAllEvents();
			List<Integer> speakerEvents = new ArrayList<Integer>();
			for (Event event : allEvent) {
				String[] idsSpeaker = event.speakersId.split(",");
				for (String idSp : idsSpeaker) {
					if (Integer.valueOf(idSp).equals(speakerId)) {
						speakerEvents.add(event.id);
					}
				}
			}
			return speakerEvents;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  new ArrayList<Integer>();
	}
}

/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.event.list</li>
 * <li>30 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.event.list;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to make callBack from EventsListFragment to the Activity using it
 */
public interface EventsListCallBack {
	/**
	 * This method is called when a event is selected and has to be shown
	 * @param eventId
	 */
	public void showSelectedEvent(int eventID, boolean fromEvent);
}

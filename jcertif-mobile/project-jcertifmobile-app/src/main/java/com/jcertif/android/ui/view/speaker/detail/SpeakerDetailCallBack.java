/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.speaker.detail</li>
 * <li>30 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.ui.view.speaker.detail;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to make callBack from SpeakerDetailFragment to the Activity using it
 */
public interface SpeakerDetailCallBack {
	/**
	 * Show the others fragment using all the available space (not this one because this one is removed when you call this method)
	 */
	public void fillSpace(Boolean force);
	/**
	 * This method is called when a event is selected and has to be shown
	 * @param eventId
	 */
	public void showSelectedEvent(int eventID, boolean fromEvent);
}

/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.speaker.list</li>
 * <li>22 mai 2012</li>
 * 
 * <li>======================================================</li>
 */
package com.jcertif.android.ui.view.speaker.list;


/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to make callBack from SpeakersListFragment to the Activity using it
 */
public interface SpeakersListCallBack {
	
	/**
	 * This method is called when a speaker is selected and has to be shown
	 * @param speakerID
	 */
	public void showSelectedSpeaker(int speakerID);
	
}

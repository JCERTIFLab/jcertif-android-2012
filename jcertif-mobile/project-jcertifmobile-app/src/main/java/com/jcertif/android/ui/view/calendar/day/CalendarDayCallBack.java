/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.calendar.day</li>
 * <li>11 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.ui.view.calendar.day;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to:
 * <ul><li></li></ul>
 */
public interface CalendarDayCallBack {
	/**
	 * This method is called when a event is selected and has to be shown
	 * @param eventId
	 */
	public void showSelectedEvent(int eventID, boolean fromEvent);
}

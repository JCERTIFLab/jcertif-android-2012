/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.dao</li>
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
package com.jcertif.android.dao;

import com.jcertif.android.dao.intf.StaredEventsDaoIntf;
import com.jcertif.android.dao.prefs.StaredEventsDaoPrefs;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to builds the StaredEventsDao to use in the other parts of the application
 */
public class StaredEventsDaoFactory {
	/**
	 * The Dao
	 */
	private static StaredEventsDaoIntf dao;
	

	/**
	 * @return the StaredEventsDao to use
	 */
	public static StaredEventsDaoIntf getDao() {
		if(null==dao) {
			dao=new StaredEventsDaoPrefs();
		}
		return  dao;
	}
}

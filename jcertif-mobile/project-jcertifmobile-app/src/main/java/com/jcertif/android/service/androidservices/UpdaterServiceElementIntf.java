/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices</li>
 * <li>28 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.service.androidservices;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to make a common interface for all the elements that can be used by the
 *        UpdaterService
 *        The updaterService has a bunch of those interface and call a thread to run the onUpdate
 *        method. It uses the getName to set the thread name
 */
public interface UpdaterServiceElementIntf {
	/**
	 * The working method that will be run within a thread to update the data
	 */
	public void onUpdate();

	/**
	 * @return the name of the service
	 */
	public String getName();
}

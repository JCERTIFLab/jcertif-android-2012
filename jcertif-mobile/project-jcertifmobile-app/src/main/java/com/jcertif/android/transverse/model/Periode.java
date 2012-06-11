/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.transverse.model</li>
 * <li>5 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
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
package com.jcertif.android.transverse.model;

import java.util.Date;

import android.text.format.DateFormat;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to define a periode object
 */
public class Periode implements Comparable<Periode>{
	/**
	 * 
	 */
	public Date startDate;
	/**
	 * 
	 */
	public Date endDate;
	
	/**
	 * @param startDate
	 * @param endDate
	 */
	public Periode(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}
	/**
	 * @return the startDate
	 */
	public final Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public final void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public final Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public final void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/******************************************************************************************/
	/** Overriden Methods **************************************************************************/
	/******************************************************************************************/

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Periode another) {
		
		return this.startDate.compareTo(another.startDate);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder strBuild=new StringBuilder();
		strBuild.append("{");
		strBuild.append(DateFormat.format("MM/dd/yy h:mmaa", startDate));
		strBuild.append("-");
		strBuild.append(DateFormat.format("MM/dd/yy h:mmaa", endDate));
		strBuild.append("}");
		return strBuild.toString();
	}		
}

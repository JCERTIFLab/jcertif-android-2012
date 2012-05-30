/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.transverse</li>
 * <li>22 mai 2012</li>
 */
package com.jcertif.android.transverse.tools;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 * This class aims to centralize some HTTP usefull methods
 */
public class HttpTools {
	/**
	 * To know if a HttpResponse code is ok
	 * Should be between 200 and 299
	 * @param code the code to test
	 * @return true if valid, else return false 
	 */
	public static Boolean isValidHttpResponseCode(int code) {
		//if code commence par 200 ok=> 100<code-200<=0
		int codeMinusTwoUndred=code-200;
		if(codeMinusTwoUndred<100&&codeMinusTwoUndred>=0) {
			return true;
		}
		return false;
	}

}

package com.jcertif.android;

/**
 * 
 * @author Yakhya DABO
 *
 */
public class Application {
	//Application context
	public static final String NAME = "JCertif-Mobile";
	public static String EMAIL;
	public static String PASSWORD;
	
	// BASE URL
	public static final String BASE_URL = "http://jcertif.baamtu.com/jcertif-facade/api";
	public static final String BASE_PICTURE_URL = "http://jcertif.baamtu.com/jcertif-pics/Speaker";
	
	// RESOURCES URL
	public static final String SPEAKER_URL = BASE_URL + "/speaker/list";
    public static final String EVENT_URL = BASE_URL + "/event/list";
	public static final String AUTHENTICATION_URL = BASE_URL + "/user/connect";
	public static final String REGISTER_URL =  BASE_URL; 
}

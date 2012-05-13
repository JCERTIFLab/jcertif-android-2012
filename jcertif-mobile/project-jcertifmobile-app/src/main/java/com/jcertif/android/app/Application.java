package com.jcertif.android.app;

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
	public static final String TAG="";
	// BASE URL http://facade.jcertif.cloudbees.net/api
	// public static final String BASE_URL = "http://jcertif.baamtu.com/jcertif-facade/api";
	public static final String BASE_URL = "http://facade.jcertif.cloudbees.net/api";
	public static final String BASE_PICTURE_URL = "http://pics.jcertif.cloudbees.net/img/2012";
	
	// RESOURCES URL
	public static final String SPEAKER_URL = BASE_URL + "/speaker/list/2";
    public static final String EVENT_URL = BASE_URL + "/event/list/2";
	public static final String AUTHENTICATION_URL = BASE_URL + "/user/connect";
	public static final String REGISTER_URL =  BASE_URL + "/user/create"; 
	
}

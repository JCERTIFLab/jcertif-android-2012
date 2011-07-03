package com.jcertif.android;

public class Application {
	//Application context
	public static final String NAME = "JCertif-Mobile";
	public static String LOGIN;
	public static String PASSWORD;
	
	// URL
	public static final String BASE_URL = "http://jcertif.baamtu.com/jcertif-facade/api";
	public static final String BASE_PICTURE_URL = "http://jcertif.baamtu.com/jcertif-pics/Speaker";
	public static final String SPEAKER_URL = BASE_URL + "/speaker/list";
    public static final String EVENT_URL = BASE_URL + "/event/list";
}

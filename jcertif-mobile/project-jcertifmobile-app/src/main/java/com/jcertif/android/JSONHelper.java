package com.jcertif.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jcertif.android.model.Speaker;


public class JSONHelper {
	
	private static final String ID = "id";
	private static final String PRENOM = "prenom";
	private static final String NOM = "nom";
	private static final String BIO = "bio";
	private static final String COMPAGNIE = "compagnie";
	private static final String URL_PHOTO = "photo";

	/**
	 * 
	 * @param data
	 * @return List of speakers 
	 */ 
	public static List<Speaker> getSpeaker(String data){
			List<Speaker> speakers = new ArrayList<Speaker>();
			
			try {
				JSONObject jsonObject =new JSONObject(data);		
				JSONObject json = new JSONObject(jsonObject.toString());
				JSONArray jArray = json.getJSONArray(jsonObject.names().getString(0));

					for (int i = 0; i < jArray.length(); i++) {
						JSONObject jsonData = jArray.getJSONObject(i);
						Log.i(Application.NAME,"JSON Speaker : " + jsonData.toString());
						
						Speaker speaker = new Speaker();
						
						speaker.id = jsonData.getInt(ID);
						speaker.firstName = jsonData.getString(PRENOM);
						speaker.lastName = jsonData.getString(NOM);
						speaker.bio = jsonData.getString(BIO);
						speaker.company = jsonData.getString(COMPAGNIE);
						speaker.urlPhoto = jsonData.getString(URL_PHOTO);
						
						speakers.add(speaker);
						Log.i(Application.NAME,"Speaker name : " + speaker.lastName);
					}
					
			} catch (JSONException e) {
				Log.e(Application.NAME, " JSONHelper : "+e.toString());
				// throw e;
			}
			return speakers;
		} 	
	}

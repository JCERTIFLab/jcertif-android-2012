package com.jcertif.android.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jcertif.android.model.Event;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.jcertif.android.model.Speaker;

/**
 * 
 * @author Yakhya DABO
 *
 */
public class JSONHelper {

    private static final String ID = "id";
    private static final String PRENOM = "prenom";
    private static final String NOM = "nom";
    private static final String BIO = "bio";
    private static final String COMPAGNIE = "compagnie";
    private static final String URL_PHOTO = "photo";
    private static final String START_DATE = "dateDebut";
    private static final String END_DATE = "dateFin";
    private static final String ROOM = "salle";
    private static final String SUMMARY = "sommaire";
    private static final String DESCRIPTION = "description";
    private static final String SPEAKERS_ID = "speakersId";
    private static final String KEY_WORD = "motCle";
    private static final String SUBJECT = "sujets";

    /**
     * @param data
     * @return List of speakers
     */
    public static List<Speaker> getSpeaker(String data) {
        List<Speaker> speakers = new ArrayList<Speaker>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject json = new JSONObject(jsonObject.toString());
            JSONArray jArray = json.getJSONArray(jsonObject.names().getString(0));

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonData = jArray.getJSONObject(i);
                Log.i(Application.NAME, "JSON Speaker : " + jsonData.toString());

                Speaker speaker = new Speaker();

                speaker.id = jsonData.getInt(ID);
                speaker.firstName = jsonData.getString(PRENOM);
                speaker.lastName = jsonData.getString(NOM);
                speaker.bio = jsonData.getString(BIO);
                speaker.company = jsonData.getString(COMPAGNIE);
                speaker.urlPhoto = jsonData.getString(URL_PHOTO);

                speakers.add(speaker);
                Log.i(Application.NAME, "Speaker name : " + speaker.lastName);
            }

        } catch (JSONException e) {
            Log.e(Application.NAME, " JSONHelper : " + e.toString());
            // throw e;
        }
        return speakers;
    }

    public static List<Event> getEvents(String data) {
        List<Event> events = new ArrayList<Event>();

        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject json = new JSONObject(jsonObject.toString());
            JSONArray jArray = json.getJSONArray(jsonObject.names().getString(0));

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jsonData = jArray.getJSONObject(i);
                Log.i(Application.NAME, "JSON Event : " + jsonData.toString());

                Event event = new Event();

                event.id = jsonData.getInt(ID);
                event.name = jsonData.getString(NOM);
                event.description = jsonData.getString(DESCRIPTION);
                event.keyWord = jsonData.getString(KEY_WORD);
                event.room = jsonData.getString(ROOM);
                event.subjects = jsonData.getString(SUBJECT);
                event.summary = jsonData.getString(SUMMARY);
                event.speakersId = jsonData.getString(SPEAKERS_ID);
                event.startDate = getDate(jsonData.getString(START_DATE));
                event.endDate = getDate(jsonData.getString(END_DATE));

                events.add(event);
                Log.i(Application.NAME, "Event name : " + event.name);
            }

        } catch (JSONException e) {
            Log.e(Application.NAME, " JSONHelper : " + e.toString());
            // throw e;
        } catch (ParseException e) {
            Log.e(Application.NAME, " JSONHelper : " + e.toString());
        }
        return events;
    }

    /**
     * Build date with a facade string date.
     *
     * @param facadeStringDate facade string date (eg. 2011-09-03T17:30:00+02:00)
     * @return
     */
    public static Date getDate(String facadeStringDate) throws ParseException {
        String[] splitedDateTime = facadeStringDate.split("T");
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(splitedDateTime[0] + " " + splitedDateTime[1].substring(0, 8));
    }
}
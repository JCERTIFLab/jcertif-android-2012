package com.jcertif.android.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.data.DatabaseHelper;
import com.jcertif.android.data.EventProvider;
import com.jcertif.android.model.Event;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.JCertifLocalService;

/**
 * Display details of a selected speaker
 *
 * @author mouhamed_diouf
 */
public class SpeakerActivity extends Activity {

    public static final int BIO_PART1_MAX_LENGTH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_detail);
        int speakerId = getIntent().getIntExtra("speakerId", -1);
        ImageView i11 = (ImageView) findViewById(R.id.speakerImg);

        TextView speakerBioPart1 = (TextView) findViewById(R.id.speakerBioPart1);
        TextView speakerBioPart2 = (TextView) findViewById(R.id.speakerBioPart2);
        TextView speakerEvent = (TextView) findViewById(R.id.speakerEvent);

        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        Dao<Speaker, Integer> speakerDao;
        Dao<Event, Integer> eventDao;
        try {

            // Searching speaker's event
            eventDao = dbHelper.getEventDao();
            EventProvider eventProvider = new EventProvider(new JCertifLocalService());

            List<Event> allEvent = eventProvider.getAllEvents();
            List<Event> speakerEvents = new ArrayList<Event>();
            for (Event event : allEvent) {
                 String[] idsSpeaker = event.speakersId.split(",");
                for(String idSp : idsSpeaker){
                     if(Integer.valueOf(idSp).equals(speakerId)){
                          speakerEvents.add(event);
                     }
                }
            }
             Log.e("SpeakerDisplayActivity","count event = " + speakerEvents.size());
            speakerDao = dbHelper.getSpeakerDao();
            Speaker speaker = speakerDao.queryForId(speakerId);

            // Define header title
            TextView headerTitle = (TextView) findViewById(R.id.header_title);
            headerTitle.setText(speaker.firstName + " " + speaker.lastName + " (" + speaker.company + ")");

            TextView speakerName = (TextView) findViewById(R.id.speakerName);
            speakerName.setText(speaker.firstName + " " + speaker.lastName);

            Bitmap speakerBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                    + speaker.urlPhoto);
            i11.setImageBitmap(speakerBitmap);

            String[] splittedBio = splitBio(speaker.bio);
            speakerBioPart1.setText(splittedBio[0]);
            speakerBioPart2.setText(splittedBio[1]);

            StringBuilder evs = new StringBuilder();
            for(Event ev : speakerEvents)  {
                evs.append(ev.name +",");
            }
            speakerEvent.setText(evs.toString());


        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Log.e("SpeakerDisplayActivity", e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("SpeakerDisplayActivity", e.getMessage());
        }

    }

    /**
     * Spliting bio.
     *
     * @param bio a bio
     * @return String array (2 elements) with two parts
     */
    private String[] splitBio(String bio) {
        StringBuilder part1 = new StringBuilder();
        StringBuilder part2 = new StringBuilder();
        String[] spaceSplitBio = bio.split(" ");

        for (String word : spaceSplitBio) {
            if (part1.length() <= BIO_PART1_MAX_LENGTH) {
                part1.append(word + " ");
            } else {
                part2.append(word + " ");
            }
        }

        return new String[]{part1.toString(), part2.toString()};
    }

}

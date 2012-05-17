package com.jcertif.android.ui.view;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.transverse.model.Speaker;

/**
 * Display details of a selected speaker
 *
 * @author mouhamed_diouf
 */
public class SpeakerActivity extends Activity {

    public static final int BIO_PART1_MAX_LENGTH = 100;
    private SpeakerProvider speakersProvider;
    private EventProvider eventProvider;
    		
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_detail);
        int speakerId = getIntent().getIntExtra("speakerId", -1);
        ImageView i11 = (ImageView) findViewById(R.id.speakerImg);

        TextView speakerBioPart1 = (TextView) findViewById(R.id.speakerBioPart1);
        TextView speakerBioPart2 = (TextView) findViewById(R.id.speakerBioPart2);
        
//        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
//        Dao<Speaker, Integer> speakerDao;
        try {
        	speakersProvider =  new SpeakerProvider(getBaseContext());
        	
            // Searching speaker's event
            List<Event> speakerEvents = findSpeakerEvents(speakerId);
            //speakerDao = dbHelper.getSpeakerDao();
            Speaker speaker = speakersProvider.findById(speakerId);//speakerDao.queryForId(speakerId);

            // Define header title
            TextView headerTitle = (TextView) findViewById(R.id.header_title);
            headerTitle.setText(speaker.firstName + " " + speaker.lastName + " (" + speaker.company + ")");

            TextView speakerName = (TextView) findViewById(R.id.speakerName);
            speakerName.setText(speaker.firstName + " " + speaker.lastName);

            Bitmap speakerBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                    + speaker.urlPhoto);
            i11.setImageBitmap(speakerBitmap);

            String[] splitedBio = splitBio(speaker.bio);
            speakerBioPart1.setText(splitedBio[0]);
            speakerBioPart2.setText(splitedBio[1]);

            buildSpeakerEventLayout(speakerEvents);

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Log.e("SpeakerDisplayActivity", e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("SpeakerDisplayActivity", e.getMessage());
        }

    }

    private List<Event> findSpeakerEvents(int speakerId) throws Exception {
        eventProvider = new EventProvider(getBaseContext());

        List<Event> allEvent = eventProvider.getAllEvents();
        List<Event> speakerEvents = new ArrayList<Event>();
        for (Event event : allEvent) {
            String[] idsSpeaker = event.speakersId.split(",");
            for (String idSp : idsSpeaker) {
                if (Integer.valueOf(idSp).equals(speakerId)) {
                    speakerEvents.add(event);
                }
            }
        }
        return speakerEvents;
    }

    private void buildSpeakerEventLayout(List<Event> speakerEvents) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.speakerEvents);

        for (final Event ev : speakerEvents) {
            LinearLayout newLayout = new LinearLayout(this);
            LayoutInflater inflater = ((LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE));
            View detailEventView = inflater.inflate(R.layout.speaker_event_detail, newLayout);
            TextView nameView = (TextView) detailEventView.findViewById(R.id.eventName);
            nameView.setText(ev.name);
            final Intent intentForDisplay = new Intent(getApplicationContext(), EventActivity.class);
            newLayout.setOnClickListener(new View.OnClickListener(){


                public void onClick(View view) {
                      intentForDisplay.putExtra("sessionId", ev.id);
    				  startActivity(intentForDisplay);
                }
            });
            TextView roomView = (TextView) detailEventView.findViewById(R.id.eventRoom);
            roomView.setText(ev.room);
            TextView dateView = (TextView) detailEventView.findViewById(R.id.eventDate);
            dateView.setText(new SimpleDateFormat("EEE").format(ev.startDate) + " " + new SimpleDateFormat("HH:mm").format(ev.startDate) + " à " + new SimpleDateFormat("HH:mm").format(ev.endDate));
            layout.addView(newLayout);
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

package com.jcertif.android.view;

import java.sql.SQLException;

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
import com.jcertif.android.model.Speaker;

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

        DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        Dao<Speaker, Integer> speakerDao;
        try {
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

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            Log.e("SpeakerDisplayActivity", e.getMessage());
        }

    }

    /**
     * Spliting bio.
     * @param bio  a bio
     * @return String array (2 elements) with two parts
     */
    private String[] splitBio(String bio) {
        StringBuilder part1 = new StringBuilder();
        StringBuilder part2 = new StringBuilder();
        String[] spaceSplitBio = bio.split(" ");

        for (String word : spaceSplitBio) {
                 if(part1.length() <= BIO_PART1_MAX_LENGTH){
                     part1.append(word + " ");
                 } else {
                     part2.append(word + " ");
                 }
        }

        return new String[]{part1.toString(), part2.toString()};
    }

}

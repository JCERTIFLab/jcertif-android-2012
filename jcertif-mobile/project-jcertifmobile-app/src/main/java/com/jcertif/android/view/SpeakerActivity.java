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
 * @author mouhamed_diouf
 *
 */
public class SpeakerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speaker_detail);
		int speakerId = getIntent().getIntExtra("speakerId", -1);
		ImageView i11 = (ImageView) findViewById(R.id.speakerImg);
		TextView speakerFirstName = (TextView) findViewById(R.id.speakerFirstName);
		TextView speakerLastName = (TextView) findViewById(R.id.speakerLastName);
		TextView speakerCompagny = (TextView) findViewById(R.id.speakerCompany);
		TextView speakerBio = (TextView) findViewById(R.id.speakerBio);

		DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
		Dao<Speaker, Integer> speakerDao;
		try {
			speakerDao = dbHelper.getSpeakerDao();
			Speaker speaker = speakerDao.queryForId(speakerId);

			
			Bitmap speakerBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
					+ speaker.urlPhoto);
			i11.setImageBitmap(speakerBitmap);
			speakerFirstName.setText(speaker.firstName);
			speakerLastName.setText(speaker.lastName); 
			speakerCompagny.setText(speaker.company);
			speakerBio.setText(speaker.bio);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.e("SpeakerDisplayActivity", e.getMessage());
		}

	}

}

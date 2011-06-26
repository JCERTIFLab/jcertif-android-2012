package com.jcertif.android.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.Application;
import com.jcertif.android.JSONHelper;
import com.jcertif.android.model.Speaker;
import com.jcertif.android.service.JCertifLocalService;

/**
 * Provider for Speaker Do persistence and parsing stuff
 * 
 * @author mouhamed_diouf
 * 
 */
public class SpeakerProvider {
	private JCertifLocalService service;
	private Context context;

	public SpeakerProvider(JCertifLocalService service) {
		this.service = service;
	}

	public List<Speaker> getAllSpeakers() throws Exception {
		String data = service.getSpeakersData();
		List<Speaker> speakers = JSONHelper.getSpeaker(data);
		// let's save now in db
		DatabaseHelper dbHelper = new DatabaseHelper(service.getBaseContext());
		Dao<Speaker, Integer> speakerDao = dbHelper.getSpeakerDao();
		;
		for (Speaker speaker : speakers) {
			try {
				speakerDao.createOrUpdate(speaker);
				saveSpeakerPicture(Application.BASE_PICTURE_URL,
						speaker.urlPhoto);

			} catch (SQLException e) {
				// TODO Handle exception
				Log.e("SpeakerDisplayActivity", e.getMessage());
			}
		}
		return speakers;
	}

	Bitmap bmImg;

	void saveSpeakerPicture(String fileUrl, String fileName) {
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(fileUrl + "/" + fileName);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
			// this.imView.setImageBitmap(bmImg);
			String filepath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(filepath + "/"
					+ fileName);
			bmImg.compress(CompressFormat.JPEG, 75, fos);
			Log.i(SpeakerProvider.class.getName(), "Save of picture ok:" + fos.getFD().toString());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.e("MyLog", e.toString());
		}

	}
}

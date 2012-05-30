package com.jcertif.android.dao.ormlight;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.JCApplication;
import com.jcertif.android.transverse.model.Speaker;

/**
 * Provider for Speaker Do persistence and parsing stuff
 * 
 * @author mouhamed_diouf
 */
public class SpeakerProvider {
	// private JCertifLocalService service;
	// private Context context;

	private Context mContext;
	private DatabaseHelper mDbHelper;
	private Dao<Speaker, Integer> mSpeakerDao;

	public SpeakerProvider(Context context) throws SQLException {
		mContext = context;
		mDbHelper = new DatabaseHelper(mContext);
		mSpeakerDao = mDbHelper.getSpeakerDao();
	}

	public Speaker findById(int speakerId) throws SQLException {
		return mSpeakerDao.queryForId(speakerId);
	}

	public List<Speaker> findAll() throws SQLException {
		return mSpeakerDao.queryForAll();
	}

	public Speaker getEventById(Integer speakerId) throws SQLException {
		return mSpeakerDao.queryForId(speakerId);
	}

	public void saveAll(List<Speaker> speakers) throws SQLException {
		for (Speaker speaker : speakers) {
			mSpeakerDao.createOrUpdate(speaker);
			String urlPictureSpeaker = JCApplication.getInstance().getUrlFactory().getBasePictureUrl();
			saveSpeakerPicture(urlPictureSpeaker, speaker.urlPhoto);
		}
	}

	Bitmap bmImg;

	void saveSpeakerPicture(String fileUrl, String fileName) {
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(fileUrl + "/" + fileName);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
			// this.imView.setImageBitmap(bmImg);
			String filepath = Environment.getExternalStorageDirectory().getAbsolutePath();
			FileOutputStream fos = new FileOutputStream(filepath + "/" + fileName);
			bmImg.compress(CompressFormat.JPEG, 75, fos);
			Log.i(SpeakerProvider.class.getName(), "Save of picture ok:" + fos.getFD().toString());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.d("MyLog", e.toString());
		}

	}
}

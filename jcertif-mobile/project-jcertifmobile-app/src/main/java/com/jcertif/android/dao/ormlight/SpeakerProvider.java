package com.jcertif.android.dao.ormlight;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
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

	public SpeakerProvider() throws SQLException {
		mContext = JCApplication.getInstance().getApplicationContext();
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

	public void deleteAllAndsaveAll(List<Speaker> speakers) throws SQLException {
		// To avoid bad cache of data removeAll and createAll is needed
		// Remove data first:
		mSpeakerDao.delete(mSpeakerDao.queryForAll());
		// then create them again
		if (null != speakers) {
			for (Speaker speaker : speakers) {
				mSpeakerDao.createOrUpdate(speaker);
				String urlPictureSpeaker = JCApplication.getInstance().getUrlFactory().getBasePictureUrl();
				saveSpeakerPicture(urlPictureSpeaker, speaker.urlPhoto);
			}
		}
	}

	public void saveAll(List<Speaker> speakers) throws SQLException {
		if (null != speakers) {
			for (Speaker speaker : speakers) {
				mSpeakerDao.createOrUpdate(speaker);
				String urlPictureSpeaker = JCApplication.getInstance().getUrlFactory().getBasePictureUrl();
				saveSpeakerPicture(urlPictureSpeaker, speaker.urlPhoto);
			}
		}
	}

	Bitmap bmImg;

	/**
	 * Save the speaker picture
	 * 
	 * @param fileUrl
	 * @param fileName
	 */
	void saveSpeakerPicture(String fileUrl, String fileName) {
		Log.i("SpeakerProvider:saveSpeakerPicture", "Called");
		URL myFileUrl = null;
		try {
			myFileUrl = new URL(fileUrl + "/" + fileName);
			Log.i("SpeakerProvider:saveSpeakerPicture", "trying to get " + fileUrl + "/" + fileName);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bmImg = BitmapFactory.decodeStream(is);
			// save the picture
			File filesDir = JCApplication.getInstance().getExternalFilesDir(null);
			String pictureFolderName = JCApplication.getInstance().getString(R.string.folder_name_spekaer_picture);
			File pictureDir = new File(filesDir, pictureFolderName);
			if (!pictureDir.exists()) {
				pictureDir.mkdir();
			}
			File filePicture = new File(pictureDir, fileName);
			FileOutputStream fos = new FileOutputStream(filePicture);
			bmImg.compress(CompressFormat.JPEG, 75, fos);
			Log.i(SpeakerProvider.class.getName(), "Save of picture ok:" + fos.getFD().toString());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			Log.d("MyLog", e.toString());
		}

	}
}

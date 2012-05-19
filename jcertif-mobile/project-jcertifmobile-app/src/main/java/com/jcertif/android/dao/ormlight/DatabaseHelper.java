package com.jcertif.android.dao.ormlight;

import java.sql.SQLException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jcertif.android.JCApplication;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.transverse.model.Speaker;

/**
 * Database helper which creates and upgrades the database and provides the DAOs
 * for the app.
 * 
 * @author mdiouf
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String DATABASE_NAME = "jcertif.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Speaker, Integer> speakerDao;
	private Dao<Event, Integer> eventDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource) {
		Log.i(JCApplication.NAME, "Trying to create tables ... ");

		try {
			// eventDao.isTableExists() is not supported on Android
			// speakerDao.isTableExists() is not supported on Android

			if (!isTableExists(sqliteDatabase, "speakers")) {
				TableUtils.createTable(connectionSource, Speaker.class);
				Log.i(DatabaseHelper.class.getName(), "Creating table Speaker");
			}
			if (!isTableExists(sqliteDatabase, "events")) {
				TableUtils.createTable(connectionSource, Event.class);
				Log.i(DatabaseHelper.class.getName(), "Creating table Event");
			}
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases",
					e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase,
			ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Speaker.class, true);
			TableUtils.dropTable(connectionSource, Event.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(),
					"Unable to upgrade database from version " + oldVer
							+ " to new " + newVer, e);
		}
	}

	public Dao<Speaker, Integer> getSpeakerDao() throws SQLException {
		if (speakerDao == null) {
			speakerDao = getDao(Speaker.class);
		}
		return speakerDao;
	}

	public Dao<Event, Integer> getEventDao() throws SQLException {
		if (eventDao == null) {
			eventDao = getDao(Event.class);
		}
		return eventDao;
	}

	/**
	 * Return true if the table exists. 
	 * @param db the DB name
	 * @param tableName the table's name
	 * @return true if the table exists
	 */
	private boolean isTableExists(SQLiteDatabase db, String tableName) {
		if (tableName == null || db == null || !db.isOpen()) {
			return false;
		}
		Cursor cursor = db
				.rawQuery(
						"SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
						new String[] { "table", tableName });
		if (!cursor.moveToFirst()) {
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}

}

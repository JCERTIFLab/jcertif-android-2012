package com.jcertif.android.data;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jcertif.android.model.Speaker;

/**
 * Database helper which creates and upgrades the database and provides the DAOs for the app.
 * 
 * @author mdiouf
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


	private static final String DATABASE_NAME = "jcertif.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Speaker, Integer> speakerDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Speaker.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Speaker.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
					+ newVer, e);
		}
	}

	public Dao<Speaker, Integer> getSpeakerDao() throws SQLException {
		if (speakerDao == null) {
			speakerDao = getDao(Speaker.class);
		}
		return speakerDao;
	}
}

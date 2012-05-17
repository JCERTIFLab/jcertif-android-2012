package com.jcertif.android.dao.ormlight;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.transverse.model.Event;

/**
 * 
 * @author Yakhya DABO
 *
 */
public class EventProvider{
	
	private Context mContext;
	private DatabaseHelper mDbHelper;
	private Dao<Event, Integer> mEventDao;
	
	
	public EventProvider(Context context) throws SQLException{
		mContext = context;
        mDbHelper = new DatabaseHelper(mContext); 
        mEventDao = mDbHelper.getEventDao();	
	}
	
	public List<Event> getAllEvents() throws SQLException {
		return mEventDao.queryForAll();
	}
	
	public Event getEventById(Integer eventId) throws SQLException {
		return mEventDao.queryForId(eventId);
	}
	
	public void saveEvents(List<Event> events) throws SQLException{
		for (Event event : events) {
			mEventDao.createOrUpdate(event);
		}
	}
}

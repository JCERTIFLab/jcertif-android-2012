package com.jcertif.android.dao.ormlight;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.jcertif.android.JCApplication;
import com.jcertif.android.service.business.stardevents.StaredEventsService;
import com.jcertif.android.transverse.model.Event;

/**
 * @author Yakhya DABO
 */
public class EventProvider {

	private Context mContext;
	private DatabaseHelper mDbHelper;
	private Dao<Event, Integer> mEventDao;

	public EventProvider() throws SQLException {
		mContext = JCApplication.getInstance().getApplicationContext();
		mDbHelper = new DatabaseHelper(mContext);
		mEventDao = mDbHelper.getEventDao();
	}

	public List<Event> getAllEvents() throws SQLException {
		return mEventDao.queryForAll();
	}

	public Event getEventById(Integer eventId) throws SQLException {
		return mEventDao.queryForId(eventId);
	}
	
	public void deleteAllAndSaveAllEvents(List<Event> events) throws SQLException {
		//delete theml all
		mEventDao.delete(mEventDao.queryForAll());
		//create them all
		if (null != events) {
			for (Event event : events) {
				mEventDao.createOrUpdate(event);
			}
		}
	}
	
	public void saveEvents(List<Event> events) throws SQLException {
		if (null != events) {
			for (Event event : events) {
				mEventDao.createOrUpdate(event);
			}
		}
	}

	public List<Event> getEventsOfTheDay(Calendar day) throws SQLException {
		// TODO make a query
		List<Event> events = getAllEvents();
		List<Event> eventsRet = new ArrayList<Event>();
		Calendar cal = Calendar.getInstance();
		for (Event event : events) {
			cal.setTime(event.getStartDate());
			if (cal.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH)
					&& cal.get(Calendar.MONTH) == day.get(Calendar.MONTH)
					&& cal.get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
				eventsRet.add(event);

			}
		}
		Log.e("EventProvider:getEventOfTheDay", "returned elements :" + eventsRet.size());
		return eventsRet;
	}

	public List<Event> getStaredEventsOfTheDay(Calendar day) throws SQLException {
		// TODO make a query
		List<Event> events = getAllEvents();
		List<Event> eventsRet = new ArrayList<Event>();
		StaredEventsService service = StaredEventsService.instance;
		Calendar cal = Calendar.getInstance();
		for (Event event : events) {
			cal.setTime(event.getStartDate());
			if (cal.get(Calendar.DAY_OF_MONTH) == day.get(Calendar.DAY_OF_MONTH)
					&& cal.get(Calendar.MONTH) == day.get(Calendar.MONTH)
					&& cal.get(Calendar.YEAR) == day.get(Calendar.YEAR)) {
				if (service.isStared(event.id)) {
					eventsRet.add(event);
				}

			}
		}
		Log.e("EventProvider:getEventOfTheDay", "returned elements :" + eventsRet.size());
		return eventsRet;
	}
}

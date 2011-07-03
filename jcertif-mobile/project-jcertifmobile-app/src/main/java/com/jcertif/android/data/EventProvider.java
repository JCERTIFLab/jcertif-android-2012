package com.jcertif.android.data;

import android.util.Log;
import com.j256.ormlite.dao.Dao;
import com.jcertif.android.JSONHelper;
import com.jcertif.android.model.Event;
import com.jcertif.android.service.JCertifLocalService;

import java.sql.SQLException;
import java.util.List;

/**
 * Provider for Event.
 *
 * @author: rossi.oddet
 */
public class EventProvider {
    private JCertifLocalService service;

    public EventProvider(JCertifLocalService service) {
		this.service = service;
	}

    public List<Event> getAllEvents() throws Exception {
        final String data = service.getEventsData();
        final List<Event> events = JSONHelper.getEvents(data);
        DatabaseHelper dbHelper = new DatabaseHelper(service.getBaseContext());
        Dao<Event, Integer> eventDao = dbHelper.getEventDao();
		for (Event event : events) {
			//try {
				//eventDao.createOrUpdate(event);
			//} catch (SQLException e) {
				// TODO Handle exception
			//Log.e("EventsListActivity", e.getMessage());
			//}
		}
        return events;
    }
}

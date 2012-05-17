package com.jcertif.android.com.parsing.jackson.service;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.jcertif.android.com.parsing.jackson.model.Events;
import com.jcertif.android.transverse.model.Event;

/**
 * 
 * @author Yakhya DABO
 *
 */
public class EventsController {
	private String mJson = null;

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private ArrayList<Event> events = null;
	private Events mEvents = null;

	public EventsController(String json) {
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
		mJson = json;
	}

	public void init() {
		try {
			jp = jsonFactory.createJsonParser(mJson);
			mEvents = objectMapper.readValue(jp, Events.class);
			events = mEvents.get("event");
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Event> findAll() {
		return events;
	}

	public Event findById(int id) {
		return events.get(id);
	}
}

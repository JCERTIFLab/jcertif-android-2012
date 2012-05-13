package com.jcertif.android.parsing.jackson.service;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import com.jcertif.android.model.Speaker;
import com.jcertif.android.parsing.jackson.model.Speakers;

public class SpeakersController {
	private String mJson = null;

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private ArrayList<Speaker> speakers = null;
	private Speakers mSpeakers = null;

	public SpeakersController(String json) {
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
		mJson = json;
	}

	public void init() {
		try {
			jp = jsonFactory.createJsonParser(mJson);
			mSpeakers = objectMapper.readValue(jp, Speakers.class);
			speakers = mSpeakers.get("speaker");
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Speaker> findAll() {
		return speakers;
	}

	public Speaker findById(int id) {
		return speakers.get(id);
	}
}

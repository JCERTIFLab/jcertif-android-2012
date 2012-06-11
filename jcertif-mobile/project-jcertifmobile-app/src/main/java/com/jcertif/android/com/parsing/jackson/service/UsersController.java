/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.com.parsing.jackson.service</li>
 * <li>1 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.com.parsing.jackson.service;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.ObjectMapper;

import com.jcertif.android.transverse.model.User;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to parse a user JSON file to an user Object
 */
public class UsersController {
	private String mJson = null;

	private ObjectMapper objectMapper = null;
	private JsonFactory jsonFactory = null;
	private JsonParser jp = null;
	private User user = null;

	public UsersController(String json) {
		objectMapper = new ObjectMapper();
		jsonFactory = new JsonFactory();
		mJson = json;
	}

	public Boolean init() {
		Boolean ret=true;
		try {
			jp = jsonFactory.createJsonParser(mJson);
			objectMapper.configure(Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			user = objectMapper.readValue(jp, User.class);
		} catch (JsonParseException e) {
			ret=false;
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			ret=false;
		}finally {
			return ret;
		}
	}

	public User get() {
		return user;
	}
}

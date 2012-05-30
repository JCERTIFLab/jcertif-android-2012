/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices.speakers</li>
 * <li>28 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.service.androidservices.speakers;

import java.util.List;

import android.util.Log;

import com.jcertif.android.JCApplication;
import com.jcertif.android.com.net.RestClient;
import com.jcertif.android.com.net.RestClient.RequestMethod;
import com.jcertif.android.com.parsing.jackson.service.SpeakersController;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.service.androidservices.UpdaterServiceElementIntf;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to update the speakers :
 *        use the rest services to retrieve the speakers
 *        parse them 
 *        add them to the database
 */
public class SpeakersUpdater implements UpdaterServiceElementIntf {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#onUpdate()
	 */
	@Override
	public void onUpdate() {
		Log.w("onUpdate Speaker", "onUpdate Speaker called");
		// Demander la liste des speakers (chaque speaker est complet)
		try {
			String speakersJson = getSpeakerList();
			// reconstruire les données
			SpeakersController spc = new SpeakersController(speakersJson);
			Log.i("onUpdate", speakersJson);
			spc.init();
			List<Speaker> speakers = spc.findAll();
			// Enregistrer chacun en BD
			SpeakerProvider spp = new SpeakerProvider(JCApplication.getInstance().getApplicationContext());
			spp.saveAll(speakers);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.service.androidservices.UpdaterServiceElementIntf#getName()
	 */
	@Override
	public String getName() {
		return JCApplication.getInstance().getApplicationContext().getString(R.string.speakerUpdater);
	}

	/**
	 * @return the Http response of the web services
	 * @throws Exception
	 */
	private String getSpeakerList() throws Exception {
		String responseString = null;
		String url = JCApplication.getInstance().getUrlFactory().getSpeakerUrl();

		RestClient client = new RestClient(url);
		try {
			client.Execute(RequestMethod.GET);
			responseString = client.getResponse();
			Log.i(this.getClass().getSimpleName(), responseString);
		} catch (Exception e) {
			Log.d(this.getClass().getSimpleName(), "LocalServiceBinder : json " + responseString);
			throw e;
		}
		return responseString;
	}

}

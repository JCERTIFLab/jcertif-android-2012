/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.event.detail</li>
 * <li>30 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.event.detail;

import java.sql.SQLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to:
 *        <ul>
 *        <li></li>
 *        </ul>
 */
public class EventDetailFragment extends Fragment {
	/**
	 * 
	 */
	public static final int BIO_PART1_MAX_LENGTH = 100;
	/**
	 * 
	 */
	public static final String EVENT_ID = "eventId";
	/**
	 * 
	 */
	private SpeakerProvider speakersProvider;
	/**
	 * 
	 */
	private EventProvider eventProvider;
	/**
	 * 
	 */
	private EventDetailCallBack callBack;
	/**
	 * 
	 */
	private int eventId = -1;

	/**
	 * 
	 */
	public EventDetailFragment() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		if (args != null) {
			eventId = args.getInt(EVENT_ID);
		}
		super.setArguments(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.w("EventDetailFragment onResume", "getView :" + getView());
		View view = inflater.inflate(R.layout.event_detail, container, false);

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.w("EventDetailFragment onResume", "getView :" + getView());
		View view = getView();
		Log.w("EventDetailFragment onCreateView", "speakerId :" + eventId);
		if (eventId != -1) {
			updateScreen(view);
		}
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Log.d("LifeCycle EventDetailFragment", "onDestroy");
		getCallBack().fillSpace(false);
		// Then the backStack is called
		super.onDestroy();
	}

	/**
	 * To be sure that the callBack is instantiate
	 */
	public EventDetailCallBack getCallBack() {
		if (callBack == null) {
			callBack = (EventDetailCallBack) ((MainActivityLegacy) getActivity()).getFragmentSwitcher();
		}
		return callBack;
	}

	/**
	 * @param view
	 * @param speakerId
	 * @return
	 */
	public void updateEvent(int eventID) {
		Log.d("EventDetailFragment updateSpeaker", "updateSpeaker :" + eventID);
		this.eventId = eventID;
		if (eventId != -1) {
			View view = getView();
			// if view is null the onresume is not called yet
			if (view != null) {
				updateScreen(view);
			}
		}
	}

	/**
	 * @param view
	 */
	private void updateScreen(View view) {
		try {

			Event event = findEvent();
			Speaker speaker = findSpeaker(event);
			
			TextView headerTitle = (TextView) getActivity().findViewById(R.id.header_title);
			headerTitle.setText(event.name);
			TextView eventTitle = (TextView) view.findViewById(R.id.eventTitle);
			TextView eventDetail = (TextView) view.findViewById(R.id.eventDetail);
			TextView eventDebut = (TextView) view.findViewById(R.id.eventDebut);
			TextView eventFin = (TextView) view.findViewById(R.id.eventFin);
			TextView eventRoom = (TextView) view.findViewById(R.id.eventRoom);
			TextView eventSubject = (TextView) view.findViewById(R.id.eventSubject);
			TextView eventSummary = (TextView) view.findViewById(R.id.eventSummary);
			TextView spName = (TextView) view.findViewById(R.id.speakerName);
			ImageView i11 = (ImageView) view.findViewById(R.id.speakerImg);
			TextView spBio1 = (TextView) view.findViewById(R.id.speakerBioPart1);
			TextView spBio2 = (TextView) view.findViewById(R.id.speakerBioPart2);
			eventTitle.setText(event.name);
			eventDetail.setText(event.description);
			

			eventDebut.setText(DateFormat.format("dd/MMM hh:mm",event.startDate));
			eventFin.setText(DateFormat.format("dd/MMM hh:mm",event.endDate));
			eventRoom.setText(event.room);
			eventSubject.setText(event.subjects);
			eventSummary.setText(event.summary);
			
			
			spName.setText(speaker.firstName + " " + speaker.lastName);
			String[] splitedBio = splitBio(speaker.bio);			
			spBio1.setText(splitedBio[0]);			
			spBio2.setText(splitedBio[1]);
			Bitmap speakerBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/" + speaker.urlPhoto);
			i11.setImageBitmap(speakerBitmap);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.d("SpeakerDisplayActivity", e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve the event
	 * 
	 * @return the event
	 * @throws SQLException
	 */
	private Event findEvent() throws SQLException {
		if (eventProvider == null) {
			eventProvider = new EventProvider(getActivity());
		}
		Event ev = eventProvider.getEventById(eventId);
		return ev;
	}

	/**
	 * Retrieve the speaker of the event
	 * 
	 * @return the speaker
	 * @throws SQLException
	 */
	private Speaker findSpeaker(Event event) throws SQLException {
		if (speakersProvider == null) {
			speakersProvider = new SpeakerProvider(getActivity());
		}
		Speaker sp = speakersProvider.findById(Integer.parseInt(event.speakersId));
		return sp;
	}

	/**
	 * Spliting bio.
	 * 
	 * @param bio
	 *            a bio
	 * @return String array (2 elements) with two parts
	 */
	private String[] splitBio(String bio) {
		StringBuilder part1 = new StringBuilder();
		StringBuilder part2 = new StringBuilder();
		String[] spaceSplitBio = bio.split(" ");

		for (String word : spaceSplitBio) {
			if (part1.length() <= BIO_PART1_MAX_LENGTH) {
				part1.append(word + " ");
			} else {
				part2.append(word + " ");
			}
		}

		return new String[] { part1.toString(), part2.toString() };
	}
}

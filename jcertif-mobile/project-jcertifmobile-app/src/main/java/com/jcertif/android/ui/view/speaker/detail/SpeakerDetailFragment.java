/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.speaker.detail</li>
 * <li>28 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 */
package com.jcertif.android.ui.view.speaker.detail;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.service.business.stardevents.StaredEventsService;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.view.main.MainActivityLegacy;

/**
 * @author Mathias Seguy (Android2EE)
 * @author mouhamed_diouf
 * @goals
 *        This class aims to displays a speaker within a fragment
 */
public class SpeakerDetailFragment extends Fragment {
	/**
	 * The max lenght for the Speaker bio part 1
	 */
	public static final int BIO_PART1_MAX_LENGTH = 100;
	/**
	 * The constant to use to pass the speaker id attribute to the fragment
	 */
	public static final String SPEAKER_ID = "speakerId";
	/**
	 * The speakers provider
	 */
	private SpeakerProvider speakersProvider;
	/**
	 * The events providers
	 */
	private EventProvider eventProvider;
	/**
	 * The callBack
	 */
	private SpeakerDetailCallBack callBack;
	/**
	 * The displayed speaker id 
	 */
	private int speakerId = -1;
	/**
	 * The background for even events rows
	 */
	GradientDrawable evenBackground = null;
	/**
	 * The background for odd events rows
	 */
	GradientDrawable oddBackground = null;
	/**
	 * The star
	 */
	Drawable star=null;
	/**
	 * The empty star
	 */
	Drawable emptyStar=null;
	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/


	/**
	 * Default constructor
	 */
	public SpeakerDetailFragment() {
		super();
	}

	/******************************************************************************************/
	/** Managing Life Cycle **************************************************************************/
	/******************************************************************************************/


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.w("SpeakerDetailFragment onCreateView", "getView :" + getView());
		evenBackground = (GradientDrawable) getResources().getDrawable(R.drawable.list_item);
		oddBackground = (GradientDrawable) getResources().getDrawable(R.drawable.list_item_odd);
		star=(Drawable) getResources().getDrawable(R.drawable.star);
		emptyStar=(Drawable) getResources().getDrawable(R.drawable.empty_star);
		View view = inflater.inflate(R.layout.speaker_detail, container, false);
		// insure the fragment not being destroyed when activity destroyed because else memory leaks
				// is generated and null pointerExceptions too (when rotating the device)
				setRetainInstance(true);
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		Log.w("SpeakerDetailFragment onResume", "getView :" + getView());
		View view = getView();
		Log.w("SpeakerDetailFragment onResume", "speakerId :" + speakerId);
		if (speakerId != -1) {
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
		Log.d("LifeCycle SpeakerDetailFragment", "onDestroy");
		if(getCallBack()!=null) {
			getCallBack().fillSpace(false);
		}
		// Then the backStack is called
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#setArguments(android.os.Bundle)
	 */
	@Override
	public void setArguments(Bundle args) {
		if (args != null) {
			speakerId = args.getInt(SPEAKER_ID);
		}
		super.setArguments(args);
	}
	
	/**
	 * To be sure that the callBack is instantiate
	 */
	public SpeakerDetailCallBack getCallBack() {
		if (callBack == null) {
			callBack = (SpeakerDetailCallBack) ((MainActivityLegacy) getActivity()).getFragmentSwitcher();
		}
		return callBack;
	}
	
	/******************************************************************************************/
	/** Updating the screen **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param view
	 * @param speakerId
	 * @return
	 */
	public void updateSpeaker(int speakerID) {
		Log.d("SpeakerDetailFragment updateSpeaker", "updateSpeaker :" + speakerId);
		this.speakerId = speakerID;
		if (speakerId != -1) {
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
		Log.w("SpeakerDetailFragment:updateScreen","begins viewupdate");
		try {

			ImageView i11 = (ImageView) view.findViewById(R.id.speakerImg);
			TextView speakerBioPart1 = (TextView) view.findViewById(R.id.speakerBioPart1);
			TextView speakerBioPart2 = (TextView) view.findViewById(R.id.speakerBioPart2);
			TextView headerTitle = (TextView) getActivity().findViewById(R.id.header_title);
			TextView speakerName = (TextView) view.findViewById(R.id.speakerName);
			LinearLayout speakerEventLayout = (LinearLayout) view.findViewById(R.id.speakerEvents);
			speakerEventLayout.removeAllViewsInLayout();
			speakersProvider = new SpeakerProvider();
			//update the star
			final ImageView starView=(ImageView) view.findViewById(R.id.star_spe_det);
			if(StaredEventsService.instance.isStaredSpeaker(speakerId)) {
				starView.setBackgroundDrawable(star);
			}else {
				starView.setBackgroundDrawable(emptyStar);
			}
			//add the listener
			starView.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					changeSpeakerStartState(starView,speakerId);
				}
			});
			
			// Searching speaker's event
			List<Event> speakerEvents = findSpeakerEvents(speakerId);
			Speaker speaker = speakersProvider.findById(speakerId);
			Log.d("SpeakerDetailFragment updateSpeaker", "speaker :" + speaker);
			// Define header title

			headerTitle.setText(speaker.firstName + " " + speaker.lastName + " (" + speaker.company + ")");
			speakerName.setText(speaker.firstName + " " + speaker.lastName);
			//load the picture
			File filesDir = JCApplication.getInstance().getExternalFilesDir(null);
			String pictureFolderName=getString(R.string.folder_name_spekaer_picture);
			File pictureDir=new File(filesDir,pictureFolderName);
			File filePicture=new File(pictureDir,speaker.urlPhoto);
			Bitmap speakerBitmap = BitmapFactory.decodeFile(filePicture.getAbsolutePath());
			i11.setImageBitmap(speakerBitmap);

			String[] splitedBio = splitBio(speaker.bio);
			speakerBioPart1.setText(splitedBio[0]);
			speakerBioPart2.setText(splitedBio[1]);
			
			buildSpeakerEventLayout(speakerEventLayout, speakerEvents);
			//and set the scroll to the top:
			ScrollView scv=(ScrollView) view.findViewById(R.id.scrollView);
			scv.fullScroll(ScrollView.FOCUS_UP);
		} catch (SQLException e) {
			Log.d("SpeakerDisplayActivity", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.w("SpeakerDetailFragment:updateScreen","ends viewupdate");
	}

	
	/**
	 * @param speakerEvents
	 */
	private void buildSpeakerEventLayout(LinearLayout speakerEventLayout, List<Event> speakerEvents) {
		int i=0;
		for (final Event ev : speakerEvents) {
			Log.w("SpeakerDetailFragment:buildSpeakerEventLayout"," a new events "+ev.id);
			LinearLayout newLayout = new LinearLayout(getActivity());
			View detailEventView = getActivity().getLayoutInflater().inflate(R.layout.speaker_event_detail, newLayout);
			TextView nameView = (TextView) detailEventView.findViewById(R.id.eventName);
			nameView.setText(ev.name);
			newLayout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					getCallBack().showSelectedEvent(ev.id, false);
				}
			});
			TextView roomView = (TextView) detailEventView.findViewById(R.id.eventRoom);
			roomView.setText(ev.room);
			TextView dateView = (TextView) detailEventView.findViewById(R.id.eventDate);
			dateView.setText(new SimpleDateFormat("EEE").format(ev.getStartDate()) + " "
					+ new SimpleDateFormat("HH:mm").format(ev.getStartDate()) + " à "
					+ new SimpleDateFormat("HH:mm").format(ev.getEndDate()));
			
			if(i%2==0) {
				newLayout.setBackgroundDrawable(evenBackground);
			}else {
				newLayout.setBackgroundDrawable(oddBackground);
			}
			i++;
			
			speakerEventLayout.addView(newLayout);
		}

	}
	
	/******************************************************************************************/
	/** Data management **************************************************************************/
	/******************************************************************************************/
	/**
	 * Change the state of the event from stared to unstared or from unstared to stared
	 * @param imageView the holder that holds the view
	 * @param eventId the event id of the event
	 */
	private void changeSpeakerStartState(ImageView imageView,int speakerId) {
		StaredEventsService service= StaredEventsService.instance;
		if(service.isStaredSpeaker(speakerId)) {
			//first change the stared status of the event
			service.removeStaredSpeaker(speakerId);
			//then update the gui
			imageView.setBackgroundDrawable(emptyStar);
		}else {
			//first change the stared status of the event
			service.addStaredSpeaker(speakerId);
			//then update the gui
			imageView.setBackgroundDrawable(star);
		}
	}
	/**
	 * @param speakerId
	 * @return
	 * @throws Exception
	 */
	private List<Event> findSpeakerEvents(int speakerId) throws Exception {
		if (eventProvider == null) {
			eventProvider = new EventProvider();
		}

		List<Event> allEvent = eventProvider.getAllEvents();
		List<Event> speakerEvents = new ArrayList<Event>();
		for (Event event : allEvent) {
			String[] idsSpeaker = event.speakersId.split(",");
			for (String idSp : idsSpeaker) {
				if (Integer.valueOf(idSp).equals(speakerId)) {
					speakerEvents.add(event);
				}
			}
		}
		return speakerEvents;
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

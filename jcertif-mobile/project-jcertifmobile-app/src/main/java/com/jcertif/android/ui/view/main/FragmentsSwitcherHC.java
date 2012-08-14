/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.main</li>
 * <li>25 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.ui.view.main;

import java.util.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentManager.BackStackEntry;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jcertif.android.R;
import com.jcertif.android.ui.view.calendar.day.CalendarDayAdapter;
import com.jcertif.android.ui.view.calendar.day.CalendarDayCallBack;
import com.jcertif.android.ui.view.calendar.day.CalendarDayFragmentHC;
import com.jcertif.android.ui.view.event.detail.EventDetailCallBack;
import com.jcertif.android.ui.view.event.detail.EventDetailFragmentHC;
import com.jcertif.android.ui.view.event.list.EventsListCallBack;
import com.jcertif.android.ui.view.event.list.EventsListFragmentHC;
import com.jcertif.android.ui.view.info.InfoFragmentHC;
import com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack;
import com.jcertif.android.ui.view.main.fragment.MainFragmentHC;
import com.jcertif.android.ui.view.speaker.detail.SpeakerDetailCallBack;
import com.jcertif.android.ui.view.speaker.detail.SpeakerDetailFragmentHC;
import com.jcertif.android.ui.view.speaker.list.SpeakersListCallBack;
import com.jcertif.android.ui.view.speaker.list.SpeakersListFragmentHC;

import de.akquinet.android.androlog.Log;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to switch fragments (from main to speakers, speakers to speaker...)
 *        depending on the orientation of the screen
 *        If you want to show a fragment in two different layouts you have to implement the fragment
 *        twice, one for each layout. This rules explains why the are SpeakerFragment1 and
 *        speakerFragment2
 */
public class FragmentsSwitcherHC implements MainFragmentCallBack, SpeakersListCallBack, SpeakerDetailCallBack,
		EventDetailCallBack, EventsListCallBack, CalendarDayCallBack {
	/******************************************************************************************/
	/** Attributes **************************************************************************/
	/******************************************************************************************/

	/**
	 * If two fragments visible = true
	 */
	Boolean twoFragmentsVisible = false;

	/**
	 * The mainActivity parent
	 */
	Activity mainActivity = null;

	/******************************************************************************************/
	/** TAGS **************************************************************************/
	/******************************************************************************************/

	/**
	 * The main fragment TAG
	 */
	private final String mainFragmentTag = "mainFragmentTag";
	/**
	 * The speakers List fragment TAG
	 */
	private final String speakersListFragmentTag = "speakersListFragmentTag";
	/**
	 * The speaker fragment for the first layout TAG
	 */
	private final String speakerFragment1Tag = "speakerFragment1Tag";
	/**
	 * The speaker fragment for the second layout TAG
	 */
	private final String speakerFragment2Tag = "speakerFragment2Tag";
	/**
	 * The events List fragment TAG
	 */
	private final String eventsListFragmentTag = "eventsListFragmentTag";
	/**
	 * The event fragment for the first layout TAG
	 */
	private final String eventFragment1Tag = "eventFragment1Tag";
	/**
	 * The event fragment for the second layout TAG
	 */
	private final String eventFragment2Tag = "eventFragment2Tag";
	/**
	 * The calendar main fragment TAG
	 */
	private final String calendarMainFragmentTag = "calendarMainFragmentTag";
	/**
	 * The agenda fragment TAG
	 */
	private final String agendaFragmentTag = "agendaFragmentTag";
	/**
	 * The info fragment TAG
	 */
	private final String infoFragmentTag = "infoFragmentTag";
	/**
	 * 
	 */
	Boolean displayLeaf = false;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param mainActivity
	 */
	public FragmentsSwitcherHC(Activity mainActivity, Boolean twoFragmentsVisible) {
		this.mainActivity = mainActivity;
		this.twoFragmentsVisible = twoFragmentsVisible;
		// TODO Delete those lines when dev is over: no need to listen backstack except in dev mode
		FragmentManager fm = mainActivity.getFragmentManager();
		
		
		fm.addOnBackStackChangedListener(new OnBackStackChangedListener() {
			@Override
			public void onBackStackChanged() {
				backstackChanged();
			}
		});
		Log.i("FragmentsSwitcherLegacy:FragmentsSwitcherLegacy", "twoFragmentsVisible :" + twoFragmentsVisible);

	}

	/******************************************************************************************/
	/** Managing second fragment visibility **************************************************************************/
	/******************************************************************************************/

	/**
	 * Manage the second fragment visibility
	 */
	public void setSecondFragmentVisible(Boolean show) {
		if (show) {
			mainActivity.findViewById(R.id.secondfragment).setVisibility(View.VISIBLE);
			LinearLayout.LayoutParams params = (LayoutParams) mainActivity.findViewById(R.id.mainfragment)
					.getLayoutParams();
			params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
			mainActivity.findViewById(R.id.mainfragment).setLayoutParams(params);
		} else {
			mainActivity.findViewById(R.id.secondfragment).setVisibility(View.GONE);
			LinearLayout.LayoutParams params = (LayoutParams) mainActivity.findViewById(R.id.mainfragment)
					.getLayoutParams();
			params.width = LinearLayout.LayoutParams.MATCH_PARENT;
			mainActivity.findViewById(R.id.mainfragment).setLayoutParams(params);
		}
	}

	/******************************************************************************************/
	/** Implementation of CallBack Methods **************************************************************************/
	/******************************************************************************************/
	/**
	 * This method is the same than showMain
	 * 
	 * @param isRecreationMode
	 */
	public void showMain(Boolean isRecreationMode) {
		displayLeaf = false;
		Fragment mainFragment = null;
		Log.i("FragmentsSwitcherLegacy:initialize", "twoFragmentsVisible :" + twoFragmentsVisible);
		// Now we can instantiate the appropriate fragment
		// if not in recreation mode we can act we are in the creation mode
		// so we instantiate the main fragment
		FragmentManager fm = mainActivity.getFragmentManager();
		FragmentTransaction fTransaction = fm.beginTransaction();
		if (!isRecreationMode) {// ||mainFragment==null
			mainFragment = new MainFragmentHC();
		} else {
			mainFragment = fm.findFragmentByTag(mainFragmentTag);
			if (null == mainFragment) {
				mainFragment = new MainFragmentHC();
			}
		}
		// then set the animation, add the fragment and commit
		fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_right_in,R.anim.anim_push_right_out);
		fTransaction.add(R.id.mainfragment, mainFragment, mainFragmentTag);
		fTransaction.commit();
		// As we are in the main menu and there is not second fragment to display
		// just hide the second frameLayout
		if (twoFragmentsVisible) {
			setSecondFragmentVisible(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showEvents()
	 */
	@Override
	public void showEvents() {
		displayLeaf = false;
		Log.i("FragmentsSwitcherLegacy:showEvents", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		EventsListFragmentHC eventsListFragment = (EventsListFragmentHC) fm.findFragmentByTag(eventsListFragmentTag);
		FragmentTransaction fTransaction = fm.beginTransaction();
		fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
		if (eventsListFragment == null) {
			eventsListFragment = new EventsListFragmentHC();
		}
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment
			fTransaction.replace(R.id.mainfragment, eventsListFragment, eventsListFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			fTransaction.replace(R.id.mainfragment, eventsListFragment, eventsListFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.event.list.EventsListCallBack#showSelectedEvent(int)
	 */
	@Override
	public void showSelectedEvent(int eventID, boolean fromEvent) {
		FragmentManager fm = mainActivity.getFragmentManager();
		displayLeaf = true;
		Log.i("showSelectedEvent", "Selected event:" + eventID);
		Log.i("FragmentsSwitcherLegacy:showSelectedEvent", "twoFragmentsVisible :" + twoFragmentsVisible);
		if (twoFragmentsVisible) {
			// Set the second fragment visible
			setSecondFragmentVisible(true);
			// Prepare the fragment transaction

			EventDetailFragmentHC eventFragment2 = (EventDetailFragmentHC) fm.findFragmentByTag(eventFragment2Tag);
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// Ensure the first fragment is the event list if the event is selected from events list
			if (fromEvent) {
				EventsListFragmentHC eventsListFragment = (EventsListFragmentHC) fm
						.findFragmentByTag(eventsListFragmentTag);
				if (eventsListFragment == null) {
					eventsListFragment = (EventsListFragmentHC) fm.findFragmentByTag(eventsListFragmentTag);
					if (eventsListFragment == null) {
						eventsListFragment = new EventsListFragmentHC();
					}
					fTransaction.replace(R.id.mainfragment, eventsListFragment, eventsListFragmentTag);
				}
			}
			// Then manage the speaker detail
			if (eventFragment2 == null) {
				// the event has to be created
				eventFragment2 = new EventDetailFragmentHC();
				// add it to the secondfragment layout
				fTransaction.replace(R.id.secondfragment, eventFragment2, eventFragment2Tag);
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(EventDetailFragmentHC.EVENT_ID, eventID);
				eventFragment2.setArguments(bundle);
				fTransaction.addToBackStack(mainActivity.getString(R.string.sessions_list_htitle));
			} else {
				// the speakerFragment already exists, update it
				eventFragment2.updateEvent(eventID);
				// if the fragment is not visible set it visible
				if (!eventFragment2.isVisible()) {
					fTransaction.replace(R.id.secondfragment, eventFragment2, eventFragment2Tag);
				}

					// Add only one level of backstack for the speaker detail
					// so if the last back stack entry is R.string.speaker_htitle do nothing
					if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()
							.equals(mainActivity.getString(R.string.speaker_htitle))) {
						fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_htitle));
					}else {
						fTransaction.addToBackStack(mainActivity.getString(R.string.session_htitle));
					}

			}
			fTransaction.commit();
		} else {
			//only one event visible
			Boolean firstCall = false;
			// So add the main fragment
			EventDetailFragmentHC eventFragment1 = (EventDetailFragmentHC) fm.findFragmentByTag(eventFragment1Tag);
			FragmentTransaction fTransaction = fm.beginTransaction();
			// if the fragment doesn't exist create it
			if (eventFragment1 == null) {
				// the event has to be created
				eventFragment1 = new EventDetailFragmentHC();
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(EventDetailFragmentHC.EVENT_ID, eventID);
				eventFragment1.setArguments(bundle);
				firstCall = true;
			}
			// add the fragment
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// if (fromEvent) {
			// fTransaction.remove(eventsListFragment);
			// } else {
			// fTransaction.remove(speakerFragment1);
			// }
			fTransaction.replace(R.id.mainfragment, eventFragment1, eventFragment1Tag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_htitle));
			fTransaction.commit();
			// if it's not the first call, update the speaker data
			if (!firstCall) {
				eventFragment1.updateEvent(eventID);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showSpeakers()
	 */
	@Override
	public void showSpeakers() {
		displayLeaf = false;
		Log.i("FragmentsSwitcherLegacy:showSpeakers", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		SpeakersListFragmentHC speakersListFragment = (SpeakersListFragmentHC) fm
				.findFragmentByTag(speakersListFragmentTag);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment

			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// the speakersListFragment has to be created
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragmentHC();
			}
			fTransaction.replace(R.id.mainfragment, speakersListFragment, speakersListFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentTransaction fTransaction = fm.beginTransaction();
			// the speakersListFragment has to be created
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragmentHC();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, speakersListFragment, speakersListFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jcertif.android.ui.view.speaker.list.SpeakersListCallBack#showSelectedSpeaker(com.jcertif
	 * .android.transverse.model.Speaker)
	 */
	@Override
	public void showSelectedSpeaker(int speakerID) {
		displayLeaf = true;
		Log.i("showSelectedSpeaker", "Selected speaker:" + speakerID);
		Log.i("FragmentsSwitcherLegacy:showSpeakers", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		SpeakersListFragmentHC speakersListFragment = (SpeakersListFragmentHC) fm
				.findFragmentByTag(speakersListFragmentTag);
		if (twoFragmentsVisible) {
			// Set the second fragment visible
			setSecondFragmentVisible(true);
			// Prepare the fragment transaction
			SpeakerDetailFragmentHC speakerFragment2 = (SpeakerDetailFragmentHC) fm.findFragmentByTag(speakerFragment2Tag);
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// Ensure the first fragment is the speakers list:
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragmentHC();
				fTransaction.replace(R.id.mainfragment, speakersListFragment, speakersListFragmentTag);
			}
			// Then manage the speaker detail
			// the speaker has to be created
			if (speakerFragment2 == null) {
				speakerFragment2 = new SpeakerDetailFragmentHC();
				// add it to the secondfragment layout
				fTransaction.replace(R.id.secondfragment, speakerFragment2, speakerFragment2Tag);
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(SpeakerDetailFragmentHC.SPEAKER_ID, speakerID);
				speakerFragment2.setArguments(bundle);
				fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_list_htitle));
			} else {
				// the speakerFragment already exists, update it
				speakerFragment2.updateSpeaker(speakerID);
				// if the fragment is not visible set it visible
				if (!speakerFragment2.isVisible()) {
					fTransaction.replace(R.id.secondfragment, speakerFragment2, speakerFragment2Tag);
				}
				// Add only one level of backstack for the speaker detail
				// so if the last back stack entry is R.string.speaker_htitle do nothing
				if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()
						.equals(mainActivity.getString(R.string.speaker_htitle))) {
					fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_htitle));
				}
			}
			fTransaction.commit();
		} else {

			Boolean firstCall = false;
			// So add the main fragment
			FragmentTransaction fTransaction = fm.beginTransaction();
			SpeakerDetailFragmentHC speakerFragment1 = (SpeakerDetailFragmentHC) fm.findFragmentByTag(speakerFragment1Tag);
			// if the fragment doesn't exist create it
			if (speakerFragment1 == null) {
				speakerFragment1 = new SpeakerDetailFragmentHC();
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(SpeakerDetailFragmentHC.SPEAKER_ID, speakerID);
				speakerFragment1.setArguments(bundle);
				firstCall = true;
			}
			// add the fragment
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.remove(speakersListFragment);
			fTransaction.add(R.id.mainfragment, speakerFragment1, speakerFragment1Tag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_htitle));
			fTransaction.commit();
			// if it's not the first call, update the speaker data
			if (!firstCall) {
				speakerFragment1.updateSpeaker(speakerID);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showCalendar()
	 */
	@Override
	public void showCalendar() {
		displayLeaf = false;
		Log.i("FragmentsSwitcherLegacy:showCalendar", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		CalendarDayFragmentHC calendarDayFragment = (CalendarDayFragmentHC) fm
				.findFragmentByTag(calendarMainFragmentTag);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment

			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// the speakersListFragment has to be created
			if (calendarDayFragment == null) {
				//instanciate the fragment
				calendarDayFragment = getCalendarDayFragmentHC();
			}
			fTransaction.replace(R.id.mainfragment, calendarDayFragment, calendarMainFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.calendar_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentTransaction fTransaction = fm.beginTransaction();
			// the speakersListFragment has to be created
			if (calendarDayFragment == null) {
				//instanciate the fragment
				calendarDayFragment = getCalendarDayFragmentHC();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, calendarDayFragment, calendarMainFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.calendar_htitle));
			fTransaction.commit();
		}
		
		
	}
	
	/**
	 * @return the calendar day fragment to use
	 */
	private CalendarDayFragmentHC getCalendarDayFragmentHC() {
		Calendar day = Calendar.getInstance();
		day.set(Calendar.YEAR, 2012);
		day.set(Calendar.MONTH, 8);
		day.set(Calendar.DAY_OF_MONTH, 3);
		CalendarDayFragmentHC calendarDay  = new CalendarDayFragmentHC();
		// define the adapter
		CalendarDayAdapter adapter = new CalendarDayAdapter(CalendarDayAdapter.ALL_EVENTS, day);
		// and link them
		calendarDay.setAdapter(adapter);
		return calendarDay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showAgenda()
	 */
	@Override
	public void showAgenda() {
		displayLeaf = false;
		Log.i("FragmentsSwitcherLegacy:showCalendar", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		CalendarDayFragmentHC calendarDayFragment = (CalendarDayFragmentHC) fm
				.findFragmentByTag(agendaFragmentTag);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment

			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// the speakersListFragment has to be created
			if (calendarDayFragment == null) {
				//instanciate the fragment
				calendarDayFragment = getAgendaFragmentHC();
			}
			fTransaction.replace(R.id.mainfragment, calendarDayFragment, agendaFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.agenda_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentTransaction fTransaction = fm.beginTransaction();
			// the speakersListFragment has to be created
			if (calendarDayFragment == null) {
				//instanciate the fragment
				calendarDayFragment = getAgendaFragmentHC();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, calendarDayFragment, agendaFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.agenda_htitle));
			fTransaction.commit();
		}

	}
	
	/**
	 * @return the calendar day fragment to use
	 */
	private CalendarDayFragmentHC getAgendaFragmentHC() {
		Calendar day = Calendar.getInstance();
		day.set(Calendar.YEAR, 2012);
		day.set(Calendar.MONTH, 8);
		day.set(Calendar.DAY_OF_MONTH, 3);
		CalendarDayFragmentHC calendarDay  = new CalendarDayFragmentHC();
		// define the adapter
		CalendarDayAdapter adapter = new CalendarDayAdapter(CalendarDayAdapter.STARED_EVENTS, day);
		// and link them
		calendarDay.setAdapter(adapter);
		return calendarDay;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showInfo()
	 */
	@Override
	public void showInfo() {
		displayLeaf = false;
		Log.i("FragmentsSwitcherLegacy:showInfo", "twoFragmentsVisible :" + twoFragmentsVisible);
		FragmentManager fm = mainActivity.getFragmentManager();
		InfoFragmentHC infoFragment = (InfoFragmentHC) fm
				.findFragmentByTag(infoFragmentTag);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the info
			setSecondFragmentVisible(false);
			// So add the info fragment

			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// the speakersListFragment has to be created
			if (infoFragment == null) {
				infoFragment = new InfoFragmentHC();
			}
			fTransaction.replace(R.id.mainfragment, infoFragment, infoFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentTransaction fTransaction = fm.beginTransaction();
			// the speakersListFragment has to be created
			if (infoFragment == null) {
				infoFragment = new InfoFragmentHC();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out,R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, infoFragment, infoFragmentTag);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();
		}

	}

	/******************************************************************************************/
	/** Managing Space **************************************************************************/
	/******************************************************************************************/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#fillSpace()
	 */
	@Override
	public void fillSpace(Boolean force) {
		// This method is used to hide the second fragment when nothing in displayed in it
		if (force) {
			// The caller knows it alone so it force the second fragment hide
			if (twoFragmentsVisible) {
				setSecondFragmentVisible(false);
			}
		} else {
			// check if we are at the second level (speaker list, event list...)
			// we are at the second level if the next click on the back button send the user to the
			// mainFragment
			FragmentManager fm = mainActivity.getFragmentManager();
			if (fm.getBackStackEntryCount() == 1) {
				//
				setSecondFragmentVisible(false);

			}
		}
	}

	/******************************************************************************************/
	/** Getter/Setter **************************************************************************/
	/******************************************************************************************/
	/**
	 * @param mainActivity
	 *            the mainActivity to set
	 */
	public final void setMainActivity(Activity mainActivity) {
		this.mainActivity = mainActivity;
	}

	/**
	 * @param twoFragmentsVisible
	 *            the twoFragmentsVisible to set
	 */
	public final void setTwoFragmentsVisible(Boolean twoFragmentsVisible) {
		this.twoFragmentsVisible = twoFragmentsVisible;
	}

	/******************************************************************************************/
	/** Unused **************************************************************************/
	/******************************************************************************************/
	/**
	 * Tracking backstack changes
	 * TODO Delete when dev is over
	 */
	private void backstackChanged() {
		Log.e("FragmentsSwitcherLegacy:OnBackStackChangedListener", "OnBackStackChangedListener called");
		FragmentManager fm = mainActivity.getFragmentManager();
		BackStackEntry bse;
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			bse = fm.getBackStackEntryAt(i);
			Log.e("FragmentsSwitcherLegacy:BackStackEntry",
					"BackStackEntry[" + i + "] = " + bse + "Id = " + bse.getId() + "Name = " + bse.getName());
		}
	}

}

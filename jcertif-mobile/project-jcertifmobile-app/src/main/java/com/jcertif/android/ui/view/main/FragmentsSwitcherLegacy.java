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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.jcertif.android.ui.view.R;
import com.jcertif.android.ui.view.event.detail.EventDetailCallBack;
import com.jcertif.android.ui.view.event.detail.EventDetailFragment;
import com.jcertif.android.ui.view.event.list.EventsListCallBack;
import com.jcertif.android.ui.view.event.list.EventsListFragment;
import com.jcertif.android.ui.view.main.fragment.MainFragment;
import com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack;
import com.jcertif.android.ui.view.speaker.detail.SpeakerDetailCallBack;
import com.jcertif.android.ui.view.speaker.detail.SpeakerDetailFragment;
import com.jcertif.android.ui.view.speaker.list.SpeakersListCallBack;
import com.jcertif.android.ui.view.speaker.list.SpeakersListFragment;

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
public class FragmentsSwitcherLegacy implements MainFragmentCallBack, SpeakersListCallBack, SpeakerDetailCallBack,
		EventDetailCallBack, EventsListCallBack {
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
	FragmentActivity mainActivity = null;
	/**
	 * The main fragment
	 */
	Fragment mainFragment;
	/**
	 * The speakers List fragment
	 */
	SpeakersListFragment speakersListFragment;
	/**
	 * The speaker fragment for the first layout
	 */
	SpeakerDetailFragment speakerFragment1;
	/**
	 * The speaker fragment for the second layout
	 */
	SpeakerDetailFragment speakerFragment2;
	/**
	 * The events List fragment
	 */
	EventsListFragment eventsListFragment;
	/**
	 * The event fragment for the first layout
	 */
	EventDetailFragment eventFragment1;
	/**
	 * The event fragment for the second layout
	 */
	EventDetailFragment eventFragment2;
	/**
	 * The calendar main fragment
	 */
	Fragment calendarMainFragment;
	/**
	 * The calendar detail fragment
	 */
	Fragment calendarDetailFragment;
	/**
	 * The agenda fragment
	 */
	Fragment agendaFragment;
	/**
	 * The agenda detail fragment
	 */
	Fragment agendaDetailFragment;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param mainActivity
	 */
	public FragmentsSwitcherLegacy(FragmentActivity mainActivity, Boolean twoFragmentsVisible) {
		this.mainActivity = mainActivity;
		this.twoFragmentsVisible = twoFragmentsVisible;
		FragmentManager fm = mainActivity.getSupportFragmentManager();
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
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mainActivity.findViewById(
					R.id.mainfragment).getLayoutParams();
			params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
			mainActivity.findViewById(R.id.mainfragment).setLayoutParams(params);
		} else {
			mainActivity.findViewById(R.id.secondfragment).setVisibility(View.GONE);
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mainActivity.findViewById(
					R.id.mainfragment).getLayoutParams();
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
		Log.i("FragmentsSwitcherLegacy:initialize", "twoFragmentsVisible :" + twoFragmentsVisible);
		// Now we can instantiate the appropriate fragment
		// if not in recreation mode we can act we are in the creation mode
		// so we instantiate the main fragment
		FragmentManager fm = mainActivity.getSupportFragmentManager();
		FragmentTransaction fTransaction = fm.beginTransaction();
		if (!isRecreationMode) {// ||mainFragment==null
			mainFragment = new MainFragment();
		} else {
			mainFragment = fm.findFragmentById(R.id.mainfragment);
		}
		// then set the animation, add the fragment and commit
		fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
		fTransaction.add(R.id.mainfragment, mainFragment);
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
		Log.i("FragmentsSwitcherLegacy:showEvents", "twoFragmentsVisible :" + twoFragmentsVisible);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			if (eventsListFragment == null) {
				eventsListFragment = new EventsListFragment();
			}
			fTransaction.replace(R.id.mainfragment, eventsListFragment);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			if (eventsListFragment == null) {
				eventsListFragment = new EventsListFragment();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, eventsListFragment, "eventsListFragmenttag");
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
	public void showSelectedEvent(int eventID,boolean fromEvent) {
		Log.i("showSelectedEvent", "Selected event:" + eventID);
		Log.i("FragmentsSwitcherLegacy:showSelectedEvent", "twoFragmentsVisible :" + twoFragmentsVisible);
		if (twoFragmentsVisible) {
			// Set the second fragment visible
			setSecondFragmentVisible(true);
			// Prepare the fragment transaction
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// Ensure the first fragment is the speakers list:
			if (eventsListFragment == null) {
				eventsListFragment = new EventsListFragment();
				fTransaction.replace(R.id.mainfragment, eventsListFragment);
			}
			// Then manage the speaker detail
			if (eventFragment2 == null) {
				// the speaker has to be created
				eventFragment2 = new EventDetailFragment();
				// add it to the secondfragment layout
				fTransaction.replace(R.id.secondfragment, eventFragment2);
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(EventDetailFragment.EVENT_ID, eventID);
				eventFragment2.setArguments(bundle);
				fTransaction.addToBackStack(mainActivity.getString(R.string.sessions_list_htitle));
			} else {
				// the speakerFragment already exists, update it
				eventFragment2.updateEvent(eventID);
				// if the fragment is not visible set it visible
				if (!eventFragment2.isVisible()) {
					fTransaction.replace(R.id.secondfragment, eventFragment2);
				}
				
				if(fromEvent) {
					// Add only one level of backstack for the speaker detail
					// so if the last back stack entry is R.string.speaker_htitle do nothing
					if (!fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName()
							.equals(mainActivity.getString(R.string.speaker_htitle))) {
						fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_htitle));
					}
				}else {
					fTransaction.addToBackStack(mainActivity.getString(R.string.session_htitle));
				}
				
			}
			fTransaction.commit();
		} else {
			Boolean firstCall = false;
			// So add the main fragment
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			// if the fragment doesn't exist create it
			if (eventFragment1 == null) {
				eventFragment1 = new EventDetailFragment();
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(EventDetailFragment.EVENT_ID, eventID);
				eventFragment1.setArguments(bundle);
				firstCall = true;
			}
			// add the fragment
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			if(fromEvent) {
				fTransaction.remove(eventsListFragment);
			}else {
				fTransaction.remove(speakerFragment1);
			}
			fTransaction.add(R.id.mainfragment, eventFragment1);
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
		Log.i("FragmentsSwitcherLegacy:showSpeakers", "twoFragmentsVisible :" + twoFragmentsVisible);
		if (twoFragmentsVisible) {
			// hide the second fragment because we displays the list of speakers without any
			// speakers selected
			setSecondFragmentVisible(false);
			// So add the speakers fragment
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragment();
			}
			fTransaction.replace(R.id.mainfragment, speakersListFragment);
			fTransaction.addToBackStack(mainActivity.getString(R.string.main_htitle));
			fTransaction.commit();

		} else {
			// So add the speaker fragment
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragment();
			}
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.replace(R.id.mainfragment, speakersListFragment, "speakersListFragmenttag");
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
		Log.i("showSelectedSpeaker", "Selected speaker:" + speakerID);
		Log.i("FragmentsSwitcherLegacy:showSpeakers", "twoFragmentsVisible :" + twoFragmentsVisible);
		if (twoFragmentsVisible) {
			// Set the second fragment visible
			setSecondFragmentVisible(true);
			// Prepare the fragment transaction
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			// Ensure the first fragment is the speakers list:
			if (speakersListFragment == null) {
				speakersListFragment = new SpeakersListFragment();
				fTransaction.replace(R.id.mainfragment, speakersListFragment);
			}
			// Then manage the speaker detail
			if (speakerFragment2 == null) {
				// the speaker has to be created
				speakerFragment2 = new SpeakerDetailFragment();
				// add it to the secondfragment layout
				fTransaction.replace(R.id.secondfragment, speakerFragment2);
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(SpeakerDetailFragment.SPEAKER_ID, speakerID);
				speakerFragment2.setArguments(bundle);
				fTransaction.addToBackStack(mainActivity.getString(R.string.speaker_list_htitle));
			} else {
				// the speakerFragment already exists, update it
				speakerFragment2.updateSpeaker(speakerID);
				// if the fragment is not visible set it visible
				if (!speakerFragment2.isVisible()) {
					fTransaction.replace(R.id.secondfragment, speakerFragment2);
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
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			FragmentTransaction fTransaction = fm.beginTransaction();
			// if the fragment doesn't exist create it
			if (speakerFragment1 == null) {
				speakerFragment1 = new SpeakerDetailFragment();
				// The bundle is used to give to the speakerFragment the id of the selected speaker
				Bundle bundle = new Bundle();
				bundle.putInt(SpeakerDetailFragment.SPEAKER_ID, speakerID);
				speakerFragment1.setArguments(bundle);
				firstCall = true;
			}
			// add the fragment
			fTransaction.setCustomAnimations(R.anim.anim_push_left_in, R.anim.anim_push_left_out);
			fTransaction.remove(speakersListFragment);
			fTransaction.add(R.id.mainfragment, speakerFragment1);
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
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showAgenda()
	 */
	@Override
	public void showAgenda() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcertif.android.ui.view.main.fragment.MainFragmentCallBack#showInfo()
	 */
	@Override
	public void showInfo() {
		// TODO Auto-generated method stub

	}

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
			FragmentManager fm = mainActivity.getSupportFragmentManager();
			if (fm.getBackStackEntryCount() == 1) {
				//
				setSecondFragmentVisible(false);

			}
		}
	}

	/**
	 * Tracking backstack changes
	 * TODO Delete when dev is over
	 */
	private void backstackChanged() {
		Log.e("FragmentsSwitcherLegacy:OnBackStackChangedListener", "OnBackStackChangedListener called");
		FragmentManager fm = mainActivity.getSupportFragmentManager();
		BackStackEntry bse;
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			bse = fm.getBackStackEntryAt(i);
			Log.e("FragmentsSwitcherLegacy:BackStackEntry",
					"BackStackEntry[" + i + "] = " + bse + "Id = " + bse.getId() + "Name = " + bse.getName());
		}
	}

}

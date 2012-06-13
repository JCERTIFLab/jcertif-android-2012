/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.service.androidservices</li>
 * <li>28 mai 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 *
 /**
 * <ul>
 * Android Tutorial, An <strong>Android2EE</strong>'s project.</br> 
 * Produced by <strong>Dr. Mathias SEGUY</strong>.</br>
 * Delivered by <strong>http://android2ee.com/</strong></br>
 *  Belongs to <strong>Mathias Seguy</strong></br>
 ****************************************************************************************************************</br>
 * This code is free for any usage except training and can't be distribute.</br>
 * The distribution is reserved to the site <strong>http://android2ee.com</strong>.</br>
 * The intelectual property belongs to <strong>Mathias Seguy</strong>.</br>
 * <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * 
 * *****************************************************************************************************************</br>
 *  Ce code est libre de toute utilisation mais n'est pas distribuable.</br>
 *  Sa distribution est reservée au site <strong>http://android2ee.com</strong>.</br> 
 *  Sa propriété intellectuelle appartient à <strong>Mathias Seguy</strong>.</br>
 *  <em>http://mathias-seguy.developpez.com/</em></br> </br>
 * *****************************************************************************************************************</br>
 */
package com.jcertif.android.service.androidservices;

import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jcertif.android.service.androidservices.events.EventsUpdater;
import com.jcertif.android.service.androidservices.speakers.SpeakersUpdater;
import com.jcertif.android.ui.view.R;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to Launch in a bunch of threads all the data updaters
 *        They have to ask the new data unsing HTTP
 *        To parse them and to inject those data in the database
 *        To do that this class use a bunch of elements that implements the
 *        UpdaterServiceElementIntf interface. Each of those element have in charge a specific
 *        update (session, speakers, events...)
 */
public class UpdaterService extends Service {

	/**
	 * The list of elements that have to update the data
	 * Each element has to update a specific data (spreakers, events,...)
	 */
	List<UpdaterServiceElementIntf> updaters;
	/**
	 * The threads (each updater will runs in its own threads)
	 */
	protected List<Thread> backgroundThreads;

	/**
	 * To use to notify the user that updates are runnning
	 */
	private NotificationManager mNM;

	// Unique Identification Number for the Notification.
	// We use it on Notification start, and to cancel it.
	private int NOTIFICATION = R.string.updateServiceNotificationMessage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// This service will never be bound to anyone
		// It is called by onStart
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		// Display a notification about us starting. We put an icon in the status bar.
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// instantiate all the update services
		updaters = new ArrayList<UpdaterServiceElementIntf>();
		updaters.add(new SpeakersUpdater());
		updaters.add(new EventsUpdater());
		// create the Threads
		backgroundThreads = new ArrayList<Thread>();
		Thread backgroundThread;
		for (final UpdaterServiceElementIntf updater : updaters) {
			// Define the Threads
			backgroundThread = new Thread(new Runnable() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					try {
						updater.onUpdate();
					} catch (Throwable t) {
						// just end the background thread
					}
				}
			});
			backgroundThread.setName("UpdateThread" + updater.getName());
			backgroundThreads.add(backgroundThread);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("UpdaterService", "Received start id " + startId + ": " + intent);
		showNotification();
		// Launch the thread// start the thread
		for (Thread backgroundThread : backgroundThreads) {
			Log.i("UpdaterService", "RbackgroundThread" + startId + ": " + intent);
			if (!backgroundThread.isAlive()) {
				try {
					backgroundThread.start();
				} catch (IllegalThreadStateException e) {
					// Thread already running
				}
			}
		}
		// When the threatment is finished call stopSelf... pas facile ça

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);
		// Kill the thread

		// Tell the user we stopped.
		Toast.makeText(this, R.string.updateServiceStopMessage, Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// TODO MSE mettre en place la notification, le PendingIntent doit la stopper
		// NotificationManager mNM = (NotificationManager)
		// getSystemService(Context.NOTIFICATION_SERVICE);
		// // In this sample, we'll use the same text for the ticker and the expanded notification
		// CharSequence text = "Updates Jcertif running";
		//
		// // Set the icon, scrolling text and timestamp
		// Notification notification = new Notification(R.drawable.logo, text,
		// System.currentTimeMillis());
		//
		// // The PendingIntent to launch our activity if the user selects this notification
		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
		// LocalServiceActivities.Controller.class), 0);
		//
		// // Set the info for the views that show in the notification panel.
		// notification.setLatestEventInfo(this, "JCertif", text, null);
		//
		// // Send the notification.
		// mNM.notify(NOTIFICATION, notification);
		// ou faire comme ça:
		// //Récupération du notification Manager
		// final NotificationManager notificationManager =
		// (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		//
		// //Création de la notification avec spécification de l'icone de la notification et le
		// texte qui apparait à la création de la notfication
		// final Notification notification = new Notification(R.drawable.notification,
		// notificationTitle, System.currentTimeMillis());
		//
		// //Definition de la redirection au moment du clique sur la notification. Dans notre cas la
		// notification redirige vers notre application
		// final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
		// TutoNotificationHomeActivity.class), 0);
		//
		// //Récupération du titre et description de la notfication
		// final String notificationTitle = getResources().getString(R.string.notification_title);
		// final String notificationDesc = getResources().getString(R.string.notification_desc);
		//
		// //Notification & Vibration
		// notification.setLatestEventInfo(this, notificationTitle, notificationDesc,
		// pendingIntent);
		// notification.vibrate = new long[] {0,200,100,200,100,200};
		//
		// notificationManager.notify(NOTIFICATION_ID, notification);
	}
}

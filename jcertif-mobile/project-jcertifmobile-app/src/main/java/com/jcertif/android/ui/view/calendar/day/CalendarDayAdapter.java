/**<ul>
 * <li>project-jcertifmobile-app</li>
 * <li>com.jcertif.android.ui.view.calendar</li>
 * <li>5 juin 2012</li>
 * 
 * <li>======================================================</li>
 *
 * <li>Projet : JCertif Africa 2012 Project</li>
 * <li>Produit par MSE.</li>
 */
package com.jcertif.android.ui.view.calendar.day;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.jcertif.android.dao.ormlight.EventProvider;
import com.jcertif.android.dao.ormlight.SpeakerProvider;
import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.transverse.model.Periode;
import com.jcertif.android.transverse.model.Speaker;

/**
 * @author Mathias Seguy (Android2EE)
 * @goals
 *        This class aims to manage the events to be displayed in the calendar for a specific day
 */
public class CalendarDayAdapter {
	/******************************************************************************************/
	/** Constants **************************************************************************/
	/******************************************************************************************/

	/**
	 * To load all events to use in the constructor
	 */
	public static final int ALL_EVENTS = 0;
	/**
	 * To load stared events to use in the constructor
	 */
	public static final int STARED_EVENTS = 1;
	/**
	 * The size of the row in minutes
	 */
	public static final int ROW_CURRENT_SIZE = 15;
	/**
	 * The first hour of the day displayed
	 */
	public static final int FIRST_HOUR_OF_DAY = 7;
	/**
	 * The last hour of the day displayed
	 */
	public static final int LAST_HOUR_OF_DAY = 22;
	/******************************************************************************************/
	/** Attribute **************************************************************************/
	/******************************************************************************************/

	/**
	 * The list of events
	 */
	List<Event> events = null;
	/**
	 * the calendar
	 */
	CalendarDayFragment calendar;
	/**
	 * The day to display
	 */
	Calendar dayToDisplay;
	/**
	 * If it's ALL_EVENTS or STARED_EVENTS
	 */
	int eventType;
	/**
	 * The map to remember what is the left cell for a specific hour
	 * <HourCellId,LeftCellId>
	 */
	Map<Integer, Integer> leftId;
	/**
	 * The map to remember what is the left cell level for a specific hour
	 * <HourCellId,level> The level is the number of cells at that hour
	 */
	Map<Integer, Integer> leftLevel;
	/**
	 * The speaker provider
	 */
	SpeakerProvider speakersProvider;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	/**
	 * @param events
	 */
	/**
	 * @param eventType
	 *            The constant CalendarDayAdapter.ALL_EVENT or CalendarDayAdapter.STARED_EVENTS
	 */
	public CalendarDayAdapter(int eventType, Calendar day) {
		super();
		dayToDisplay = day;
		this.eventType = eventType;
		leftId = new HashMap<Integer, Integer>();
		leftLevel = new HashMap<Integer, Integer>();
		initData();
	}

	/**
	 * @param step
	 */
	void changeDay(int step) {
		dayToDisplay.set(Calendar.DAY_OF_MONTH, dayToDisplay.get(Calendar.DAY_OF_MONTH) + step);
		leftId.clear();
		leftLevel.clear();
		initData();

	}

	/**
	 * 
	 */
	private void initData() {
		if (eventType == ALL_EVENTS) {
			loadAllEvents();
		} else if (eventType == STARED_EVENTS) {
			loadStaredEvents();
		}
		Collections.sort(events);
		// then build the data
		buildData();
	}

	/******************************************************************************************/
	/** Loading events **************************************************************************/
	/******************************************************************************************/

	/**
	 * Load all the event in the events list
	 */
	private void loadAllEvents() {
		// retrieve data in the database
		EventProvider ep;
		try {
			ep = new EventProvider();
			events = ep.getEventsOfTheDay(dayToDisplay);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load the stared event in the events list
	 */
	private void loadStaredEvents() {
		EventProvider ep;
		try {
			ep = new EventProvider();
			events = ep.getStaredEventsOfTheDay(dayToDisplay);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The map that link a Day with the map that links the event and their position in the relative
	 * layout
	 */
//	Map<Integer, Map<Event, RelativeCellPosition>> dayToEventPosition;
	/**
	 * The event to position map of the dayToDisplay
	 */
	Map<Event, RelativeCellPosition> currentEventToPosition;

	/**
	 * This method build the map that link an event with its relative position in the layout
	 */
	private void buildData() {
		Log.e("CalendarDayAdapter:buildData", "buildData of "+dayToDisplay.get(Calendar.DAY_OF_MONTH));
//		if (null == dayToEventPosition.get(dayToDisplay.get(Calendar.DAY_OF_MONTH))) {
			Log.e("CalendarDayAdapter:buildData", "null == dayToEventPosition.get(dayToDisplay)");
			// then build the map
			currentEventToPosition = new HashMap<Event, RelativeCellPosition>();
			// then for each event find its top, bottom and left alignment
			RelativeCellPosition rcp;
			for (final Event event : events) {				
				// build the relative position of the event
				rcp = new RelativeCellPosition();
				rcp.top = getCellId(event.startDate);
				rcp.bottom = getPreviousCellId(event.endDate);
				rcp.leftCell = getLevelRightOf(event);
				Log.e("CalendarDayAdapter:buildData", "buildData of the "+event.id+" top: "+rcp.top+" bottom:"+rcp.bottom+" left:"+rcp.leftCell);
				// add it to the map
				currentEventToPosition.put(event, rcp);
			}
			// add the map of the day to the dayToEventPosition
//			dayToEventPosition.put(dayToDisplay.get(Calendar.DAY_OF_MONTH), eventToPosition);
//		}
//		Log.e("CalendarDayAdapter:buildData", "buildData returns "+dayToEventPosition.get(dayToDisplay.get(Calendar.DAY_OF_MONTH)).size()+" done");
		// update the currentMap
//		currentEventToPosition = dayToEventPosition.get(dayToDisplay.get(Calendar.DAY_OF_MONTH));

	}

	/**
	 * Return the cell id to align on top for the event
	 * @param event
	 * @return the cell id to align on top for the event
	 */
	public int getTopAlign(Event event) {
		return currentEventToPosition.get(event).top;
	}
	/**
	 *  Return the cell id to align on bottom for the event
	 * @param event
	 * @return the cell id to align on bottom for the event
	 */
	public int getBottomAlign(Event event) {
		return currentEventToPosition.get(event).bottom;
	}
	/**
	 *  Return the cell id to align on left for the event
	 * @param event
	 * @return the cell id to align on left for the event
	 */
	public int getLeftAlign(Event event) {
		return currentEventToPosition.get(event).leftCell;
	}

	/******************************************************************************************/
	/** Usefull methods **************************************************************************/
	/******************************************************************************************/

	public String getStartHour(Event event) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(event.startDate);
		return formatDate(cal);
	}

	/**
	 * Format the date to display the hour:minute
	 * 
	 * @param cal
	 *            the date to format
	 * @return the return formated string (ex 8:00)
	 */
	public String formatDate(Calendar cal) {
		StringBuilder strB = new StringBuilder();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		if (hour < 10) {
			strB.append(0);
		}
		strB.append(hour);
		strB.append(":");
		int minute = cal.get(Calendar.MINUTE);
		if (minute < 10) {
			strB.append(0);
		}
		strB.append(minute);

		return strB.toString();
	}

	/**
	 * Format the date to display the 3 Septembre
	 * 
	 * @param cal
	 *            the date to format
	 * @return the return formated string (ex 3 Septembre)
	 */
	public String formatTitleDate(Calendar cal) {
		StringBuilder strB = new StringBuilder();
		strB.append(cal.get(Calendar.DAY_OF_MONTH));
		return strB.toString();
	}

	/**
	 * Return the Cell Id associated to a hour cell
	 * 
	 * @param cal
	 *            the hour
	 * @return the cell Id to use for the HourCell
	 */
	public int getCellId(Calendar cal) {
		return (cal.get(Calendar.HOUR_OF_DAY) * 100) + cal.get(Calendar.MINUTE);
	}

	/**
	 * Return the Cell Id associated to a hour cell
	 * 
	 * @param date
	 *            the hour
	 * @return the cell Id to use for the HourCell
	 */
	public int getCellId(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// only quarter work so
		// 8h14=>8h00
		int minute = cal.get(Calendar.MINUTE);
		minute = (minute / CalendarDayAdapter.ROW_CURRENT_SIZE) * CalendarDayAdapter.ROW_CURRENT_SIZE;
		return (cal.get(Calendar.HOUR_OF_DAY) * 100) + minute;
	}

	/**
	 * Return the Cell Id associated to the previous hour cell
	 * 
	 * @param date
	 *            the hour
	 * @return the cell Id to use for the previous HourCell
	 */
	public int getPreviousCellId(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - CalendarDayAdapter.ROW_CURRENT_SIZE);
		// only quarter work so
		// 8h14=>8h00
		int minute = cal.get(Calendar.MINUTE);
		minute = (minute / CalendarDayAdapter.ROW_CURRENT_SIZE) * CalendarDayAdapter.ROW_CURRENT_SIZE;
		return (cal.get(Calendar.HOUR_OF_DAY) * 100) + minute;
	}

	/**
	 * @param event
	 * @return the speaker name of the event
	 */
	public String getSpeakerName(Event event) {
		StringBuilder strName;
		Speaker sp = null;
		try {
			if (speakersProvider == null) {
				speakersProvider = new SpeakerProvider();
			}
			sp = speakersProvider.findById(Integer.parseInt(event.speakersId));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (sp != null) {
			strName = new StringBuilder(sp.firstName);
			strName.append(" ");
			strName.append(sp.lastName);
		} else {
			strName = new StringBuilder("Unknown");
		}
		return strName.toString();
	}

	/**
	 * Return the left cell id to add this event on its right
	 * 
	 * @param event
	 *            the event to place
	 * @return the cellId to be rightOf
	 */
	public int getLevelRightOf(Event event) {
		Log.e("CalendarDayAdapter:getLevelRightOf", "Debut avec event :" + event.id);
		// The list of all the hourCellId
		List<Integer> leftCells = getHourCellsId(event);
		Log.e("CalendarDayAdapter:getLevelRightOf", "");
		// on instancie à la première cellules
		Integer leftCellId = leftId.get(leftCells.get(0));
		// on trouve la cellule non nulle:
		for (Integer leftCellsId : leftCells) {
			if (leftCellId == null) {
				leftCellId = leftId.get(leftCellsId);
			}
		}
		// on construit l'ihm
		if (leftCellId != null) {
			Log.e("CalendarDayAdapter:getLevelRightOf", "leftCellId != null");
			// on reupere son niveau de profondeur horizontal
			Integer maxLevel = leftLevel.get(leftCells.get(0));
			Log.e("CalendarDayAdapter:getLevelRightOf", "maxLevel " + maxLevel + " for " + leftCellId);
			// si le level est null c'est qu'il est à 1
			if (maxLevel == null) {
				maxLevel = 1;
			}
			// on parcours toutes les cellules pour savoir la ligne de plus grande largeur
			for (Integer leftCell : leftCells) {
				Log.e("CalendarDayAdapter:getLevelRightOf", "Looking for leftCellId: " + leftCell
						+ " leftLevel.get(leftCell):" + leftLevel.get(leftCell));
				// si une ligne est plus large on met a jour
				if (null != leftLevel.get(leftCell) && leftLevel.get(leftCell) > maxLevel) {
					leftCellId = leftId.get(leftCell);
					maxLevel = leftLevel.get(leftCell);
					Log.e("CalendarDayAdapter:getLevelRightOf", "New left cell found leftCellId" + leftCellId
							+ "maxLevel" + maxLevel);
				}
			}
			maxLevel++;
			// the update all the leftLevel and leftId reference
			for (Integer leftCell : leftCells) {
				Log.e("CalendarDayAdapter:getLevelRightOf", "leftLevel mis a jour avec " + leftCell + " maxLevel "
						+ maxLevel + " evtid " + event.id);
				leftLevel.put(leftCell, maxLevel);
				leftId.put(leftCell, event.id * 10000);
			}
		} else {
			Log.e("CalendarDayAdapter:getLevelRightOf", "leftCellId == null");
			leftCellId = getCellId(event.startDate);
			// the update all the leftLevel and leftId reference
			for (Integer leftCell : leftCells) {
				leftLevel.put(leftCell, 1);
				leftId.put(leftCell, event.id * 10000);
				Log.e("CalendarDayAdapter:getLevelRightOf", "leftLevel mis a jour avec " + leftCell + " maxLevel " + 1
						+ " evtid " + event.id);
			}
		}
		Log.e("CalendarDayAdapter:getLevelRightOf", "Debut avec event :" + event.id + " renvoie leftCellId "
				+ leftCellId);
		return leftCellId;
	}

	/**
	 * Renvoie la liste de tous les CellId qui compose cet évènement
	 * 
	 * @param event
	 * @return
	 */
	private List<Integer> getHourCellsId(Event event) {
		List<Integer> ret = new ArrayList<Integer>();
		// retrouve le debut et la fin
		Calendar stratCal = Calendar.getInstance();
		stratCal.setTime(event.startDate);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(event.endDate);
		// construit la liste de toutes le identifiants de cellules hourCellId entre le debut et la
		// fin
		while (stratCal.get(Calendar.HOUR_OF_DAY) * 100 + stratCal.get(Calendar.MINUTE) < endCal
				.get(Calendar.HOUR_OF_DAY) * 100 + endCal.get(Calendar.MINUTE)) {
			ret.add(stratCal.get(Calendar.HOUR_OF_DAY) * 100 + stratCal.get(Calendar.MINUTE));
			stratCal.set(Calendar.MINUTE, stratCal.get(Calendar.MINUTE) + CalendarDayAdapter.ROW_CURRENT_SIZE);
			Log.e("CalendarDayAdapter:getHourCellsId", "Ajout de "
					+ (stratCal.get(Calendar.HOUR_OF_DAY) * 100 + stratCal.get(Calendar.MINUTE)));
		}
		Log.e("CalendarDayAdapter:getHourCellsId", "fin de la construction de la liste");
		return ret;
	}

	/******************************************************************************************/
	/** Getters/Setters **************************************************************************/
	/******************************************************************************************/

	/**
	 * @return the calendar
	 */
	public final CalendarDayFragment getCalendar() {
		return calendar;
	}

	/**
	 * @param calendar
	 *            the calendar to set
	 *            This method is package (not public, neither private, neither protected)
	 *            To be calledo only by CalendarDayFragment
	 */
	final void setCalendar(CalendarDayFragment calendar) {
		this.calendar = calendar;
	}

	/**
	 * @return the dayToDisplay
	 */
	public final Calendar getDayToDisplay() {
		return dayToDisplay;
	}

	/**
	 * @author Mathias Seguy (Android2EE)
	 * @goals
	 *        This class aims to: store the relative position of a cell
	 */
	public class RelativeCellPosition {
		public int top = 0;
		public int bottom = 0;
		public int leftCell = 0;
	}

}

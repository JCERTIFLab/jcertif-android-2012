package com.jcertif.android.transverse.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.codehaus.jackson.annotate.JsonProperty;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Event model.
 * 
 * @author: rossi.oddet
 */
@DatabaseTable(tableName = "events")
public class Event implements Comparable<Event> {
	@DatabaseField(id = true)
	public Integer id;
	@DatabaseField
	@JsonProperty(value = "nom")
	public String name;
	@DatabaseField
	@JsonProperty(value = "dateDebut")
	private Date startDate;
	@DatabaseField
	@JsonProperty(value = "dateFin")
	private Date endDate;
	@DatabaseField
	@JsonProperty(value = "salle")
	public String room;
	@DatabaseField
	@JsonProperty(value = "description")
	public String description;
	@DatabaseField
	@JsonProperty(value = "motCle")
	public String keyWord;
	@DatabaseField
	@JsonProperty(value = "sommaire")
	public String summary;
	@DatabaseField
	@JsonProperty(value = "speakersId")
	public String speakersId;
	@DatabaseField
	@JsonProperty(value = "sujets")
	public String subjects;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Event another) {
		// first compare lenght
		Long lenghtThis = this.endDate.getTime() - this.startDate.getTime();
		Long lenghtAnother = another.endDate.getTime() - another.startDate.getTime();
		if (lenghtThis.compareTo(lenghtAnother) != 0) {
			return (lenghtThis.compareTo(lenghtAnother));
		} else if (this.startDate.compareTo(another.startDate) != 0) {
			return this.startDate.compareTo(another.startDate);
		} else if (this.endDate.compareTo(another.endDate) != 0) {
			return this.endDate.compareTo(another.endDate);
		} else {
			return this.room.compareTo(another.room);
		}
	}

	public String getTime() {
		return startDate.toString().substring(0, 10) + " ...";
		// return time;
	}

	public String getRoom() {
		return room;
	}

	/**
	 * @return the startDate
	 */
	public final Date getStartDate() {
		// Change to GMT:
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)-2);
		return cal.getTime();
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public final void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public final Date getEndDate() {
		// Change to GMT:
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTime(endDate);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY)-2);
		return cal.getTime();
	}

	/**
	 * @param endDate
	 *            the endDate to set
	 */
	public final void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}

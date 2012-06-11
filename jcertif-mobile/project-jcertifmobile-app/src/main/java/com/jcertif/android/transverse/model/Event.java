package com.jcertif.android.transverse.model;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Event model.
 *
 * @author: rossi.oddet
 */
@DatabaseTable(tableName = "events")
public class Event  implements Comparable<Event>{
	@DatabaseField(id = true)
	public Integer id;
	@DatabaseField
	@JsonProperty(value="nom")
	public String name;
	@DatabaseField
	@JsonProperty(value="dateDebut")
	public Date startDate;
	@DatabaseField
	@JsonProperty(value="dateFin")
	public Date endDate;
	@DatabaseField
	@JsonProperty(value="salle")
	public String room;
	@DatabaseField
	@JsonProperty(value="description")
	public String description;
	@DatabaseField
	@JsonProperty(value="motCle")
	public String keyWord;
	@DatabaseField
	@JsonProperty(value="sommaire")
	public String summary;
	@DatabaseField
	@JsonProperty(value="speakersId")
	public String speakersId;
	@DatabaseField
	@JsonProperty(value="sujets")
	public String subjects;
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Event another) {
		if(this.startDate.compareTo(another.startDate)!=0) {
			return this.startDate.compareTo(another.startDate);
		}else if(this.endDate.compareTo(another.endDate)!=0) {
			return this.endDate.compareTo(another.endDate);
		}else {
			return this.room.compareTo(another.room);
		}
	}
	public String getTime(){
		return startDate.toString().substring(0, 10) + " ...";
		// return time;
	}
	
	public String getRoom(){
		return room;
	}	
}

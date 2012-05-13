package com.jcertif.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Event model.
 *
 * @author: rossi.oddet
 */
@DatabaseTable(tableName = "events")
public class Event {
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
	
	public String getTime(){
		return startDate.toString().substring(0, 10) + " ...";
		// return time;
	}
	
	public String getRoom(){
		return room;
	}	
}

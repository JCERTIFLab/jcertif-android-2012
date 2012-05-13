package com.jcertif.android.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * Our Speaker model
 * @author mouhamed_diouf
 *
 */

@DatabaseTable(tableName = "speakers")
public class Speaker {
	@DatabaseField(id = true)
	@JsonProperty("id")
	public int id;
	@DatabaseField
	@JsonProperty("prenom")
	public String firstName;
	@DatabaseField
	@JsonProperty("nom")
	public String lastName;
	@DatabaseField
	@JsonProperty("bio")
	public String bio;
	@DatabaseField
	@JsonProperty("compagnie")
	public String company;
	@DatabaseField
	@JsonProperty("photo")
	public String urlPhoto; 
}

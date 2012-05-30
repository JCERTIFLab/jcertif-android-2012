package com.jcertif.android.transverse.model;

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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder strb=new StringBuilder();
		strb.append("Nom[")
		.append(lastName)
		.append("][Prenom")
		.append(firstName)
		.append("][id")
		.append(id)
		.append("][bio")
		.append(bio)
		.append("][Company")
		.append(company)
		.append("][photo")
		.append(urlPhoto);
		return strb.toString();
	} 
	
}

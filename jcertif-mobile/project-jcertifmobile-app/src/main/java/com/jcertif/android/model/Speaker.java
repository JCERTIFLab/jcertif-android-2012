package com.jcertif.android.model;

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
	public int id;
	@DatabaseField
	public String firstName;
	@DatabaseField
	public String lastName;
	@DatabaseField
	public String bio;
	@DatabaseField
	public String company;
	@DatabaseField
	public String urlPhoto; 
}

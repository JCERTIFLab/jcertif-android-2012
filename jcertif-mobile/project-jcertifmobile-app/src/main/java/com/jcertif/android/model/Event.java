package com.jcertif.android.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;

/**
 * Event model.
 *
 * @author: rossi.oddet
 */
@DatabaseTable(tableName = "events")
public class Event {
    @DatabaseField(id = true)
    public Long id;
    @DatabaseField
    public String name;
    @DatabaseField
    public Date startDate;
    @DatabaseField
    public Date endDate;
    @DatabaseField
    public String room;
    @DatabaseField
    public String summary;
    @DatabaseField
    public String description;
    @DatabaseField
    public String speakersId;
    @DatabaseField
    public String keyWord;
    @DatabaseField
    public String subjects;
}

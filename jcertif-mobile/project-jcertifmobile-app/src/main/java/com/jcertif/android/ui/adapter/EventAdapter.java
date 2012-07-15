package com.jcertif.android.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcertif.android.R;
import com.jcertif.android.service.business.stardevents.StaredEventsService;
import com.jcertif.android.transverse.model.Event;

/**
 * from http://www.vogella.de/articles/AndroidListView/article.html
 * 
 * @author Yakhya DABO
 * @author Mathias Seguy
 */
public class EventAdapter extends ArrayAdapter<Event> {
	/**
	 * The maximal lenght of the description
	 */
	private final int MAX_DESC_LENGHT=200;
	/**
	 * Even position for item
	 */
	private static final int TYPE_ITEM = 1;
	/**
	 * Odd position for item
	 */
	private static final int TYPE_ITEM_ODD = 0;
	/**
	 * The inflater
	 */
	private LayoutInflater mInflater;
	/**
	 * The background for even rows
	 */
	GradientDrawable evenBackground = null;
	/**
	 * The background for odd rows
	 */
	GradientDrawable oddBackground = null;
	/**
	 * The star
	 */
	Drawable star=null;
	/**
	 * The empty star
	 */
	Drawable emptyStar=null;

	public EventAdapter(Activity context, List<Event> events) {
		super(context, R.layout.event_list_item, events);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		evenBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item);
		oddBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item_odd);
		star=(Drawable) context.getResources().getDrawable(R.drawable.star);
		emptyStar=(Drawable) context.getResources().getDrawable(R.drawable.empty_star);
}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public TextView place = null;
		public TextView time = null;
		public TextView name = null;
		public TextView description = null;
		public ImageView star=null;
		public LinearLayout eventLay=null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout
		final Event event = getItem(position);

		// Recycle existing view if passed as parameter
		// This will save memory and time on Android
		// This only works if the base layout for all classes are the same
		View rowView = convertView;
		if (rowView == null) {

			
			rowView = mInflater.inflate(R.layout.event_list_item, null);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.place = (TextView) rowView.findViewById(R.id.txtPlace);
			viewHolder.time = (TextView) rowView.findViewById(R.id.txtTime);
			viewHolder.name = (TextView) rowView.findViewById(R.id.txtName);
			viewHolder.description = (TextView) rowView.findViewById(R.id.txtDescription);
			viewHolder.eventLay = (LinearLayout) rowView.findViewById(R.id.ev_list_layout);
			viewHolder.star = (ImageView) rowView.findViewById(R.id.star_evt_list);
			rowView.setTag(viewHolder);

		}

		final ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.name.setText(event.name);
		holder.description.setText(getDescription(event.description));
		holder.place.setText(event.room);
		holder.time.setText(event.getTime());
		switch (getItemViewType(position)) {
		case TYPE_ITEM:
			holder.eventLay.setBackgroundDrawable(evenBackground);
			break;
		case TYPE_ITEM_ODD:
			holder.eventLay.setBackgroundDrawable(oddBackground);
			break;
		}
		//update the star according to the state of the event (stared or not)
		if(StaredEventsService.instance.isStared(event.id)) {
			holder.star.setBackgroundDrawable(star);
		}else {
			holder.star.setBackgroundDrawable(emptyStar);
		}
		//add the listener
		holder.star.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				changeEventStartState(holder,event.id);
			}
		});
		return rowView;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		return (position % 2 == 0) ? TYPE_ITEM : TYPE_ITEM_ODD;
	}
	
	/**
	 * Return the description truncate at MAX_DESC_LENGHT 
	 * @param description the string to cut
	 * @return the description truncate at MAX_DESC_LENGHT 
	 */
	private String getDescription(String description) {
		if(description.length()<MAX_DESC_LENGHT) {
			return description;
		}else {
			return description.substring(0,MAX_DESC_LENGHT-1)+"...";
		}
	}
	
	/**
	 * Change the state of the event from stared to unstared or from unstared to stared
	 * @param holder the holder that holds the view
	 * @param eventId the event id of the event
	 */
	private void changeEventStartState(ViewHolder holder,int eventId) {
		StaredEventsService service= StaredEventsService.instance;
		if(service.isStared(eventId)) {
			//first change the stared status of the event
			service.staredEventsStatusChanged(eventId, false);
			//then update the gui
			holder.star.setBackgroundDrawable(emptyStar);
		}else {
			//first change the stared status of the event
			service.staredEventsStatusChanged(eventId, true);
			//then update the gui
			holder.star.setBackgroundDrawable(star);
		}
	}
}
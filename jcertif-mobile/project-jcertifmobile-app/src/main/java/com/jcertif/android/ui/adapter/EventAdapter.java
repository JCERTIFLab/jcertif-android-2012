package com.jcertif.android.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.ui.view.R;

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

	public EventAdapter(Activity context, List<Event> events) {
		super(context, R.layout.event_list_item, events);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		evenBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item);
		oddBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item_odd);
}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public TextView place = null;
		public TextView time = null;
		public TextView name = null;
		public TextView description = null;
		public LinearLayout eventLay=null;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ViewHolder will buffer the assess to the individual fields of the row
		// layout
		Event event = getItem(position);

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
			rowView.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
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
}
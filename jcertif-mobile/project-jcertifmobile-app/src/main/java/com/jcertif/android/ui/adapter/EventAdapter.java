package com.jcertif.android.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jcertif.android.transverse.model.Event;
import com.jcertif.android.ui.view.R;

/**
 * from http://www.vogella.de/articles/AndroidListView/article.html
 * 
 * @author Yakhya DABO
 * 
 */
public class EventAdapter extends ArrayAdapter<Event> {
	private Activity context;

	public EventAdapter(Activity context, List<Event> events) {
		super(context, R.layout.event_list_item, events);
		this.context = context;
	}

	// static to save the reference to the outer class and to avoid access to
	// any members of the containing class
	static class ViewHolder {
		public TextView place = null;
		public TextView time = null;
		public TextView name = null;
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

			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.event_list_item, null);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.place = (TextView) rowView.findViewById(R.id.txtPlace);
			viewHolder.time = (TextView) rowView.findViewById(R.id.txtTime);
			viewHolder.name = (TextView) rowView.findViewById(R.id.txtName);

			rowView.setTag(viewHolder);

		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.name.setText(event.name);
		holder.place.setText(event.room);
		holder.time.setText(event.getTime());

		return rowView;
	}

}
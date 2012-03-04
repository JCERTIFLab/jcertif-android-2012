package com.jcertif.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcertif.android.model.Speaker;
import com.jcertif.android.view.R;

/**
 * This adaptateur is responsible of how to display items in speaker's list
 * @author mouhamed_diouf
 *
 */
public class SpeakerAdapter extends ArrayAdapter<Speaker> {
	private int resource;
	private LayoutInflater mInflater;

	public SpeakerAdapter(Context context, int resource,
			List<Speaker> patients, LayoutInflater mInflater) {
		super(context, resource, patients);
		this.resource = resource;
		this.mInflater = mInflater;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		TextView title = null;
		TextView detail = null;
		ImageView i11 = null;
		Speaker speaker = getItem(position);
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.speakerslist, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		title = holder.getTitle();
		title.setText(speaker.firstName + " " + speaker.lastName + "("
				+ speaker.company + ")");
		detail = holder.getDetail();
		detail.setText(speaker.bio.substring(0, 100) + " ...");
		i11 = holder.getImage();
		Bitmap speakerBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
				+ speaker.urlPhoto);
		i11.setImageBitmap(speakerBitmap);
		return convertView;

	}

	private class ViewHolder {
		private View mRow;
		private TextView title = null;
		private TextView detail = null;
		private ImageView i11 = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getTitle() {
			if (null == title) {
				title = (TextView) mRow.findViewById(R.id.title);
			}
			return title;
		}

		public TextView getDetail() {
			if (null == detail) {
				detail = (TextView) mRow.findViewById(R.id.detail);
			}
			return detail;
		}

		public ImageView getImage() {
			if (null == i11) {
				i11 = (ImageView) mRow.findViewById(R.id.img);
			}
			return i11;
		}
	}
}

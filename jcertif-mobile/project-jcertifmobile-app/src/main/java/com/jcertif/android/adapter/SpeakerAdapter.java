package com.jcertif.android.adapter;

import java.util.List;

import android.app.Activity;
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

	private Activity context;
	
	public SpeakerAdapter(Activity context, List<Speaker> speakers) {
		super(context, R.layout.speaker_list_item, speakers);
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		TextView title = null;
		TextView detail = null;
		ImageView i11 = null;
		Speaker speaker = getItem(position);
		if (null == convertView) {
			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.speaker_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		title = holder.getTitle();
		title.setText(speaker.firstName + " " + speaker.lastName + "("
				+ speaker.company + ")");
		detail = holder.getDetail();
		detail.setText(speaker.bio);
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

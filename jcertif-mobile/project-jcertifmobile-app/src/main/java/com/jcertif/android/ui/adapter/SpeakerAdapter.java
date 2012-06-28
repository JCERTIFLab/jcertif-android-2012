package com.jcertif.android.ui.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.view.R;

/**
 * This adaptateur is responsible of how to display items in speaker's list
 * 
 * @author mouhamed_diouf
 * @author Mathias Seguy
 */
public class SpeakerAdapter extends ArrayAdapter<Speaker> {

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
	GradientDrawable evenBackground=null;
	/**
	 * The background for odd rows
	 */
	GradientDrawable oddBackground=null;
	public SpeakerAdapter(Activity context, List<Speaker> speakers) {
		super(context, R.layout.speaker_list_item, speakers);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		evenBackground=(GradientDrawable) context.getResources().getDrawable(R.drawable.list_item_odd);
		oddBackground=(GradientDrawable) context.getResources().getDrawable(R.drawable.list_item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary calls
		// to findViewById() on each row.
		ViewHolder holder = null;
		TextView title = null;
		TextView detail = null;
		ImageView i11 = null;
		LinearLayout speakLay = null;
		Speaker speaker = getItem(position);
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.speaker_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		title = holder.getTitle();
		title.setText(speaker.firstName + " " + speaker.lastName + "(" + speaker.company + ")");
		detail = holder.getDetail();
		detail.setText(speaker.bio);
		i11 = holder.getImage();
		//load the picture
		File filesDir = JCApplication.getInstance().getExternalFilesDir(null);
		String pictureFolderName=getContext().getString(R.string.folder_name_spekaer_picture);
		File pictureDir=new File(filesDir,pictureFolderName);
		File filePicture=new File(pictureDir,speaker.urlPhoto);
		Bitmap speakerBitmap = BitmapFactory.decodeFile(filePicture.getAbsolutePath());
		i11.setImageBitmap(speakerBitmap);
		speakLay=holder.getSpeakerLay();
		switch (getItemViewType(position)) {
		case TYPE_ITEM:
			speakLay.setBackgroundDrawable(evenBackground);
			break;
		case TYPE_ITEM_ODD:
			speakLay.setBackgroundDrawable(oddBackground);
			break;
		}
		
		return convertView;

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
	 * @author mouhamed_diouf
	 * @goals
	 *        ViewHolder keeps references to children views to avoid unneccessary calls
	 *        to findViewById() on each row.
	 */
	private class ViewHolder {
		private View mRow;
		private TextView title = null;
		private TextView detail = null;
		private ImageView i11 = null;
		private LinearLayout speakerLay;

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

		/**
		 * @return the speakerLay
		 */
		public final LinearLayout getSpeakerLay() {
			if (null == speakerLay) {
				speakerLay = (LinearLayout) mRow.findViewById(R.id.sp_list_layout);
			}
			return speakerLay;
		}
	}
}

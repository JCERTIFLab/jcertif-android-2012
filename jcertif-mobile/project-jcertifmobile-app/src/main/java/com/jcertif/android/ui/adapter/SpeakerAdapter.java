package com.jcertif.android.ui.adapter;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcertif.android.JCApplication;
import com.jcertif.android.R;
import com.jcertif.android.service.business.stardevents.StaredEventsService;
import com.jcertif.android.transverse.model.Speaker;
import com.jcertif.android.ui.adapter.EventAdapter.ViewHolder;

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
	GradientDrawable evenBackground = null;
	/**
	 * The background for odd rows
	 */
	GradientDrawable oddBackground = null;
	/**
	 * The star
	 */
	Drawable star = null;
	/**
	 * The empty star
	 */
	Drawable emptyStar = null;

	/******************************************************************************************/
	/** Constructors **************************************************************************/
	/******************************************************************************************/

	public SpeakerAdapter(Activity context, List<Speaker> speakers) {
		super(context, R.layout.speaker_list_item, speakers);
		// Cache the LayoutInflate to avoid asking for a new one each time.
		mInflater = LayoutInflater.from(context);
		evenBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item_odd);
		oddBackground = (GradientDrawable) context.getResources().getDrawable(R.drawable.list_item);
		star = (Drawable) context.getResources().getDrawable(R.drawable.star);
		emptyStar = (Drawable) context.getResources().getDrawable(R.drawable.empty_star);
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
		TextView title = null;
		TextView detail = null;
		ImageView i11 = null;
		LinearLayout speakLay = null;
		final Speaker speaker = getItem(position);
		View rowView = convertView;
		if (null == rowView) {
			rowView = mInflater.inflate(R.layout.speaker_list_item, null);
			ViewHolder viewHolder = new ViewHolder(rowView);
			viewHolder.star = (ImageView) rowView.findViewById(R.id.star_spe_list);
			rowView.setTag(viewHolder);
		}
		final ViewHolder holder = (ViewHolder) rowView.getTag();

		title = holder.getTitle();
		title.setText(speaker.firstName + " " + speaker.lastName + "(" + speaker.company + ")");
		detail = holder.getDetail();
		detail.setText(speaker.bio);
		i11 = holder.getImage();
		// load the picture
		File filesDir = JCApplication.getInstance().getExternalFilesDir(null);
		String pictureFolderName = getContext().getString(R.string.folder_name_spekaer_picture);
		File pictureDir = new File(filesDir, pictureFolderName);
		File filePicture = new File(pictureDir, speaker.urlPhoto);
		Bitmap speakerBitmap = BitmapFactory.decodeFile(filePicture.getAbsolutePath());
		i11.setImageBitmap(speakerBitmap);
		speakLay = holder.getSpeakerLay();
		switch (getItemViewType(position)) {
		case TYPE_ITEM:
			speakLay.setBackgroundDrawable(evenBackground);
			break;
		case TYPE_ITEM_ODD:
			speakLay.setBackgroundDrawable(oddBackground);
			break;
		}
		// update the star according to the state of the event (stared or not)
		if (StaredEventsService.instance.isStaredSpeaker(speaker.id)) {
			holder.star.setBackgroundDrawable(star);
		} else {
			holder.star.setBackgroundDrawable(emptyStar);
		}
		// add the listener
		holder.star.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeSpeakerStartState(holder, speaker.id);
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
	 * Change the state of the event from stared to unstared or from unstared to stared
	 * @param imageView the holder that holds the view
	 * @param eventId the event id of the event
	 */
	private void changeSpeakerStartState(ViewHolder holder,int speakerId) {
		StaredEventsService service= StaredEventsService.instance;
		if(service.isStaredSpeaker(speakerId)) {
			//first change the stared status of the event
			service.removeStaredSpeaker(speakerId);
			//then update the gui
			holder.star.setBackgroundDrawable(emptyStar);
		}else {
			//first change the stared status of the event
			service.addStaredSpeaker(speakerId);
			//then update the gui
			holder.star.setBackgroundDrawable(star);
		}
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
		public ImageView star = null;

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

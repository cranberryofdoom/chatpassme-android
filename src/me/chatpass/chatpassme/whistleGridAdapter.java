package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WhistleGridAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mQuesTxt;
	private int[] mHitCount;
	private ArrayList<Bitmap> mQuesImg;
	private ArrayList<Bitmap> mUserImg;

	// Constructor
	public WhistleGridAdapter(Context c, String[] quesTxt, int[] hitCount,
			ArrayList<Bitmap> quesImg, ArrayList<Bitmap> userImg) {
		mContext = c;
		mQuesTxt = quesTxt;
		mHitCount = hitCount;
		mQuesImg = quesImg;
		mUserImg = userImg;
	}

	@Override
	public int getCount() {
		return mQuesTxt.length;
	}

	@Override
	public Object getItem(int position) {
		return mQuesTxt[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View gridView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			// Create a new View
			gridView = new View(mContext);

			// Get layout from layout_whistle.xml
			gridView = inflater.inflate(R.layout.layout_whistle, null);

		} else {
			gridView = (View) convertView;
		}

		// Push all question texts into the layout_whistle_question
		TextView layoutWhistleQuestion = (TextView) gridView
				.findViewById(R.id.layout_whistle_question);
		layoutWhistleQuestion.setText(mQuesTxt[position]);

		// Push all clik counts into the layout_whistle_clik_count
		TextView layoutWhistleClikCount = (TextView) gridView
				.findViewById(R.id.layout_whistle_clik_count);
		layoutWhistleClikCount.setText(String.valueOf(mHitCount[position]));

		// Push all the whistle images into the layout_whistle_image
		ImageView layoutWhistleImage = (ImageView) gridView
				.findViewById(R.id.layout_whistle_image);
		layoutWhistleImage.setImageBitmap(mQuesImg.get(position));

		// Push all the whistle images into the layout_whistle_image
		ImageView layoutUserImage = (ImageView) gridView
				.findViewById(R.id.layout_user_image);
		layoutUserImage.setImageBitmap(mUserImg.get(position));

		return gridView;
	}

}
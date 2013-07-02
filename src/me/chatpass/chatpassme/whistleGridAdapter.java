package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WhistleGridAdapter extends BaseAdapter {
	private Context mContext;
	private String[] mQuesTxt;
	private int[] mHitCount;
	private ArrayList<Bitmap> mQuesImg;

	// Constructor
	public WhistleGridAdapter(Context c, String[] quesTxt, int[] hitCount) {
		mContext = c;
		mQuesTxt = quesTxt;
		mHitCount = hitCount;
		// mQuesImg = quesImg;
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

		return gridView;
	}

}
package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommentGridAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> mComment;
	private ArrayList<String> mUserFirstName;
	private ArrayList<String> mUserLastName;
	private ArrayList<Bitmap> mUserImg;

	// Constructor
	public CommentGridAdapter(Context c, ArrayList<String> comment,
			ArrayList<String> userFirstName, ArrayList<String> userLastName,
			ArrayList<Bitmap> userImg) {
		mContext = c;
		mComment = comment;
		mUserFirstName = userFirstName;
		mUserLastName = userLastName;
		mUserImg = userImg;
	}

	@Override
	public int getCount() {
		return mComment.size();
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View gridView;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView = new View(mContext);
			gridView = inflater.inflate(R.layout.layout_comment, null);
		} else {
			gridView = (View) convertView;
		}
		
		// Push all question texts into the layout_comment_name
		TextView layoutUserName = (TextView) gridView
				.findViewById(R.id.layout_comment_name);
		layoutUserName.setText(mUserFirstName.get(position) + " " + mUserLastName.get(position));
		
		// Push all comment texts into the layout_comment_comment
		TextView layoutUserComment = (TextView) gridView
				.findViewById(R.id.layout_comment_comment);
		layoutUserComment.setText(mComment.get(position));
		
		// Push all the user images into the layout_comment_user_image
		ImageButton layoutUserImage = (ImageButton) gridView
				.findViewById(R.id.layout_comment_user_image);
		layoutUserImage.setImageBitmap(mUserImg.get(position));

		return gridView;
	}

}

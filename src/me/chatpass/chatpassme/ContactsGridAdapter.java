package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactsGridAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> mUserFirstName;
	private ArrayList<Bitmap> mUserImg;
	private int mDimensions;
	
	public ContactsGridAdapter(Context c,
			ArrayList<String> userfirstName, ArrayList<Bitmap> userImg, int dimensions) {
		mContext = c;
		mUserFirstName = userfirstName;
		mUserImg = userImg;
		mDimensions = dimensions;
	}

	@Override
	public int getCount() {
		return mUserFirstName.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
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
			gridView = inflater.inflate(R.layout.layout_contact, null);

		} else {
			gridView = (View) convertView;
		}
		
		TextView layoutUserFirstName = (TextView) gridView
				.findViewById(R.id.layout_contact_user_name);
		layoutUserFirstName.setText(mUserFirstName.get(position));
		
		ImageView layoutUserImage = (ImageView) gridView
				.findViewById(R.id.layout_contact_user_image);
		layoutUserImage.setLayoutParams(new RelativeLayout.LayoutParams(
				mDimensions, mDimensions));
		layoutUserImage.setImageBitmap(mUserImg.get(position));
		return gridView;
	}

}

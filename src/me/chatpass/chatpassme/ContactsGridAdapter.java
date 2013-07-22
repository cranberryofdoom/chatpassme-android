package me.chatpass.chatpassme;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class ContactsGridAdapter extends BaseAdapter {
	private Context mContext;
	private List<ParseObject> mObjects;

	DecodeSampledBitmap decode = new DecodeSampledBitmap();
	private GetPlaceholder mPlaceholder;

	public ContactsGridAdapter(Context c, List<ParseObject> objects) {
		mContext = c;
		mObjects = objects;
		mPlaceholder = new GetPlaceholder((Activity) c);
	}

	public static class ViewHolder {
		public TextView userFirstName;
		public ImageView userImage;
	}

	@Override
	public int getCount() {
		return mObjects.size();
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
		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			gridView = new View(mContext);
			gridView = inflater.inflate(R.layout.layout_contact, null);
			holder = new ViewHolder();
			holder.userFirstName = (TextView) gridView
					.findViewById(R.id.layout_contact_user_name);
			holder.userImage = (ImageView) gridView
					.findViewById(R.id.layout_contact_user_image);
			gridView.setTag(holder);

		} else {
			gridView = (View) convertView;
			holder = (ViewHolder) gridView.getTag();
		}

		holder.userFirstName.setText(mObjects.get(position).getString(
				"firstName"));

		ParseFile pUserImage = mObjects.get(position).getParseFile("imageFile");
		if (pUserImage == null) {
			holder.userImage.setImageBitmap(mPlaceholder.getBitmap());
		} else {
			pUserImage.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException e) {
					DisplayMetrics metrics = new DisplayMetrics();
					((Activity) mContext).getWindowManager()
							.getDefaultDisplay().getMetrics(metrics);
					int dimensions = metrics.widthPixels / 4;
					Bitmap source = decode.decodeSampledBitmap(data,
							100, 100);
					holder.userImage.setImageBitmap(source);
					holder.userImage.setLayoutParams(new RelativeLayout.LayoutParams(
							dimensions, dimensions));
				}
			});
		}
		return gridView;
	}
}

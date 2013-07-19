package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class WhistleGridAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> mQuesTxt;
	private ArrayList<Integer> mHitCount;
	private ArrayList<Bitmap> mQuesImg;
	private ArrayList<Bitmap> mUserImg;
	private ArrayList<ParseFile> pQuesImg;
	private ArrayList<ParseFile> pUserImg;
	private ParseQuery<ParseObject> mQuery;
	private List<ParseObject> mObjects;
	private Bitmap bitmapPlaceholder;

	DecodeSampledBitmap decode = new DecodeSampledBitmap();

	public WhistleGridAdapter(Context c, List<ParseObject> objects) {
		mContext = c;
		mObjects = objects;

		// Get the placeholder image
		Resources res = c.getResources();
		Drawable drawable = res.getDrawable(R.drawable.whistle_placeholder);
		bitmapPlaceholder = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmapPlaceholder.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	}

	public static class ViewHolder {
		public TextView whistleQuestion;
		public TextView whistleClikCount;
		public ImageView whistleImage;
		public ImageView userImage;
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
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
			gridView = inflater.inflate(R.layout.layout_whistle, null);
			holder = new ViewHolder();
			holder.whistleQuestion = (TextView) gridView
					.findViewById(R.id.layout_whistle_question);
			holder.whistleClikCount = (TextView) gridView
					.findViewById(R.id.layout_whistle_clik_count);
			holder.whistleImage = (ImageView) gridView
					.findViewById(R.id.layout_whistle_image);
			holder.userImage = (ImageView) gridView
					.findViewById(R.id.layout_user_image);
			gridView.setTag(holder);

		} else {
			gridView = (View) convertView;
			holder = (ViewHolder) gridView.getTag();
		}

		// Push all question texts
		holder.whistleQuestion.setText(mObjects.get(position).getString(
				"quesTxt"));

		// Push all clik counts
		holder.whistleClikCount.setText(mObjects.get(position)
				.getNumber("hitCount").toString());

		// Push all whistle images
		ParseFile pWhistleImage = mObjects.get(position)
				.getParseFile("quesImg");
		if (pWhistleImage == null) {
			holder.whistleImage.setImageBitmap(bitmapPlaceholder);
		} else {
			pWhistleImage.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException e) {
					holder.whistleImage.setImageBitmap(decode
							.decodeSampledBitmap(data, 200, 100));
				}

			});
		}

		// Push all user images
		Number userId = mObjects.get(position).getNumber("userId");
		ParseQuery<ParseObject> qUser = ParseQuery.getQuery("Users");
		qUser.whereEqualTo("userId", userId);
		qUser.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					Log.i("WHAT THE FUCK MAN",
							"I GOT SOMETHING!" + object.toString());
					ParseFile pUserImage = object.getParseFile("imageFile");
					if (pUserImage == null) {
						holder.userImage.setImageBitmap(bitmapPlaceholder);
					} else {
						try {
							holder.userImage.setImageBitmap(decode
									.decodeSampledBitmap(pUserImage.getData(),
											50, 50));
						} catch (ParseException e1) {
							holder.userImage.setImageBitmap(bitmapPlaceholder);
						}
					}
				} else {
					holder.userImage.setImageBitmap(bitmapPlaceholder);
				}
			}
		});
		return gridView;
	}
}
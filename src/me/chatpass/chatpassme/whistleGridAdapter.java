package me.chatpass.chatpassme;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
	private List<ParseObject> mObjects;

	DecodeSampledBitmap decode = new DecodeSampledBitmap();
	private GetPlaceholder mPlaceholder;

	public WhistleGridAdapter(Context c, List<ParseObject> objects) {
		mContext = c;
		mObjects = objects;
		mPlaceholder = new GetPlaceholder((Activity) c);
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

		holder.whistleQuestion.setText(mObjects.get(position).getString(
				"quesTxt"));

		holder.whistleClikCount.setText(mObjects.get(position)
				.getNumber("hitCount").toString());

		ParseFile pWhistleImage = mObjects.get(position)
				.getParseFile("quesImg");
		if (pWhistleImage == null) {
			holder.whistleImage.setImageBitmap(mPlaceholder.getBitmap());
		} else {
			pWhistleImage.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException e) {
					Bitmap cropped;
					Bitmap source = decode.decodeSampledBitmap(data, 200, 100);
					if (source.getWidth() >= source.getHeight()) {
						cropped = Bitmap.createBitmap(source, source.getWidth()
								/ 2 - source.getHeight() / 2, 0,
								source.getHeight(), source.getHeight());
					} else {
						cropped = Bitmap.createBitmap(source, 0,
								source.getHeight() / 2 - source.getWidth() / 2,
								source.getWidth(), source.getWidth());
					}
					Bitmap scaled = Bitmap.createScaledBitmap(cropped, 200,
							200, true);
					holder.whistleImage.setImageBitmap(scaled);
				}
			});
		}

		Number userId = mObjects.get(position).getNumber("userId");
		ParseQuery<ParseObject> qUser = ParseQuery.getQuery("Users");
		qUser.whereEqualTo("userId", userId);
		qUser.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					ParseFile pUserImage = object.getParseFile("imageFile");
					if (pUserImage == null) {
						holder.userImage.setImageBitmap(mPlaceholder
								.getBitmap());
					} else {
						try {
							byte[] data = pUserImage.getData();
							Bitmap cropped;
							Bitmap source = decode.decodeSampledBitmap(data,
									100, 100);
							if (source.getWidth() >= source.getHeight()) {
								cropped = Bitmap.createBitmap(
										source,
										source.getWidth() / 2
												- source.getHeight() / 2, 0,
										source.getHeight(), source.getHeight());
							} else {
								cropped = Bitmap.createBitmap(
										source,
										0,
										source.getHeight() / 2
												- source.getWidth() / 2,
										source.getWidth(), source.getWidth());
							}
							Bitmap scaled = Bitmap.createScaledBitmap(cropped,
									50, 50, true);
							holder.userImage.setImageBitmap(scaled);
						} catch (ParseException e1) {
							holder.userImage.setImageBitmap(mPlaceholder
									.getBitmap());
						}
					}
				} else {
					holder.userImage.setImageBitmap(mPlaceholder.getBitmap());
				}
			}
		});
		return gridView;
	}
}
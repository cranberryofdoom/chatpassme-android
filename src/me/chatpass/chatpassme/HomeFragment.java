package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeFragment extends Fragment {

	private GridView gridView;
	private WhistleGridAdapter gridAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		// Identify which grid view we're going to load data into
		gridView = (GridView) view.findViewById(R.id.home_grid_view);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the activity that this fragment is called from
		final Activity activity = getActivity();

		// If there is an activity linked to this fragment
		if (activity != null) {

			// Create a ParseObject query and ask for the VoteQues class
			ParseQuery<ParseObject> query = ParseQuery.getQuery("VoteQues");

			// Tell Parse to find it
			query.findInBackground(new FindCallback<ParseObject>() {

				// When Parse has found the objects it put them in a list
				public void done(List<ParseObject> objects, ParseException e) {

					// If there is no exception
					if (e == null) {

						// Initialize String array of question texts
						String[] quesTxt = new String[objects.size()];

						// Initialize int array of number of cliks
						int[] hitCount = new int[objects.size()];

						// Initialize String array of question image urls
						final ArrayList<Bitmap> quesImg = new ArrayList<Bitmap>();

						final ArrayList<Bitmap> userImg = new ArrayList<Bitmap>();

						// Retrieve data from Parse and push all data into
						// respective arrays
						for (int i = 0; i < objects.size(); i++) {
							// Log.e("NUMBER", "" + i);

							// Get whistle question string from ParseObject
							quesTxt[i] = objects.get(i).getString("quesTxt");

							// Get number of cliks from ParseObject
							hitCount[i] = objects.get(i).getInt("hitCount");

							// Get question image
							ParseFile pQuesImg = objects.get(i).getParseFile(
									"quesImg");

							if (pQuesImg != null) {
								try {
									//Log.w("WHISTLE IMAGE", "MY ANUS IS BLEEDING");
									byte[] data = pQuesImg.getData();
									quesImg.add(decodeSampledBitmap(data, 200,
											100));
								} catch (ParseException e1) {
								}
							} else {
								quesImg.add(null);
							}

							// Get the userId from the ParseObject
							Number userId = objects.get(i).getNumber("userId");

							// Ask for the Users class
							ParseQuery<ParseObject> user = ParseQuery
									.getQuery("Users");

							// Get the that specific userId
							user.whereEqualTo("userId", userId);
							try {
								ParseObject pUser = user.getFirst();
								ParseFile pUserImg = pUser
										.getParseFile("imageFile");
								//Log.w("USER IMAGE", "I AM A BANANA");
								byte[] data = pUserImg.getData();
								userImg.add(decodeSampledBitmap(data, 50, 50));
							} catch (ParseException e1) {
							}
						}

						// Create a new adapter
						gridAdapter = new WhistleGridAdapter(activity, quesTxt,
								hitCount, quesImg, userImg);

						// Set the adapter
						gridView.setAdapter(gridAdapter);
					}
				}
			});
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;

		//Log.i("WHEE", "height ma bob " + height);
		//Log.i("WHEE", "width ma bob " + width);
		int inSampleSize = 64;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image with both dimensions larger than or equal
			// to the requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			//Log.i("WHEE", "sample size ma bob " + inSampleSize);
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmap(byte[] data, int reqWidth,
			int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
}

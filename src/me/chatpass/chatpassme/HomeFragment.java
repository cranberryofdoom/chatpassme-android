package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeFragment extends Fragment {

	private GridView gridView;
	private WhistleGridAdapter gridAdapter;
	private ArrayList<String> objectId = new ArrayList<String>();
	private ArrayList<String> quesTxt = new ArrayList<String>();
	private ArrayList<Integer> hitCount = new ArrayList<Integer>();
	private ArrayList<Bitmap> quesImg = new ArrayList<Bitmap>();
	private ArrayList<byte[]> dataQuesImg = new ArrayList<byte[]>();
	private ArrayList<Bitmap> userImg = new ArrayList<Bitmap>();
	private ArrayList<byte[]> dataUserImg = new ArrayList<byte[]>();

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
		
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.whistle_placeholder);
		final Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		final byte[] bitMapData = stream.toByteArray();
		

		// If there is an activity linked to this fragment
		if (activity != null) {

			// Create a ParseObject query and ask for the VoteQues class
			ParseQuery<ParseObject> query = ParseQuery.getQuery("VoteQues");

			query.setLimit(5);
			query.addAscendingOrder("createdAt");
			// Tell Parse to find it
			query.findInBackground(new FindCallback<ParseObject>() {

				// When Parse has found the objects it put them in a list
				public void done(List<ParseObject> objects, ParseException e) {

					// If there is no exception
					if (e == null) {

						// Retrieve data from Parse and push all data into
						// respective arrays
						for (int i = 0; i < objects.size(); i++) {
							// Log.e("NUMBER", "" + i);
							
							//Log.i("WTF", objects.get(i).getObjectId());

							objectId.add(i, objects.get(i).getObjectId());

							// Get whistle question string from ParseObject
							quesTxt.add(i, objects.get(i).getString("quesTxt"));

							// Get number of cliks from ParseObject
							hitCount.add(i, objects.get(i).getInt("hitCount"));

							// Get question image
							ParseFile pQuesImg = objects.get(i).getParseFile(
									"quesImg");

							if (pQuesImg != null) {
								try {
									// Log.w("WHISTLE IMAGE",
									// "MY SPOON IS TOO BIG");
									byte[] data = pQuesImg.getData();
									dataQuesImg.add(data);
									quesImg.add(decodeSampledBitmap(data, 200,
											100));
								} catch (ParseException e1) {
									dataQuesImg.add(bitMapData);
									quesImg.add(bitmap);
								}
							} else {
								dataQuesImg.add(bitMapData);
								quesImg.add(bitmap);
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
								// Log.w("USER IMAGE", "I AM A BANANA");
								byte[] data = pUserImg.getData();
								dataUserImg.add(data);
								userImg.add(decodeSampledBitmap(data, 50, 50));
							} catch (ParseException e1) {
							}
						}

						// Create a new adapter
						gridAdapter = new WhistleGridAdapter(activity, quesTxt,
								hitCount, quesImg, userImg);

						// Set the adapter
						gridView.setAdapter(gridAdapter);

						// Show the whistle when one of the grid items are
						// clicked on
						gridView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent,
									View v, int position, long id) {
								Intent intent = new Intent(activity,
										ViewWhistleActivity.class);
								intent.putExtra("iObjectId",
										objectId.get(position));
								intent.putExtra("iQuesTxt",
										quesTxt.get(position));
								intent.putExtra("iHitCount",
										hitCount.get(position));
								intent.putExtra("iUserImg",
										dataUserImg.get(position));
								intent.putExtra("iQuesImg",
										dataQuesImg.get(position));
								startActivity(intent);
							}
						});
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

		// Log.i("WHEE", "height ma bob " + height);
		// Log.i("WHEE", "width ma bob " + width);
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
			// Log.i("WHEE", "sample size ma bob " + inSampleSize);
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

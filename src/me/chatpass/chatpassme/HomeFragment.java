package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeFragment extends Fragment {

	private GridView gridView;

	private ArrayList<String> objectId = new ArrayList<String>();
	private ArrayList<String> quesTxt = new ArrayList<String>();
	private ArrayList<Integer> hitCount = new ArrayList<Integer>();
	private ArrayList<Bitmap> quesImg = new ArrayList<Bitmap>();
	private ArrayList<byte[]> dataQuesImg = new ArrayList<byte[]>();
	private ArrayList<Bitmap> userImg = new ArrayList<Bitmap>();
	private ArrayList<byte[]> dataUserImg = new ArrayList<byte[]>();
	private List<ParseObject> mObjects;
	private Bitmap bitmapPlaceholder;


	DecodeSampledBitmap decode = new DecodeSampledBitmap();

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

		if (activity != null) {
			// Get all questions that are directed at everyone
			ParseQuery<ParseObject> qEveryone = ParseQuery.getQuery("VoteQues");
			qEveryone.whereEqualTo("target", "EVRY");

			// Get user's school Id
			ParseQuery<ParseObject> qUserSchool = ParseQuery
					.getQuery("UserSchool");
			Number userId = 257;
			Number schoolId = 0;
			qUserSchool.whereEqualTo("userId", userId);
			try {
				schoolId = qUserSchool.getFirst().getNumber("schoolId");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			// Get all questions that are targeted at the user's school
			ParseQuery<ParseObject> qSchool = ParseQuery.getQuery("VoteQues");
			qSchool.whereEqualTo("target", "SCHL");
			qSchool.whereEqualTo("schlVal", schoolId);

			List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
			queries.add(qEveryone);
			queries.add(qSchool);

			ParseQuery<ParseObject> query = ParseQuery.or(queries);
			query.addAscendingOrder("createdAt");

			query.findInBackground(new FindCallback <ParseObject>() {
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						mObjects = objects;
						
						// Create a new grid adapter and set it
						WhistleGridAdapter gridAdapter = new WhistleGridAdapter(activity,
								mObjects);
						gridView.setAdapter(gridAdapter);

						// Set grid click listener
						gridView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent, View v,
									int position, long id) {
								
								// Get the placeholder image
								Resources res = getResources();
								Drawable drawable = res.getDrawable(R.drawable.whistle_placeholder);
								bitmapPlaceholder = ((BitmapDrawable) drawable).getBitmap();
								ByteArrayOutputStream stream = new ByteArrayOutputStream();
								bitmapPlaceholder.compress(Bitmap.CompressFormat.JPEG, 100, stream);

								// Push all necessary information over to next
								// activity
								Intent intent = new Intent(activity,
										ViewWhistleActivity.class);
								Bundle b = new Bundle();
								b.putString("iObjectId", mObjects.get(position).getObjectId());
								b.putString("iQuesTxt", mObjects.get(position).getString("quesTxt"));
								b.putInt("iHitCount", mObjects.get(position).getInt("hitCount"));
								if (mObjects.get(position).getParseFile("quesImg") == null){
									
								}
								try {
									b.putByteArray("iQuesImg", mObjects.get(position).getParseFile("quesImg").getData());
								} catch (ParseException e) {
									e.printStackTrace();
								}
								startActivity(intent);
							}
						});
					}
				}
			});
		}
	}
}

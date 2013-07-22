package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class HomeFragment extends Fragment {

	private GridView gridView;

	private List<ParseObject> mObjects;

	DecodeSampledBitmap decode = new DecodeSampledBitmap();
	private GetPlaceholder placeholder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		// Identify which grid view we're going to load data into
		gridView = (GridView) view.findViewById(R.id.home_grid_view);

		placeholder = new GetPlaceholder(getActivity());

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

			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> objects, ParseException e) {
					if (e == null) {
						mObjects = objects;

						WhistleGridAdapter gridAdapter = new WhistleGridAdapter(
								activity, mObjects);
						gridView.setAdapter(gridAdapter);

						gridView.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> parent,
									View v, final int position, long id) {
								viewWhistle(mObjects
										.get(position));
							}
						});
					}
				}
			});
		}
	}
	
	private void viewWhistle(final ParseObject object) {
		final Intent intent = new Intent(getActivity(),
				ViewWhistleActivity.class);
		final Bundle b = new Bundle();

		b.putString("iObjectId", object.getObjectId());
		b.putString("iQuesTxt", object.getString("quesTxt"));
		b.putInt("iHitCount", object.getInt("hitCount"));
		b.putInt("iQuesId", object.getNumber("quesId").intValue());

		ParseFile pQuesImg = object.getParseFile("quesImg");
		if (pQuesImg != null) {
			pQuesImg.getDataInBackground(new GetDataCallback() {

				@Override
				public void done(byte[] data, ParseException e) {
					b.putByteArray("iQuesImg", data);
					Log.i("AFKDLS", data.toString());
					getUserProfileImage(intent, object, b);
				}

			});
		} else {
			b.putByteArray("iQuesImg", placeholder.getByteArray());
			Log.i("I DIDN'T GET ANYTHING", placeholder.getByteArray()
					.toString());
			getUserProfileImage(intent, object, b);
		}
	}
	
	private void getUserProfileImage(final Intent intent, ParseObject object,
			final Bundle b) {
		Number userId = object.getNumber("userId");
		ParseQuery<ParseObject> qUser = ParseQuery.getQuery("Users");
		qUser.whereEqualTo("userId", userId);
		qUser.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				ParseFile pUserImg = object.getParseFile("imageFile");
				if (pUserImg == null) {
					b.putByteArray("iUserImg", placeholder.getByteArray());
				} else {
					pUserImg.getDataInBackground(new GetDataCallback() {

						@Override
						public void done(byte[] data, ParseException e) {
							b.putByteArray("iUserImg", data);
							Log.i("WOOHOO", data.toString());
							intent.putExtras(b);
							startActivity(intent);
						}
					});
				}
			}
		});
	}
}

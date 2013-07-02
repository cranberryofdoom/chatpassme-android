package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
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

						// Initialize ParseFile array of user profile images
						ParseFile[] pQuesImg = new ParseFile[objects.size()];

						// Initialize String array of question image urls
						final ArrayList<Bitmap> quesImg = new ArrayList<Bitmap>();

						// Retrieve data from Parse and push all data into
						// respective arrays
						for (int i = 0; i < objects.size(); i++) {
							Log.i("WTF", "" + objects.get(i).toString());

							// Get whistle question string from Parse object
							quesTxt[i] = objects.get(i).getString("quesTxt");

							// Get number of cliks from Parse object
							hitCount[i] = objects.get(i).getInt("hitCount");

							// Get question image
							pQuesImg[i] = (ParseFile) objects.get(i).get(
									"quesImg");

							Log.i("WTF", "" + pQuesImg[i]);
							Log.i("WTF", "" + i);
							if (pQuesImg[i] != null) {
								try {
									byte[] data = pQuesImg[i].getData();
									quesImg.add(BitmapFactory.decodeByteArray(
											data, 0, data.length));
								} catch (ParseException e1) {
									Log.i("", "error ):");
								}
							} else {
								quesImg.add(null);
							}

						}

						// Create a new adapter
						gridAdapter = new WhistleGridAdapter(activity, quesTxt,
								hitCount);

						// Set the adapter
						gridView.setAdapter(gridAdapter);
					}
				}
			});
		}
	}
}

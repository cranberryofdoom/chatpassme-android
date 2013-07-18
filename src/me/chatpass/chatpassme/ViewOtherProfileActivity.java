package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewOtherProfileActivity extends Activity {
	private TabHost tabHost;

	private int whistleCount;
	private int clikCount;
	private int podCount;
	private Number userId;
	private ArrayList<String> whistles = new ArrayList<String>();
	private ArrayList<String> cliks = new ArrayList<String>();
	private byte[] userImg;

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_other_profile);

		Intent intent = getIntent();
		userId = (Number) intent.getIntExtra("iUserId", 0);
		userImg = intent.getByteArrayExtra("iUserImg");

		setupTabs();

		getWhistleCount();

		getClikCount();

		getPodCount();

		getProfilePicture();

		getCliksAnswered();

		getWhistlesCreated();
	}

	private void getWhistlesCreated() {
		final ListView listView = (ListView) findViewById(R.id.other_profile_whistles);

		// Create a ParseObject query and ask for the VoteQues class
		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");

		// Ask for all the times userId shows up
		qVoteQues.whereEqualTo("userId", userId);

		// Get it
		qVoteQues.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {

					// Retrieve all whistles created and push into array
					for (int i = 0; i < objects.size(); i++) {

						// Get the string for the quesTxt
						String quesTxt = objects.get(i).getString("quesTxt");

						// Add it to the whistles ArrayList
						whistles.add(quesTxt);
					}

					// Create a new adapter for whistles
					ArrayAdapter<String> whistlesAdapter = new ArrayAdapter<String>(
							ViewOtherProfileActivity.this,
							android.R.layout.simple_list_item_1,
							android.R.id.text1, whistles);

					// Set the adapter
					listView.setAdapter(whistlesAdapter);

					// Set list click listener
					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							viewWhistle(objects, position);
						}
					});
				}
			}
		});
	}

	private void viewWhistle(List<ParseObject> objects, int position) {
		Intent intent = new Intent(this, ViewWhistleActivity.class);

		intent.putExtra("iObjectId", objects.get(position).getObjectId());
		intent.putExtra("iQuesTxt", whistles.get(position));
		intent.putExtra("iHitCount", objects.get(position).getInt("hitCount"));

		Number userId = objects.get(position).getNumber("userId");
		ParseQuery<ParseObject> qUser = ParseQuery.getQuery("Users");
		qUser.whereEqualTo("userId", userId);
		try {
			ParseFile pUserImg = qUser.getFirst().getParseFile("imageFile");
			byte[] dUserImg = pUserImg.getData();
			intent.putExtra("iUserImg", dUserImg);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		ParseFile pQuesImg = objects.get(position).getParseFile("quesImg");
		try {
			byte[] dQuesImg = pQuesImg.getData();
			intent.putExtra("iQuesImg", dQuesImg);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		startActivity(intent);
	}

	private void getCliksAnswered() {

		final ListView listView = (ListView) findViewById(R.id.other_profile_cliks);

		// Create a ParseObject query and ask for the VoteAnswer class
		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");

		// Ask for all the times userId shows up
		qVoteAnswer.whereEqualTo("userId", userId);

		// Get it
		qVoteAnswer.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {

					// Retrieve answered clik questions and push into the array
					for (int i = 0; i < objects.size(); i++) {

						// Get the quesId for the the clik
						Number quesId = objects.get(i).getNumber("quesId");

						// Create a new ParseObject to get the clik question
						ParseQuery<ParseObject> qVoteQues = ParseQuery
								.getQuery("VoteQues");

						// Ask for all the times quesId shows up
						qVoteQues.whereEqualTo("quesId", quesId);
						try {

							// Get the string for the quesTxt
							String quesTxt = qVoteQues.getFirst().getString(
									"quesTxt");

							// Add it to the cliks ArrayList
							cliks.add(quesTxt);

						} catch (ParseException e1) {
						}
					}

					// Create a new adapter for cliks
					ArrayAdapter<String> cliksAdapter = new ArrayAdapter<String>(
							ViewOtherProfileActivity.this,
							android.R.layout.simple_list_item_1,
							android.R.id.text1, cliks);

					// Set the adapter
					listView.setAdapter(cliksAdapter);

					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							viewWhistle(objects, position);
						}
					});
				}
			}
		});
	}

	private void getProfilePicture() {
		ImageButton image = (ImageButton) findViewById(R.id.profile_picture);
		image.setImageBitmap(decode.decodeSampledBitmap(userImg, 50, 50));
	}

	private void getPodCount() {
		// Just set the default podCount to 0
		podCount = 0;
		TextView profilePodCount = (TextView) findViewById(R.id.other_profile_pod_count);
		profilePodCount.setText("" + podCount + " pods");
	}

	private void getClikCount() {
		// Create a ParseObject query and ask for the VoteAnswer class
		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");

		// Ask for all the times userId shows up
		qVoteAnswer.whereEqualTo("userId", userId);

		// Count how many times userId has cliked
		qVoteAnswer.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {

					// Record the clik number
					clikCount = count;

					// Push the clik number in the TextView
					TextView profileClikCount = (TextView) findViewById(R.id.other_profile_clik_count);

					// Make sure that we get the grammar right
					if (clikCount == 1) {
						profileClikCount.setText("" + clikCount + " clik");
					} else {
						profileClikCount.setText("" + clikCount + " cliks");
					}
				} else {
				}
			}
		});
	}

	private void getWhistleCount() {
		// Create a ParseObject query and ask for the VoteQues
		// class
		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");

		// Ask for all the times userId shows up
		qVoteQues.whereEqualTo("userId", userId);

		// Count how many times userId has created a whistle
		qVoteQues.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {

					// Record the whistle number
					whistleCount = count;

					// Push the whistle number in the TextView
					TextView profileWhistleCount = (TextView) findViewById(R.id.other_profile_whistle_count);

					// Make sure that we get the grammar right
					if (whistleCount == 1) {
						profileWhistleCount.setText("" + whistleCount
								+ " whistle");
					} else {
						profileWhistleCount.setText("" + whistleCount
								+ " whistles");
					}
				} else {
				}
			}
		});
	}

	private void setupTabs() {
		tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Cliks");
		spec1.setContent(R.id.cliks);
		spec1.setIndicator("Cliks");

		TabSpec spec2 = tabHost.newTabSpec("Whistles");
		spec2.setIndicator("Whistles");
		spec2.setContent(R.id.whistles);

		TabSpec spec3 = tabHost.newTabSpec("Pods");
		spec3.setContent(R.id.pods);
		spec3.setIndicator("Pods");

		TabSpec spec4 = tabHost.newTabSpec("Stats");
		spec4.setContent(R.id.stats);
		spec4.setIndicator("Stats");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
	}

}

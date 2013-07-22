package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewOtherProfileActivity extends Activity {
	private TabHost tabHost;

	private int whistleCount;
	private int clikCount;
	private Number userId;
	private List<ParseObject> mClikObjects = new ArrayList<ParseObject>();
	private List<ParseObject> mWhistleObjects = new ArrayList<ParseObject>();
	private byte[] userImg;

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();
	private GetPlaceholder placeholder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_other_profile);

		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		userId = (Number) b.getInt("iUserId", 0);
		userImg = b.getByteArray("iUserImg");

		placeholder = new GetPlaceholder(this);

		setupTabs();

		getUserName();

		getWhistleCount();

		getClikCount();

		getProfilePicture();

		getCliksAnswered();

		getWhistlesCreated();
	}

	private void getUserName() {
		final TextView textView = (TextView) findViewById(R.id.other_profile_user_name);
		ParseQuery<ParseObject> qUser = ParseQuery.getQuery("Users");
		qUser.whereEqualTo("userId", userId);
		qUser.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				String firstName = object.getString("firstName");
				String lastName = object.getString("lastName");
				textView.setText(firstName + " " + lastName);
			}
		});
	}

	private void getWhistleCount() {
		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");
		qVoteQues.whereEqualTo("userId", userId);
		qVoteQues.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					whistleCount = count;
					TextView profileWhistleCount = (TextView) findViewById(R.id.other_profile_whistle_count);
					if (whistleCount == 1) {
						profileWhistleCount.setText("" + whistleCount
								+ " whistle");
					} else {
						profileWhistleCount.setText("" + whistleCount
								+ " whistles");
					}
				}
			}
		});
	}

	private void getClikCount() {
		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");
		qVoteAnswer.whereEqualTo("userId", userId);
		qVoteAnswer.countInBackground(new CountCallback() {
			public void done(int count, ParseException e) {
				if (e == null) {
					clikCount = count;
					TextView profileClikCount = (TextView) findViewById(R.id.other_profile_clik_count);
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

	private void getWhistlesCreated() {
		final ListView listView = (ListView) findViewById(R.id.other_profile_whistles);
		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");
		qVoteQues.whereEqualTo("userId", userId);
		qVoteQues.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {
					mWhistleObjects = objects;

					WhistleListAdapter whistlesAdapter = new WhistleListAdapter(
							ViewOtherProfileActivity.this, mWhistleObjects);

					listView.setAdapter(whistlesAdapter);

					listView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							viewWhistle(mWhistleObjects.get(position));
						}
					});
				}
			}
		});
	}

	private void getCliksAnswered() {
		final ListView listView = (ListView) findViewById(R.id.other_profile_cliks);

		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");
		qVoteAnswer.whereEqualTo("userId", userId);
		qVoteAnswer.findInBackground(new FindCallback<ParseObject>() {
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < objects.size(); i++) {
						Number quesId = objects.get(i).getNumber("quesId");
						ParseQuery<ParseObject> qVoteQues = ParseQuery
								.getQuery("VoteQues");
						qVoteQues.whereEqualTo("quesId", quesId);
						qVoteQues
								.getFirstInBackground(new GetCallback<ParseObject>() {

									@Override
									public void done(ParseObject object,
											ParseException e) {
										if (e == null) {
											mClikObjects.add(object);

											WhistleListAdapter whistlesAdapter = new WhistleListAdapter(
													ViewOtherProfileActivity.this,
													mClikObjects);

											listView.setAdapter(whistlesAdapter);

											listView.setOnItemClickListener(new OnItemClickListener() {
												public void onItemClick(
														AdapterView<?> parent,
														View v, int position,
														long id) {
													viewWhistle(mClikObjects
															.get(position));
												}
											});
										}
									}
								});
					}
				}
			}
		});
	}

	private void viewWhistle(final ParseObject object) {
		final Intent intent = new Intent(ViewOtherProfileActivity.this,
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

	private void getProfilePicture() {
		ImageView image = (ImageView) findViewById(R.id.other_profile_picture);
		Bitmap cropped;
		Bitmap source = decode.decodeSampledBitmap(userImg, 150, 150);
		if (source.getWidth() >= source.getHeight()) {
			cropped = Bitmap.createBitmap(source, source.getWidth() / 2
					- source.getHeight() / 2, 0, source.getHeight(),
					source.getHeight());
		} else {
			cropped = Bitmap.createBitmap(source, 0, source.getHeight() / 2
					- source.getWidth() / 2, source.getWidth(),
					source.getWidth());
		}
		Bitmap scaled = Bitmap.createScaledBitmap(cropped, 150, 150, true);
		image.setImageBitmap(scaled);
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

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
	}

}

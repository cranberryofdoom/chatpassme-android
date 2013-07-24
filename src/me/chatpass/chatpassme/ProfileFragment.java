package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class ProfileFragment extends Fragment {
	private TabHost tabHost;

	private int whistleCount;
	private int clikCount;
	private List<ParseObject> mClikObjects = new ArrayList<ParseObject>();
	private List<ParseObject> mWhistleObjects = new ArrayList<ParseObject>();

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();
	private GetPlaceholder placeholder;

	// Temporary
	private Number userId = 257;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		final View view = inflater.inflate(R.layout.fragment_profile,
				container, false);

		placeholder = new GetPlaceholder(getActivity());

		setHasOptionsMenu(true);

		// // Get installation id
		// final ParseInstallation id =
		// ParseInstallation.getCurrentInstallation();
		//
		// // Get user's phoneNumber
		// String phoneNumber = id.getString("phoneNumber");
		//
		// // Create a Parse object query and ask for the Users class
		// ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		//
		// // Ask for the specific phone number that is correlated to the user
		// qUsers.whereEqualTo("phoneNumber", phoneNumber);
		//
		// // Find the first object that matches that shows up
		// qUsers.getFirstInBackground(new GetCallback<ParseObject>() {
		// public void done(ParseObject object, ParseException e) {
		// if (e == null) {
		//
		// // Get user's user id from that object
		// Number userId = id.getNumber("userId");
		//
		// // Create a ParseObject query and ask for the VoteQues
		// // class
		// ParseQuery<ParseObject> qVoteQues = ParseQuery
		// .getQuery("VoteQues");
		//
		// // Ask for all the times userId shows up
		// qVoteQues.whereEqualTo("userId", userId);
		//
		// // Count how many times userId has created a whistle
		// qVoteQues.countInBackground(new CountCallback() {
		// public void done(int count, ParseException e) {
		// if (e == null) {
		//
		// // Record the whistle number
		// whistleCount = count;
		// } else {
		// }
		// }
		// });
		// } else {
		// }
		// }
		// });
		setupTabs(view);

		getUserName(view);

		getWhistleCount(view);

		getClikCount(view);

		getProfilePicture(view);

		getCliksAnswered(view);

		getWhistlesCreated(view);

		return view;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.settings, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void getUserName(View view) {
		final TextView textView = (TextView) view.findViewById(R.id.user_name);

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

	private void getWhistleCount(final View view) {
		final TextView profileWhistleCount = (TextView) view
				.findViewById(R.id.profile_whistle_count);

		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");
		qVoteQues.whereEqualTo("userId", userId);
		qVoteQues.countInBackground(new CountCallback() {

			@Override
			public void done(int count, ParseException e) {
				if (e == null) {
					whistleCount = count;
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

	private void getClikCount(final View view) {
		final TextView profileClikCount = (TextView) view
				.findViewById(R.id.profile_clik_count);

		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");
		qVoteAnswer.whereEqualTo("userId", userId);
		qVoteAnswer.countInBackground(new CountCallback() {

			@Override
			public void done(int count, ParseException e) {
				if (e == null) {
					clikCount = count;
					if (clikCount == 1) {
						profileClikCount.setText("" + clikCount + " clik");
					} else {
						profileClikCount.setText("" + clikCount + " cliks");
					}
				}
			}
		});
	}

	private void getWhistlesCreated(View view) {
		final ListView listView = (ListView) view
				.findViewById(R.id.profile_whistles);

		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");
		qVoteQues.whereEqualTo("userId", userId);
		qVoteQues.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				if (e == null) {
					mWhistleObjects = objects;

					WhistleListAdapter whistlesAdapter = new WhistleListAdapter(
							getActivity(), mWhistleObjects);

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

	private void getCliksAnswered(View view) {
		final ListView listView = (ListView) view
				.findViewById(R.id.profile_cliks);

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
													getActivity(), mClikObjects);

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

	private void getProfilePicture(final View view) {
		final ImageView image = (ImageView) view
				.findViewById(R.id.profile_picture);

		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		qUsers.whereEqualTo("userId", userId);
		qUsers.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject Object, ParseException e) {
				if (e == null) {
					ParseFile imageFile = (ParseFile) Object.get("imageFile");
					imageFile.getDataInBackground(new GetDataCallback() {

						@Override
						public void done(byte[] data, ParseException e) {
							Bitmap cropped;
							Bitmap source = decode.decodeSampledBitmap(data,
									150, 150);
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
									150, 150, true);
							image.setImageBitmap(scaled);
						}
					});
				}
			}
		});
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});
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

	public void chooseImage(View view) {
		ChooseProfilePictureDialogFragment profilePictureDialog = new ChooseProfilePictureDialogFragment();
		profilePictureDialog.setTargetFragment(this, 1);
		profilePictureDialog.show(getFragmentManager(), "chooseProfilePicture");
	}

	private void setupTabs(View view) {
		tabHost = (TabHost) view.findViewById(R.id.tabhost);
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

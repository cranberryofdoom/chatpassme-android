package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewWhistleActivity extends Activity {
	private TabHost tabHost;
	private CommentGridAdapter gridAdapter;
	private GridView gridView;
	private DialogFragment reportWhistleFragment;
	private View textWhistle;
	private View photoWhistle;
	private View rateWhistle;

	private String objectId;
	private String quesTxt;
	private Number quesId;
	private Integer hitCount;
	private byte[] quesImg;
	private byte[] userImg;
	private String ansType;
	private ParseObject voteQues;

	private ImageButton iButton1;
	private ImageButton iButton2;
	private ImageButton iButton3;
	private ImageButton iButton4;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();

	// Temporary user Id
	private Number myUserId = 257;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_whistle);

		// Retrieve all data from the last activity
		Intent intent = getIntent();
		objectId = intent.getStringExtra("iObjectId");
		quesTxt = intent.getStringExtra("iQuesTxt");
		hitCount = intent.getIntExtra("iHitCount", 0);
		quesImg = intent.getByteArrayExtra("iQuesImg");
		userImg = intent.getByteArrayExtra("iUserImg");

		setupActionBar();

		setupWhistleInfo();

		checkVisibleAndDisplay();

		setupComments();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.view_whistle, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.report:
			reportWhistleFragment = new ReportWhistleDialogFragment();
			reportWhistleFragment.show(getFragmentManager(), "reportWhistle");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// Set the basic information about the whistle
	private void setupWhistleInfo() {

		// Set the whistle question text
		TextView viewWhistleQuestion = (TextView) findViewById(R.id.view_whistle_question);
		viewWhistleQuestion.setText(quesTxt);

		// Set the clik count
		TextView viewWhistleClikCount = (TextView) findViewById(R.id.view_whistle_clik_count);
		if (hitCount == 1) {
			viewWhistleClikCount.setText("" + hitCount + " clik");
		} else {
			viewWhistleClikCount.setText("" + hitCount + " cliks");
		}

		// Set the user profile image
		ImageButton viewWhistleUserImage = (ImageButton) findViewById(R.id.view_whistle_user_image);
		viewWhistleUserImage.setImageBitmap(decode.decodeSampledBitmap(userImg,
				50, 50));

		// Set the whistle question image
		ImageView viewWhistleQuestionImage = (ImageView) findViewById(R.id.view_whistle_question_image);
		viewWhistleQuestionImage.setImageBitmap(decode.decodeSampledBitmap(
				quesImg, 50, 50));

		// Get the object id for this whistle
		final ParseQuery<ParseObject> qVoteQues = ParseQuery
				.getQuery("VoteQues");
		try {
			voteQues = qVoteQues.get(objectId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Get the question id for this whistle
		quesId = voteQues.getNumber("quesId");
	}

	private void checkVisibleAndDisplay() {
		// Check if this user has seen this whistle or not
		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");
		qVoteAnswer.whereEqualTo("quesId", quesId);
		qVoteAnswer.whereEqualTo("userId", myUserId);
		qVoteAnswer.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {

				// Get the type of question this whistle is
				ansType = voteQues.getString("ansType");

				// If this user has not answered the question before
				if (object == null) {
					if (ansType.equals("TXT")) {
						setupTextAnswer();
					}

					else if (ansType.equals("PHOT")) {
						setupImageAnswer();
					}

					else if (ansType.equals("RATE")) {
						setupRatingAnswer();
					}
				}

				// If this user answered the question before
				else {

					// Setup everything with the tabs but disable buttons
					if (ansType.equals("TXT")) {
						setupTextAnswer();
					} else if (ansType.equals("PHOT")) {
						setupImageAnswer();
					} else if (ansType.equals("RATE")) {
						setupRatingAnswer();
					}

					// Set the tabs
					setupResults();
				}
			}
		});
	}

	// Set the buttons and the picture for the rate questions
	private void setupRatingAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rateWhistle = inflater.inflate(R.layout.layout_rate_whistle,
				viewWhistleAnswers, false);
		viewWhistleAnswers.addView(rateWhistle);
		
		// Get the rate question image
		try {
			if (voteQues.getParseFile("rateOptImg") != null) {
				ParseFile rateOptImg = voteQues.getParseFile("rateOptImg");
				byte[] dRateOptImg = rateOptImg.getData();
				ImageView vRateOptImg = (ImageView) rateWhistle.findViewById(R.id.rate_image);
				vRateOptImg.setImageBitmap(decode.decodeSampledBitmap(
						dRateOptImg, 100, 200));
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// Setup all the buttons
		iButton1 = (ImageButton) rateWhistle.findViewById(R.id.button1);
		iButton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(1, true);
			}
		});

		iButton2 = (ImageButton) rateWhistle.findViewById(R.id.button2);
		iButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(2, true);
			}
		});

		iButton3 = (ImageButton) rateWhistle.findViewById(R.id.button3);
		iButton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(3, true);
			}
		});

		iButton4 = (ImageButton) rateWhistle.findViewById(R.id.button4);
		iButton4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(4, true);
			}
		});
	}

	// Set the buttons for the photo questions
	private void setupImageAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		photoWhistle = inflater.inflate(R.layout.layout_photo_whistle,
				viewWhistleAnswers, false);
		viewWhistleAnswers.addView(photoWhistle);

		// Set all the buttons and the corresponding images
		try {
			ParseFile ansOptImg1 = voteQues.getParseFile("ansOptImg1");
			byte[] dAnsOptImg1 = ansOptImg1.getData();
			iButton1 = (ImageButton) photoWhistle.findViewById(R.id.button1);
			iButton1.setImageBitmap(decode.decodeSampledBitmap(dAnsOptImg1, 50,
					50));
			iButton1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(1, false);
				}
			});

			ParseFile ansOptImg2 = voteQues.getParseFile("ansOptImg2");
			byte[] dAnsOptImg2 = ansOptImg2.getData();
			iButton2 = (ImageButton) photoWhistle.findViewById(R.id.button2);
			iButton2.setImageBitmap(decode.decodeSampledBitmap(dAnsOptImg2, 50,
					50));
			iButton2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(2, false);
				}
			});

			if (voteQues.getParseFile("ansOptImg3") != null) {
				ParseFile ansOptImg3 = voteQues.getParseFile("ansOptImg3");
				byte[] dAnsOptImg3 = ansOptImg3.getData();
				iButton3 = (ImageButton) photoWhistle
						.findViewById(R.id.button3);
				iButton3.setImageBitmap(decode.decodeSampledBitmap(dAnsOptImg3,
						50, 50));
				iButton3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						recordAnswer(3, false);
					}
				});

				if (voteQues.getParseFile("ansOptImg4") != null) {
					ParseFile ansOptImg4 = voteQues.getParseFile("ansOptImg4");
					byte[] dAnsOptImg4 = ansOptImg4.getData();
					iButton4 = (ImageButton) photoWhistle
							.findViewById(R.id.button4);
					iButton4.setImageBitmap(decode.decodeSampledBitmap(
							dAnsOptImg4, 50, 50));
					iButton4.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							recordAnswer(4, false);
						}
					});
				}
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	// Set the buttons for the text questions
	private void setupTextAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		textWhistle = inflater.inflate(R.layout.layout_text_whistle,
				viewWhistleAnswers, false);
		viewWhistleAnswers.addView(textWhistle);

		// Set all the buttons
		String ansOptTxt1 = voteQues.getString("ansOptTxt1");
		button1 = (Button) textWhistle.findViewById(R.id.button1);
		button1.setText(ansOptTxt1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(1, false);
			}
		});

		String ansOptTxt2 = voteQues.getString("ansOptTxt2");
		button2 = (Button) textWhistle.findViewById(R.id.button2);
		button2.setText(ansOptTxt2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(2, false);
			}
		});

		if (!voteQues.getString("ansOptTxt3").equals("")) {
			String ansOptTxt3 = voteQues.getString("ansOptTxt3");
			button3 = (Button) textWhistle.findViewById(R.id.button3);
			button3.setText(ansOptTxt3);
			button3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(3, false);
				}
			});

			if (!voteQues.getString("ansOptTxt4").equals("")) {
				String ansOptTxt4 = voteQues.getString("ansOptTxt4");
				button4 = (Button) textWhistle.findViewById(R.id.button4);
				button4.setText(ansOptTxt4);
				button4.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						recordAnswer(4, false);
					}
				});
			}
		}
	}

	// Method for the inappropriate button in the report dialog
	public void reportInappropriate(View view) {
		// ThisUser myUser = new ThisUser(
		// ParseInstallation.getCurrentInstallation());
		// Number myUserId = myUser.userId();
		ParseObject voteQuesFlag = new ParseObject("VoteQuesFlag");
		voteQuesFlag.put("flagType", "INA");
		voteQuesFlag.put("quesId", quesId);
		voteQuesFlag.put("userId", myUserId);
		voteQuesFlag.saveInBackground();
		reportWhistleFragment.dismiss();
	}

	// Method for the repetitive button in the report dialog
	public void reportRepetitive(View view) {
		// ThisUser myUser = new ThisUser(
		// ParseInstallation.getCurrentInstallation());
		// Number myUserId = myUser.userId();
		ThisUser myUser = new ThisUser(
				ParseInstallation.getCurrentInstallation());
		Number myUserId = myUser.userId();
		ParseObject voteQuesFlag = new ParseObject("VoteQuesFlag");
		voteQuesFlag.put("flagType", "REP");
		voteQuesFlag.put("quesId", quesId);
		voteQuesFlag.put("userId", myUserId);
		voteQuesFlag.saveInBackground();
		reportWhistleFragment.dismiss();
	}

	// What happens when any one of the answer buttons are pressed
	public void recordAnswer(int answer, boolean rating) {
		for (int i = 1; i <= 4; i++) {
			if (answer == i) {
				int hitcount = (Integer) voteQues.getNumber("ansOptHitcount"
						+ i);
				hitcount++;
				voteQues.put("ansOptHitcount" + i, (Number) hitcount);
				voteQues.put("hitCount", hitCount + 1);
				voteQues.saveInBackground();
				recordVoteAnswer(answer, rating);
			}
		}
		setupResults();
	}

	private void recordVoteAnswer(int answer, boolean rating) {
		ParseObject voteAnswer = new ParseObject("voteAnswer");
		voteAnswer.put("favorite", false);
		voteAnswer.put("optSelected", answer);
		voteAnswer.put("quesId", quesId);
		voteAnswer.put("skipped", false);
		voteAnswer.put("userId", myUserId);
		if (rating == true) {
			voteAnswer.put("rateVal", answer);
		} else {
			voteAnswer.put("rateVal", 0);
		}
		voteAnswer.saveInBackground();
	}

	public void setupResults() {
		tabHost = (TabHost) findViewById(R.id.activity_view_whistle_tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Everyone");
		spec1.setContent(R.id.everyone);
		spec1.setIndicator("Everyone");

		TabSpec spec2 = tabHost.newTabSpec("School");
		spec2.setIndicator("School");
		spec2.setContent(R.id.school);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		if (ansType.equals("TXT")) {
			button1.setEnabled(false);
			addResults(button1, 1);

			button2.setEnabled(false);
			addResults(button2, 2);

			if (!voteQues.getString("ansOptTxt3").equals("")) {
				button3.setEnabled(false);
				addResults(button3, 3);

				if (!voteQues.getString("ansOptTxt4").equals("")) {
					button4.setEnabled(false);
					addResults(button4, 4);
				}
			}
		}

		else if (ansType.equals("PHOT")) {
			iButton1.setEnabled(false);
			addImageResults(iButton1, 1, "PHTO");

			iButton2.setEnabled(false);
			addImageResults(iButton2, 2, "PHTO");

			if (voteQues.getParseFile("ansOptImg3").isDataAvailable()) {
				iButton3.setEnabled(false);
				addImageResults(iButton3, 3, "PHTO");

				if (voteQues.getParseFile("ansOptImg4").isDataAvailable()) {
					iButton4.setEnabled(false);
					addImageResults(iButton4, 4, "PHTO");
				}
			}
		}

		else if (ansType.equals("RATE")) {
			iButton1.setEnabled(false);
			iButton2.setEnabled(false);
			iButton3.setEnabled(false);
			iButton4.setEnabled(false);
			addImageResults(iButton1, 1, "RATE");
			addImageResults(iButton2, 2, "RATE");
			addImageResults(iButton3, 3, "RATE");
			addImageResults(iButton4, 4, "RATE");
		}
	}

	private void addResults(Button button, int i) {
		Float result = voteQues.getNumber("ansOptHitcount" + i).floatValue()
				/ hitCount.floatValue() * 100;
		TextView results = new TextView(ViewWhistleActivity.this);
		results.setText("" + result.intValue() + "%");
		results.setTextSize(20);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button
				.getLayoutParams();
		results.setPadding(25, 0, 0, 0);
		((ViewGroup) textWhistle).addView(results, params);
	}

	private void addImageResults(ImageButton button, int i, String whistleType) {
		Float result = voteQues.getNumber("ansOptHitcount" + i).floatValue()
				/ hitCount.floatValue() * 100;
		TextView results = new TextView(ViewWhistleActivity.this);
		results.setText("" + result.intValue() + "%");
		results.setTextSize(20);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) button
				.getLayoutParams();
		if (whistleType.equals("PHTO")) {
			results.setPadding(100, 0, 0, 0);
			((ViewGroup) photoWhistle).addView(results,params);
		}
		else {
			results.setPaddingRelative(100, 100, 0, 0);
			((ViewGroup) rateWhistle).addView(results,params);
		}
	}

	public void sendComment(View view) {
		EditText editText = (EditText) findViewById(R.id.view_whistle_create_comment);
		String comment = editText.getText().toString();
		ParseObject voteComment = new ParseObject("VoteComment");
		voteComment.put("comment", comment);
		voteComment.put("quesId", quesId);
		voteComment.put("userId", myUserId);

		// Keep track of user pointer
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		qUsers.whereEqualTo("userId", myUserId);
		try {
			ParseObject user = qUsers.getFirst();
			voteComment.put("user", user);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// Keep track of the comment id
		ParseQuery<ParseObject> qVoteComment = ParseQuery
				.getQuery("VoteComment");
		qVoteComment.orderByDescending("commentId");
		Number commentIdCount;
		try {
			commentIdCount = qVoteComment.getFirst().getNumber("commentId");
			commentIdCount = commentIdCount.intValue() + 1;
			voteComment.put("commentId", commentIdCount);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		voteComment.saveInBackground();
		editText.setText("");
		gridAdapter.notifyDataSetChanged();
		gridView.invalidateViews();
		setupComments();
	}

	private void setupComments() {
		gridView = (GridView) findViewById(R.id.view_whistle_comment);
		ParseQuery<ParseObject> qVoteComment = ParseQuery
				.getQuery("VoteComment");

		// Ask for all the comments under that question
		qVoteComment.whereEqualTo("quesId", quesId);
		qVoteComment.addDescendingOrder("createdAt");
		qVoteComment.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {

					ArrayList<String> comments = new ArrayList<String>();
					ArrayList<String> userFirstName = new ArrayList<String>();
					ArrayList<String> userLastName = new ArrayList<String>();
					ArrayList<Bitmap> userImage = new ArrayList<Bitmap>();

					// Retrieve all comments
					for (int i = 0; i < objects.size(); i++) {
						comments.add(objects.get(i).getString("comment"));
						ParseObject user;
						try {
							user = objects.get(i).getParseObject("user")
									.fetchIfNeeded();
							userFirstName.add(user.getString("firstName"));
							userLastName.add(user.getString("lastName"));
							ParseFile imageFile = user
									.getParseFile("imageFile");

							try {
								byte[] dImageFile = imageFile.getData();
								userImage.add(decode.decodeSampledBitmap(
										dImageFile, 50, 50));
							} catch (ParseException e1) {
								e1.printStackTrace();
							}
						} catch (ParseException e2) {
							e2.printStackTrace();
						}
					}

					// Create a new adapter for comments
					gridAdapter = new CommentGridAdapter(
							ViewWhistleActivity.this, comments, userFirstName,
							userLastName, userImage);

					// Set the adapter
					gridView.setAdapter(gridAdapter);
				} else {
				}
			}
		});
	}

	public void viewOtherProfile(View view) {
		Intent intent = new Intent(this, ViewOtherProfileActivity.class);
		intent.putExtra("iUserId", voteQues.getNumber("userId"));
		intent.putExtra("iUserImg", userImg);
		startActivity(intent);
	}

	public void shareWhistle(View view) {

		Intent intent = new Intent(this, ContactsListActivity.class);
		intent.putExtra("iObjectId", objectId);
		intent.putExtra("iQuesTxt", quesTxt);
		intent.putExtra("iHitCount", hitCount);
		intent.putExtra("iQuesImg", quesImg);
		intent.putExtra("iUserImg", userImg);
		startActivity(intent);
	}
}

package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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

		// Show the Up button in the action bar
		setupActionBar();

		// Set the basic information about the whistle
		setupWhistleInfo();

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

		// Set the comments
		setupComments();
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
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

	// Set the buttons and the picture for the rate questions
	private void setupRatingAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);
		Resources res = getResources();

		// Get the rate question image
		try {
			if (voteQues.getParseFile("rateOptImg") != null) {
				ParseFile rateOptImg = voteQues.getParseFile("rateOptImg");
				byte[] dRateOptImg = rateOptImg.getData();
				ImageView vRateOptImg = new ImageView(ViewWhistleActivity.this);
				vRateOptImg.setImageBitmap(decodeSampledBitmap(dRateOptImg,
						100, 200));
				viewWhistleAnswers.addView(vRateOptImg);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// Setup all the buttons
		iButton1 = new ImageButton(ViewWhistleActivity.this);
		iButton1.setImageDrawable(res.getDrawable(R.drawable.ic_rating_yay));
		iButton1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(1, true);
			}
		});

		iButton2 = new ImageButton(ViewWhistleActivity.this);
		iButton2.setImageDrawable(res.getDrawable(R.drawable.ic_rating_unsure));
		iButton2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(2, true);
			}
		});

		iButton3 = new ImageButton(ViewWhistleActivity.this);
		iButton3.setImageDrawable(res.getDrawable(R.drawable.ic_rating_meh));
		iButton3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(3, true);
			}
		});

		iButton4 = new ImageButton(ViewWhistleActivity.this);
		iButton4.setImageDrawable(res.getDrawable(R.drawable.ic_rating_ew));
		iButton4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(4, true);
			}
		});

		// Setup the row of rate buttons
		LinearLayout rateRow = new LinearLayout(ViewWhistleActivity.this);
		rateRow.setGravity(Gravity.CENTER);
		viewWhistleAnswers.addView(rateRow);

		TextView love = new TextView(ViewWhistleActivity.this);
		love.setText("LOVE");
		TextView hate = new TextView(ViewWhistleActivity.this);
		hate.setText("HATE");

		rateRow.addView(love);
		rateRow.addView(iButton1);
		rateRow.addView(iButton2);
		rateRow.addView(iButton3);
		rateRow.addView(iButton4);
		rateRow.addView(hate);
	}

	// Set the buttons for the photo questions
	private void setupImageAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);
		LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);

		// Setup and add the first row of images
		LinearLayout answersRow1 = new LinearLayout(ViewWhistleActivity.this);
		viewWhistleAnswers.addView(answersRow1);

		// Setup the second row of images
		LinearLayout answersRow2 = new LinearLayout(ViewWhistleActivity.this);

		// Set all the buttons and the corresponding images
		try {
			ParseFile ansOptImg1 = voteQues.getParseFile("ansOptImg1");
			byte[] dAnsOptImg1 = ansOptImg1.getData();
			iButton1 = new ImageButton(ViewWhistleActivity.this);
			iButton1.setLayoutParams(param);
			iButton1.setImageBitmap(decodeSampledBitmap(dAnsOptImg1, 50, 50));
			answersRow1.addView(iButton1);
			iButton1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(1, false);
				}
			});

			ParseFile ansOptImg2 = voteQues.getParseFile("ansOptImg2");
			byte[] dAnsOptImg2 = ansOptImg2.getData();
			iButton2 = new ImageButton(ViewWhistleActivity.this);
			iButton2.setLayoutParams(param);
			iButton2.setImageBitmap(decodeSampledBitmap(dAnsOptImg2, 50, 50));
			answersRow1.addView(iButton2);
			iButton2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(2, false);
				}
			});

			if (voteQues.getParseFile("ansOptImg3") != null) {
				viewWhistleAnswers.addView(answersRow2);
				ParseFile ansOptImg3 = voteQues.getParseFile("ansOptImg3");
				byte[] dAnsOptImg3 = ansOptImg3.getData();
				iButton3 = new ImageButton(ViewWhistleActivity.this);
				iButton3.setLayoutParams(param);
				iButton3.setImageBitmap(decodeSampledBitmap(dAnsOptImg3, 50, 50));
				answersRow2.addView(iButton3);
				iButton3.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						recordAnswer(3, false);
					}
				});
			}

			if (voteQues.getParseFile("ansOptImg4") != null) {
				ParseFile ansOptImg4 = voteQues.getParseFile("ansOptImg4");
				byte[] dAnsOptImg4 = ansOptImg4.getData();
				iButton4 = new ImageButton(ViewWhistleActivity.this);
				iButton4.setLayoutParams(param);
				iButton4.setImageBitmap(decodeSampledBitmap(dAnsOptImg4, 50, 50));
				answersRow2.addView(iButton4);
				iButton4.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						recordAnswer(4, false);
					}
				});
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	// Set the buttons for the text questions
	private void setupTextAnswer() {
		LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);

		// Set all the buttons
		String ansOptTxt1 = voteQues.getString("ansOptTxt1");
		button1 = new Button(ViewWhistleActivity.this);
		button1.setText(ansOptTxt1);
		viewWhistleAnswers.addView(button1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(1, false);
			}
		});

		String ansOptTxt2 = voteQues.getString("ansOptTxt2");
		button2 = new Button(ViewWhistleActivity.this);
		button2.setText(ansOptTxt2);
		viewWhistleAnswers.addView(button2);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recordAnswer(2, false);
			}
		});

		if (voteQues.getString("ansOptTxt3") != null) {
			String ansOptTxt3 = voteQues.getString("ansOptTxt3");
			button3 = new Button(ViewWhistleActivity.this);
			button3.setText(ansOptTxt3);
			viewWhistleAnswers.addView(button3);
			button3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(3, false);
				}
			});
		}

		if (voteQues.getString("ansOptTxt4") != null) {
			String ansOptTxt4 = voteQues.getString("ansOptTxt4");
			button4 = new Button(ViewWhistleActivity.this);
			button4.setText(ansOptTxt4);
			viewWhistleAnswers.addView(button4);
			button4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					recordAnswer(4, false);
				}
			});
		}
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
		viewWhistleUserImage
				.setImageBitmap(decodeSampledBitmap(userImg, 50, 50));

		// Set the whistle question image
		ImageView viewWhistleQuestionImage = (ImageView) findViewById(R.id.view_whistle_question_image);
		viewWhistleQuestionImage.setImageBitmap(decodeSampledBitmap(quesImg,
				50, 50));

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

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
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
		if (answer == 1) {
			int hitcount1 = (Integer) voteQues.getNumber("ansOptHitcount1");
			hitcount1 = hitcount1 + 1;
			voteQues.put("ansOptHitcount1", (Number) hitcount1);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
			recordVoteAnswer(answer, rating);
		} else if (answer == 2) {
			int hitcount2 = (Integer) voteQues.getNumber("ansOptHitcount2");
			hitcount2 = hitcount2 + 1;
			voteQues.put("ansOptHitcount2", (Number) hitcount2);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
			recordVoteAnswer(answer, rating);
		} else if (answer == 3) {
			int hitcount3 = (Integer) voteQues.getNumber("ansOptHitcount3");
			hitcount3 = hitcount3 + 1;
			voteQues.put("ansOptHitcount3", (Number) hitcount3);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
			recordVoteAnswer(answer, rating);

		} else if (answer == 4) {
			int hitcount4 = (Integer) voteQues.getNumber("ansOptHitcount4");
			hitcount4 = hitcount4 + 1;
			voteQues.put("ansOptHitcount4", (Number) hitcount4);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
			recordVoteAnswer(answer, rating);
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

		TabSpec spec3 = tabHost.newTabSpec("Friends");
		spec3.setContent(R.id.friends);
		spec3.setIndicator("Friends");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);

		if (ansType.equals("TXT")) {
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			button4.setEnabled(false);
		}

		if (ansType.equals("PHOT") || ansType.equals("RATE")) {
			iButton1.setEnabled(false);
			iButton2.setEnabled(false);
			iButton3.setEnabled(false);
			iButton4.setEnabled(false);
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
								userImage.add(decodeSampledBitmap(dImageFile,
										50, 50));
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
}

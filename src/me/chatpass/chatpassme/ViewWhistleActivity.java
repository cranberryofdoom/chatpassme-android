package me.chatpass.chatpassme;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewWhistleActivity extends Activity {

	private TabHost tabHost;
	private String objectId;
	private Number quesId;
	private DialogFragment reportWhistleFragment;
	private String ansType;
	private Integer hitCount;
	private ParseObject voteQues;
	private byte[] dAnsOptImg1;
	private byte[] dAnsOptImg2;
	private byte[] dAnsOptImg3;
	private byte[] dAnsOptImg4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_whistle);
		Intent intent = getIntent();
		objectId = intent.getStringExtra("iObjectId");
		final String quesTxt = intent.getStringExtra("iQuesTxt");
		hitCount = intent.getIntExtra("iHitCount", 0);
		final byte[] quesImg = intent.getByteArrayExtra("iQuesImg");
		final byte[] userImg = intent.getByteArrayExtra("iUserImg");

		// Show the Up button in the action bar
		setupActionBar();

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

		final ParseQuery<ParseObject> qVoteQues = ParseQuery
				.getQuery("VoteQues");

		// Get the ParseObject for this whistle
		try {
			voteQues = qVoteQues.get(objectId);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// Get the question id for this whistle
		quesId = voteQues.getNumber("quesId");

		// Check if this user has seen this whistle or not
		ParseQuery<ParseObject> qVoteAnswer = ParseQuery.getQuery("voteAnswer");
		qVoteAnswer.whereEqualTo("quesId", quesId);
		qVoteAnswer.whereEqualTo("userId", 257);
		qVoteAnswer.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {

				// If this user has not answered the question before
				if (object == null) {

					// Get the type of question this whistle is
					ansType = voteQues.getString("ansType");

					// If the answer type is a text answer
					if (ansType.equals("TXT")) {

						// Find the layout to fill with content
						LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);

						// Get the first text option
						String ansOptTxt1 = voteQues.getString("ansOptTxt1");
						Button bAnsOptTxt1 = new Button(
								ViewWhistleActivity.this);
						bAnsOptTxt1.setText(ansOptTxt1);
						viewWhistleAnswers.addView(bAnsOptTxt1);
						bAnsOptTxt1
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										recordAnswer(1);
									}
								});
						// Get the second text option
						String ansOptTxt2 = voteQues.getString("ansOptTxt2");
						Button bAnsOptTxt2 = new Button(
								ViewWhistleActivity.this);
						bAnsOptTxt2.setText(ansOptTxt2);
						viewWhistleAnswers.addView(bAnsOptTxt2);
						bAnsOptTxt2
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								recordAnswer(2);
							}
						});

						// Get the third text option
						if (voteQues.getString("ansOptTxt3") != null) {
							String ansOptTxt3 = voteQues
									.getString("ansOptTxt3");
							Button bAnsOptTxt3 = new Button(
									ViewWhistleActivity.this);
							bAnsOptTxt3.setText(ansOptTxt3);
							viewWhistleAnswers.addView(bAnsOptTxt3);
							bAnsOptTxt3
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									recordAnswer(3);
								}
							});
						}

						// Get the fourth text option
						if (voteQues.getString("ansOptTxt4") != null) {
							String ansOptTxt4 = voteQues
									.getString("ansOptTxt4");
							Button bAnsOptTxt4 = new Button(
									ViewWhistleActivity.this);
							bAnsOptTxt4.setText(ansOptTxt4);
							viewWhistleAnswers.addView(bAnsOptTxt4);
							bAnsOptTxt4
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									recordAnswer(4);
								}
							});
						}
					}

					// If the answer type is a photo answer
					else if (ansType.equals("PHOT")) {

						// Find the layout to fill with content
						LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);

						LayoutParams param = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT, 1.0f);

						// Setup the first row of images
						LinearLayout answersRow1 = new LinearLayout(
								ViewWhistleActivity.this);
						viewWhistleAnswers.addView(answersRow1);

						// Setup the second row of images
						LinearLayout answersRow2 = new LinearLayout(
								ViewWhistleActivity.this);

						try {

							// Get the first image option
							ParseFile ansOptImg1 = voteQues
									.getParseFile("ansOptImg1");
							dAnsOptImg1 = ansOptImg1.getData();
							ImageButton bAnsOptImg1 = new ImageButton(
									ViewWhistleActivity.this);
							bAnsOptImg1.setLayoutParams(param);
							bAnsOptImg1.setImageBitmap(decodeSampledBitmap(
									dAnsOptImg1, 50, 50));
							answersRow1.addView(bAnsOptImg1);
							bAnsOptImg1
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									recordAnswer(1);
								}
							});

							// Get the second image option
							ParseFile ansOptImg2 = voteQues
									.getParseFile("ansOptImg2");
							dAnsOptImg2 = ansOptImg2.getData();
							ImageButton bAnsOptImg2 = new ImageButton(
									ViewWhistleActivity.this);
							bAnsOptImg2.setLayoutParams(param);
							bAnsOptImg2.setImageBitmap(decodeSampledBitmap(
									dAnsOptImg2, 50, 50));
							answersRow1.addView(bAnsOptImg2);
							bAnsOptImg2
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									recordAnswer(2);
								}
							});

							// Get the third image option
							if (voteQues.getParseFile("ansOptImg3") != null) {

								// Add another row of images
								viewWhistleAnswers.addView(answersRow2);

								ParseFile ansOptImg3 = voteQues
										.getParseFile("ansOptImg3");
								dAnsOptImg3 = ansOptImg3.getData();
								ImageButton bAnsOptImg3 = new ImageButton(
										ViewWhistleActivity.this);
								bAnsOptImg3.setLayoutParams(param);
								bAnsOptImg3.setImageBitmap(decodeSampledBitmap(
										dAnsOptImg3, 50, 50));
								answersRow2.addView(bAnsOptImg3);
								bAnsOptImg3
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										recordAnswer(3);
									}
								});
							}

							// Get the fourth image option
							if (voteQues.getParseFile("ansOptImg4") != null) {
								ParseFile ansOptImg4 = voteQues
										.getParseFile("ansOptImg4");
								dAnsOptImg4 = ansOptImg4.getData();
								ImageButton bAnsOptImg4 = new ImageButton(
										ViewWhistleActivity.this);
								bAnsOptImg4.setLayoutParams(param);
								bAnsOptImg4.setImageBitmap(decodeSampledBitmap(
										dAnsOptImg4, 50, 50));
								answersRow2.addView(bAnsOptImg4);
								bAnsOptImg4
								.setOnClickListener(new View.OnClickListener() {
									public void onClick(View v) {
										recordAnswer(4);
									}
								});
							}
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
					}

					// If the answer type is a rating answer
					else if (ansType.equals("RATE")) {

						// Find the layout to fill with content
						LinearLayout viewWhistleAnswers = (LinearLayout) findViewById(R.id.view_whistle_answers);

						// Setup resources
						Resources res = getResources();

						try {
							if (voteQues.getParseFile("rateOptImg") != null) {
								ParseFile rateOptImg = voteQues
										.getParseFile("rateOptImg");
								byte[] dRateOptImg = rateOptImg.getData();
								ImageView vRateOptImg = new ImageView(
										ViewWhistleActivity.this);
								vRateOptImg.setImageBitmap(decodeSampledBitmap(
										dRateOptImg, 100, 200));
								viewWhistleAnswers.addView(vRateOptImg);
							}
						} catch (ParseException e1) {
							e1.printStackTrace();
						}

						// Setup all the buttons
						ImageButton bAnsOptRate1 = new ImageButton(
								ViewWhistleActivity.this);
						bAnsOptRate1.setImageDrawable(res
								.getDrawable(R.drawable.ic_rating_yay));
						bAnsOptRate1
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								recordAnswer(1);
							}
						});

						ImageButton bAnsOptRate2 = new ImageButton(
								ViewWhistleActivity.this);
						bAnsOptRate2.setImageDrawable(res
								.getDrawable(R.drawable.ic_rating_unsure));
						bAnsOptRate2
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								recordAnswer(2);
							}
						});

						ImageButton bAnsOptRate3 = new ImageButton(
								ViewWhistleActivity.this);
						bAnsOptRate3.setImageDrawable(res
								.getDrawable(R.drawable.ic_rating_meh));
						bAnsOptRate3
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								recordAnswer(3);
							}
						});

						ImageButton bAnsOptRate4 = new ImageButton(
								ViewWhistleActivity.this);
						bAnsOptRate4.setImageDrawable(res
								.getDrawable(R.drawable.ic_rating_ew));
						bAnsOptRate4
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								recordAnswer(4);
							}
						});

						// Setup the row of rate buttons
						LinearLayout rateRow = new LinearLayout(
								ViewWhistleActivity.this);
						rateRow.setGravity(Gravity.CENTER);
						viewWhistleAnswers.addView(rateRow);
						TextView love = new TextView(ViewWhistleActivity.this);
						love.setText("LOVE");
						TextView hate = new TextView(ViewWhistleActivity.this);
						hate.setText("HATE");
						rateRow.addView(love);
						rateRow.addView(bAnsOptRate1);
						rateRow.addView(bAnsOptRate2);
						rateRow.addView(bAnsOptRate3);
						rateRow.addView(bAnsOptRate4);
						rateRow.addView(hate);
					}
				}

				// If this user answered the question before
				else {
					loadResults();
				}
			}
		});

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
			reportWhistleFragment = new ReportWhistleFragment();
			reportWhistleFragment.show(getFragmentManager(), "reportWhistle");
			return true;
		}
		return super.onOptionsItemSelected(item);
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

	public void reportInappropriate(View view) {
		// ThisUser myUser = new ThisUser(
		// ParseInstallation.getCurrentInstallation());
		// Number myUserId = myUser.userId();
		Number myUserId = 257;
		ParseObject voteQuesFlag = new ParseObject("VoteQuesFlag");
		voteQuesFlag.put("flagType", "INA");
		voteQuesFlag.put("quesId", quesId);
		voteQuesFlag.put("userId", myUserId);
		voteQuesFlag.saveInBackground();
		reportWhistleFragment.dismiss();
	}

	public void reportRepetitive(View view) {
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

	public void recordAnswer(int answer) {
		if (answer == 1) {
			int hitcount1 = (Integer) voteQues.getNumber("ansOptHitcount1");
			hitcount1 = hitcount1 + 1;
			voteQues.put("ansOptHitcount1", (Number) hitcount1);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
		} else if (answer == 2) {
			int hitcount2 = (Integer) voteQues.getNumber("ansOptHitcount2");
			hitcount2 = hitcount2 + 1;
			voteQues.put("ansOptHitcount2", (Number) hitcount2);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
		} else if (answer == 3) {
			int hitcount3 = (Integer) voteQues.getNumber("ansOptHitcount3");
			hitcount3 = hitcount3 + 1;
			voteQues.put("ansOptHitcount3", (Number) hitcount3);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();

		} else if (answer == 4) {
			int hitcount4 = (Integer) voteQues.getNumber("ansOptHitcount4");
			hitcount4 = hitcount4 + 1;
			voteQues.put("ansOptHitcount4", (Number) hitcount4);
			voteQues.put("hitCount", hitCount + 1);
			voteQues.saveInBackground();
		}
		loadResults();
	}
	
	public void loadResults() {		
		
		// Set up the tabs and the results
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
	}
}

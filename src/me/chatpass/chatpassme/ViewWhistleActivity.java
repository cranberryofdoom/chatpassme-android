package me.chatpass.chatpassme;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ViewWhistleActivity extends Activity {

	private TabHost tabHost;
	private String objectId;
	private Number quesId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_whistle);
		Intent intent = getIntent();
		objectId = intent.getStringExtra("iObjectId");
		String quesTxt = intent.getStringExtra("iQuesTxt");
		Integer hitCount = intent.getIntExtra("iHitCount", 0);
		byte[] quesImg = intent.getByteArrayExtra("iQuesImg");
		byte[] userImg = intent.getByteArrayExtra("iUserImg");

		// Show the Up button in the action bar
		setupActionBar();

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

		TextView viewWhistleQuestion = (TextView) findViewById(R.id.view_whistle_question);
		viewWhistleQuestion.setText(quesTxt);

		TextView viewWhistleClikCount = (TextView) findViewById(R.id.view_whistle_clik_count);
		if (hitCount == 1) {
			viewWhistleClikCount.setText("" + hitCount + " clik");
		} else {
			viewWhistleClikCount.setText("" + hitCount + " cliks");
		}

		ImageButton viewWhistleUserImage = (ImageButton) findViewById(R.id.view_whistle_user_image);
		viewWhistleUserImage
				.setImageBitmap(decodeSampledBitmap(userImg, 50, 50));

		ImageView viewWhistleQuestionImage = (ImageView) findViewById(R.id.view_whistle_question_image);
		viewWhistleQuestionImage.setImageBitmap(decodeSampledBitmap(quesImg,
				50, 50));

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
			DialogFragment newFragment = new ReportWhistleFragment();
			newFragment.show(getFragmentManager(), "reportWhistle");
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
		ThisUser myUser = new ThisUser(
				ParseInstallation.getCurrentInstallation());
		Number myUserId = myUser.userId();
		ParseObject voteQuesFlag = new ParseObject("VoteQuesFlag");
		voteQuesFlag.put("flagType", "INA");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("VoteQues");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					quesId = object.getNumber("quesId");
				} else {
				}
			}
		});
		voteQuesFlag.put("quesId", quesId);
		voteQuesFlag.put("userId", myUserId);
	}
	
	public void reportRepetitive(View view) {
		ThisUser myUser = new ThisUser(
				ParseInstallation.getCurrentInstallation());
		Number myUserId = myUser.userId();
		ParseObject voteQuesFlag = new ParseObject("VoteQuesFlag");
		voteQuesFlag.put("flagType", "REP");
		ParseQuery<ParseObject> query = ParseQuery.getQuery("VoteQues");
		query.getInBackground(objectId, new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					quesId = object.getNumber("quesId");
				} else {
				}
			}
		});
		voteQuesFlag.put("quesId", quesId);
		voteQuesFlag.put("userId", myUserId);
	}
}

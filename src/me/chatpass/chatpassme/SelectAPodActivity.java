package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SelectAPodActivity extends Activity {

	private String quesTxt;
	private byte[] quesImg;
	private String ansType;
	private ArrayList<byte[]> ansOptImgArray = new ArrayList<byte[]>();
	private ArrayList<String> ansOptTxtArray = new ArrayList<String>();
	private byte[] iRateOptImg;
	private int ansOptTxtCount;
	private int ansOptImgCount;
	private Number schoolId;
	
	// Temporary
	private Number userId = 257;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_a_pod);

		// Show the Up button in the action bar.
		setupActionBar();

		getWhistleData();
	}

	private void getWhistleData() {
		Intent intent = getIntent();
		quesTxt = intent.getStringExtra("iQuesTxt");
		quesImg = intent.getByteArrayExtra("iQuesImg");
		Log.i("FUCK YOUUUU", quesImg.toString());
		ansType = intent.getStringExtra("iAnsType");
		
		if (ansType.equals("TXT")) {
			ansOptTxtCount = intent.getIntExtra("iAnsOptTxtCount", 0);
			for (int i = 0; i < ansOptTxtCount; i++) {
				ansOptTxtArray.add(intent.getStringExtra("iAnsOptTxt" + i));
			}
		}
		else if (ansType.equals("PHOT")) {
			ansOptImgCount = intent.getIntExtra("iAnsOptImgCount", 0);
			for (int i = 0; i < ansOptImgCount; i++) {
				int count = i + 1;
				ansOptImgArray.add(intent.getByteArrayExtra("iAnsOptImg" + count));
			}
		}
		else if (ansType.equals("RATE")) {
			if (intent.getByteArrayExtra("iRateOptImg") != null) {
				iRateOptImg = intent.getByteArrayExtra("iRateOptImg");
			}
		}
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void selectEveryone(View view) {
		pushToParse("EVRY");
		backToMainActivity();
	}
	
	public void selectSchool(View view) {
		pushToParse("SCHL");
	}

	private void pushToParse(String target) {
		ParseObject voteQues = new ParseObject("VoteQues");
		
		voteQues.put("quesTxt", quesTxt);
		voteQues.put("ansType", ansType);
		voteQues.put("userId", userId);
		
		ParseFile pQuesImg = new ParseFile(quesImg);
		voteQues.put("quesImg", pQuesImg);
		
		ParseQuery<ParseObject> qVoteQues = ParseQuery.getQuery("VoteQues");
		qVoteQues.addDescendingOrder("quesId");
		try {
			Number quesId = qVoteQues.getFirst().getNumber("quesId").intValue() + 1;
			voteQues.put("quesId", quesId);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ParseQuery<ParseObject> qUserSchool = ParseQuery.getQuery("UserSchool");
		qUserSchool.whereEqualTo("userId", userId);
		try {
			schoolId = qUserSchool.getFirst().getNumber("schoolId");
			voteQues.put("schlVal", schoolId);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		for (int i = 1; i <= 4; i++) {
			voteQues.put("ansOptHitcount" + i, 0);
			voteQues.put("frdsAnsOptHitcount" + i, 0);
			voteQues.put("schlAnsOptHitcount" + i, 0);
		}
		
		voteQues.put("hitCount", 0);
		voteQues.put("rateAverage", 0);
		voteQues.put("schlHitCount", 0);
		voteQues.put("schlRateAverage", 0);
		voteQues.put("frndRateAverage", 0);
		voteQues.put("frndsHitcount", 0);

		
		if (ansType.equals("TXT")){
			for(int i = 0; i < ansOptTxtCount; i++) {
				int count = i + 1;
				voteQues.put("ansOptTxt" + count, ansOptTxtArray.get(i));
			}
		}
		if (ansType.equals("PHOT")) {
			for(int i = 0; i < ansOptImgCount; i++) {
				int count = i + 1;
				ParseFile qAnsOptImg = new ParseFile(ansOptImgArray.get(i));
				voteQues.put("ansOptImg" + count, qAnsOptImg);
			}
		}
		if (ansType.equals("RATE")) {
			ParseFile qRateOptImg = new ParseFile(iRateOptImg);
			voteQues.put("rateOptImg", qRateOptImg);
		}
		
		if (target.equals("EVRY")) {
			voteQues.put("target", target);
			voteQues.put("schlOption", "");
		}
		if (target.equals("SCHL")) {
			voteQues.put("target", target);
			voteQues.put("schlOption", "SCHLNAME");

		}
		
		voteQues.saveInBackground();
	}

	public void backToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
}

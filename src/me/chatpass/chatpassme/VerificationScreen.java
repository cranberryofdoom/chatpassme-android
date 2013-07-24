package me.chatpass.chatpassme;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class VerificationScreen extends Activity {

	private final String TAG = "Verification Screen";
	public Number numUsers = 0;
	String userId = "";
	public Number newId;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_verification_screen);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		setTitle("Chatpassme");
	}  

	/**
	 * @param view
	 */
	public void moveToCreateProfilePage (View view){
		Intent intent = new Intent(this,CreateProfile.class);

		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");

		// Retrieve the most recent ones
		qUsers.orderByDescending("createdAt");
		qUsers.setSkip(1);
		
		qUsers.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject biggestUser, ParseException e) {

				//get most recent's userId and increment by one
				newId = biggestUser.getNumber("userId").intValue() + 1;
				
				//get current telephone number using id, store as string 
				ParseInstallation id = ParseInstallation.getCurrentInstallation();
				String myNumber = id.getString("phoneNumber");

				// Create a Parse object query and ask for the Users class
				ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");

				// Ask for the specific phone number that is correlated to the current user
				qUsers.whereEqualTo("phoneNumber", myNumber);

				// Find the first object that has the current user's telephone number
				try {
					//place and save userId in Users Class
					ParseObject mostRecent = qUsers.getFirst();
					mostRecent.put("userId", newId);
					mostRecent.saveInBackground();

					//place and save userId in UserSchool Class
					ParseObject newUserParseObject = new ParseObject("UserSchool");
					newUserParseObject.put("userId",newId);
					newUserParseObject.saveInBackground();
					
					//place and save userId in UserSchool Class
					ParseObject newUserParseObject2 = new ParseObject("UserGradeNew");
					newUserParseObject2.put("userId",newId);
					newUserParseObject2.saveInBackground();
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		});  	
		//move onto next page
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_verification_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.no_code:
			Log.i(TAG, "NoCode Item Clicked");
			return true;
		}	
		return super.onOptionsItemSelected(item);
	}
}

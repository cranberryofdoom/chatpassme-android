package me.chatpass.chatpassme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PhoneNumberScreen extends Activity {
	private ThisUser user;
	private String phoneNum;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_number_screen);
		// Show the Up button in the action bar.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_phone_number_screen, menu);
		return true;
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

	public void moveOntoVerificationScreen(View view) {
		EditText editText = (EditText) findViewById(R.id.editText2);
		phoneNum = editText.getText().toString();
		phoneNum = "1" + phoneNum;

		if (phoneNum.length() != 11) {
			Toast.makeText(getApplicationContext(),
					"Please enter a valid phone number", Toast.LENGTH_SHORT)
					.show();
		} else {
			Intent intent = new Intent(this, VerificationScreen.class);
			final ParseInstallation id = ParseInstallation
					.getCurrentInstallation();
			
			// Add or update phone number to "Installation" class in Parse
			id.put("phoneNumber", phoneNum);
			id.saveInBackground();
			
			// If user does not exist create a new user
			user = new ThisUser(id);
			ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
			qUsers.whereEqualTo("phoneNumber", user.phoneNumber());
			qUsers.getFirstInBackground(new GetCallback<ParseObject>(){

				@Override
				public void done(ParseObject object, ParseException e) {
					if (object == null){
						ParseObject Users = new ParseObject("Users");
						Users.put("phoneNumber", phoneNum);
						Users.saveInBackground();
					}
				}
			});

			// Move onto verification screen
			startActivity(intent);
		}
	}
}

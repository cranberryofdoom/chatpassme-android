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

import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class PhoneNumberScreen extends Activity {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_phone_number_screen);
		// Show the Up button in the action bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Chatpassme");
        
        
//        this was supposed to make keyboard appear when user clicks on Edit text box
//        final EditText phoneNumber = (EditText) findViewById(R.id.editText2);
//        phoneNumber.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(phoneNumber, 0);
//            }
//        });   
//        
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_phone_number_screen, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void moveOntoVerificationScreen(View view) {
		EditText editText = (EditText) findViewById(R.id.editText2);
		String phoneNum = editText.getText().toString();
		phoneNum = "1" + phoneNum;


		if(phoneNum.length() != 11) {
			//  call dialog fragment say that invalid
			Toast.makeText(getApplicationContext(),"Please enter a valid phone number",Toast.LENGTH_SHORT).show();
		}
		else {
			Intent intent = new Intent(this, VerificationScreen.class);
			ParseObject Users = new ParseObject("Users");
			Users.put("phoneNumber",phoneNum);
			Users.saveInBackground();
			
			//add (or update if user reinstalls) phone number and user ID to "Installation" class in Parse
			final ParseInstallation id = ParseInstallation.getCurrentInstallation();
			id.put("phoneNumber",phoneNum);
			id.saveInBackground();
			
			//move onto verification screen
			startActivity(intent);

		}
		
		//get phone number input by user
		
		
		//add phone number to "Users" class in Parse
		
	}

}

package me.chatpass.chatpassme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Parse.initialize(this, "N1j4C4EpAYnq44vlpbZnwK0BIdBGAAgPDnyQMZsu",
				"WdjH2YPmpF14Bo3N0uTpGasp7upf7jn6NIpFDyqW");
		setContentView(R.layout.activity_sign_up);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sign_up, menu);
		return true;
	}

	public void moveOntoNumberScreen(View view) {
		Intent intent = new Intent(this, PhoneNumberScreen.class);
		final ParseInstallation id = ParseInstallation.getCurrentInstallation();
		id.saveInBackground();
		startActivity(intent);
	}

}

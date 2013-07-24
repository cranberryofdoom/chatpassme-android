package me.chatpass.chatpassme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class DecisionActivity extends Activity {
	private ThisUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_decision);

		// Initialize Parse
		Parse.initialize(this, "N1j4C4EpAYnq44vlpbZnwK0BIdBGAAgPDnyQMZsu",
				"WdjH2YPmpF14Bo3N0uTpGasp7upf7jn6NIpFDyqW");

		// Check whether the user actually exists
		user = new ThisUser(ParseInstallation.getCurrentInstallation());
		if (user.exists()) {
			Intent intent = new Intent(DecisionActivity.this,
					MainActivity.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(DecisionActivity.this,
					SignUpActivity.class);
			startActivity(intent);
		}
	}
}

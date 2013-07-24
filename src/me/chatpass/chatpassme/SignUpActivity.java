package me.chatpass.chatpassme;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	// From the "Create an Account" button
	public void moveOntoNumberScreen(View view) {
		Intent intent = new Intent(this, PhoneNumberScreen.class);

		// Push the installation id of this device into Parse
		final ParseInstallation id = ParseInstallation.getCurrentInstallation();
		id.saveInBackground();

		startActivity(intent);
	}
}

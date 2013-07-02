package me.chatpass.chatpassme;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize Parse
		Parse.initialize(this, "N1j4C4EpAYnq44vlpbZnwK0BIdBGAAgPDnyQMZsu",
				"WdjH2YPmpF14Bo3N0uTpGasp7upf7jn6NIpFDyqW");
		ParseAnalytics.trackAppOpened(getIntent());
		
		// Get Installation Id
		final ParseInstallation id = ParseInstallation.getCurrentInstallation();		
		id.put("phoneNumber", "jgkfdljgkld");
		id.saveInBackground();
		
		// Set the view to activity_main.xml
		setContentView(R.layout.activity_main);

		// Immediately load the home fragment
		FragmentTransaction t = getFragmentManager().beginTransaction();
		HomeFragment homeF = new HomeFragment();
		t.add(R.id.fragment_content, homeF);
		t.commit();
	}

	// Load the home fragment if the home button is pressed
	public void loadHomeFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		HomeFragment homeF = new HomeFragment();
		t.replace(R.id.fragment_content, homeF);
		t.commit();
	}

	// Load the contacts fragment if the contacts button is pressed
	public void loadContactsFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ContactsFragment contactsF = new ContactsFragment();
		t.replace(R.id.fragment_content, contactsF);
		t.commit();
	}

	// Load the create fragment if the create button is pressed
	public void loadCreateFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		CreateFragment createF = new CreateFragment();
		t.replace(R.id.fragment_content, createF);
		t.commit();
	}

	// load the convos fragment if the convos button is pressed
	public void loadConvosFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ConvosFragment convosF = new ConvosFragment();
		t.replace(R.id.fragment_content, convosF);
		t.commit();
	}

	// load the profile fragment if the profile button is pressed
	public void loadProfileFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ProfileFragment profileF = new ProfileFragment();
		t.replace(R.id.fragment_content, profileF);
		t.commit();
	}
}

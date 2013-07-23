package me.chatpass.chatpassme;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

public class MainActivity extends Activity {

	private ImageView imageTracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get Installation Id
		final ParseInstallation id = ParseInstallation.getCurrentInstallation();

		// Set the view
		setContentView(R.layout.activity_main);
		
		// Set action bar color
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4B8260")));

		// Immediately load the home fragment
		FragmentTransaction t = getFragmentManager().beginTransaction();
		HomeFragment createF = new HomeFragment();
		t.add(R.id.fragment_content, createF);
		imageTracker = (ImageButton) findViewById(R.id.home);
		imageTracker.setEnabled(false);
		imageTracker.setBackgroundColor(Color.parseColor("#C0432A"));
		t.commit();
	}

	// Set the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// Set the menu click listeners
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Load the home fragment if the home button is pressed
	public void loadHomeFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		HomeFragment homeF = new HomeFragment();
		t.replace(R.id.fragment_content, homeF);
		switchEnabled(imageTracker, R.id.home);
		t.commit();
	}

	// Load the contacts fragment if the contacts button is pressed
	public void loadContactsFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ContactsFragment contactsF = new ContactsFragment();
		t.replace(R.id.fragment_content, contactsF);
		switchEnabled(imageTracker, R.id.contacts);
		t.commit();
	}

	// Load the create fragment if the create button is pressed
	public void loadCreateFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		CreateFragment createF = new CreateFragment();
		t.replace(R.id.fragment_content, createF);
		switchEnabled(imageTracker, R.id.create);
		t.commit();
	}

	// Load the profile fragment if the profile button is pressed
	public void loadProfileFragment(View view) {
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ProfileFragment profileF = new ProfileFragment();
		t.replace(R.id.fragment_content, profileF);
		switchEnabled(imageTracker, R.id.profile);
		t.commit();
	}
	
	private void switchEnabled(ImageView tracker, int id) {
		tracker.setEnabled(true);
		tracker.setBackgroundColor(Color.parseColor("#494949"));
		imageTracker = (ImageButton) findViewById(id);
		tracker = imageTracker;
		tracker.setEnabled(false);
		tracker.setBackgroundColor(Color.parseColor("#C0432A"));
	}
}

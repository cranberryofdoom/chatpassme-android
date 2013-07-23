package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.telephony.SmsManager;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddContactsActivity extends Activity {
	private ListView listView;
	private ArrayList<String> phoneNumberArray = new ArrayList<String>();
	private ArrayList<String> fullNameArray = new ArrayList<String>();
	private String messageString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contacts);
		
		listView = (ListView) findViewById(R.id.add_contacts_list_view);

		setupActionBar();
		
		messageString = "DOWNLOAD CHATPASSME TODAY. YOU IMBCILE.";
		
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneNumber = phoneNumber.replaceAll("[\\D]", "");
			phoneNumberArray.add(phoneNumber);
		}

		Cursor names = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while (names.moveToNext()) {
			String fullName = names.getString(names
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			fullNameArray.add(fullName);
		}

		// Create a new adapter for contacts
		ArrayAdapter<String> contactsAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice,
				fullNameArray);

		// Set the adapter
		listView.setAdapter(contactsAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contacts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.send_invitation:
			SmsManager smsManager = SmsManager.getDefault();
			SparseBooleanArray positions = listView.getCheckedItemPositions();
			for (int i = 0; i < positions.size(); i++) {
				if (positions.get(i))
					smsManager.sendTextMessage(
							phoneNumberArray.get(positions.indexOfKey(i)),
							null, messageString, null, null);
			}
			Toast.makeText(getApplicationContext(), "Invitation Sent!",
					Toast.LENGTH_LONG).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}

package me.chatpass.chatpassme;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ContactPage extends Activity {
	private ArrayList<String> phoneNumberArray = new ArrayList<String>();
	private ArrayList<String> contactsNamesArray = new ArrayList<String>();
	private ListView listview;

	private final String TAG = "Contact Page";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_contact_page);
		// Show the Up button in the action bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }	
        setTitle("Chatpassme");
        
        //define listview
        listview = (ListView) findViewById(R.id.contactsView);
        
        //access the contacts
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);
    	
        //go through all phone numbers
        while(phones.moveToNext()) {
        	//add each phone number to phone number Array
        	String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        	phoneNumber = phoneNumber.replaceAll("[\\D]","");
        	phoneNumberArray.add(phoneNumber);	
        }
        //access names in the contacts
        Cursor names = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null,null,null,null);
        //go through all names, adding them to names array
        while(names.moveToNext()) {
        	String name = names.getString(names.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        	contactsNamesArray.add(name);	
        }
        
        //create a new array adapter
        ArrayAdapter<String> contactsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,contactsNamesArray);
        
        //set the adapter
        listview.setAdapter(contactsAdapter);
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);
    }
	
	public void moveToSchoolsPage() {
		Intent intent = new Intent(this,SchoolsPage.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_contact_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

			case R.id.add_contacts:
				Log.i(TAG,"Add Contacts Clicked");
				return true;
				
			case R.id.skip_add_contacts:
				Log.i(TAG, "Skip Add Contacts Clicked");
				moveToSchoolsPage();
				return true;
				
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
			
			default:
				return super.onOptionsItemSelected(item);
		}
	}	
}

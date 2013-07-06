package me.chatpass.chatpassme;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ThisUser {

	private Number myUserId;
	private String phoneNumber;

	// Constructor
	public ThisUser(final ParseInstallation id) {

		// Get user's phoneNumber
		phoneNumber = id.getString("phoneNumber");

		// Create a Parse object query and ask for the Users class
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");

		// Ask for the specific phone number that is correlated to the user
		qUsers.whereEqualTo("phoneNumber", phoneNumber);

		// Find the first object that matches that shows up
		qUsers.getFirstInBackground(new GetCallback<ParseObject>() {
			public void done(ParseObject object, ParseException e) {
				if (e == null) {

					// Get user's user id from that object
					myUserId = id.getNumber("userId");
				}
			}
		});	
	}
	
	public Number userId() {
		return myUserId;
	}

}

package me.chatpass.chatpassme;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ThisUser {

	private Number myUserId;
	private String phoneNumber;

	// Constructor
	public ThisUser(final ParseInstallation id) {

		// Get this user's phone number
		phoneNumber = id.getString("phoneNumber");

		// Create a ParseObject query and ask for the Users class
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");

		// Ask for the specific phone number that is correlated to the user
		qUsers.whereEqualTo("phoneNumber", phoneNumber);

		// Get this user's id
		try {
			myUserId = qUsers.getFirst().getNumber("userId");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Number userId() {
		return myUserId;
	}

}

package me.chatpass.chatpassme;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ThisUser {
	private Number myUserId;
	private String phoneNumber;
	private ParseObject myUserParseObject;
	private Number mySchoolId;

	// Constructor
	public ThisUser(final ParseInstallation id) {
		phoneNumber = id.getString("phoneNumber");
		Log.i("THIS IS MY PHONE NUMBER AHHH", "" + phoneNumber);

		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		qUsers.whereEqualTo("phoneNumber", phoneNumber);
		try {
			myUserParseObject = qUsers.getFirst();
		} catch (ParseException e) {
		}

	}

	public boolean exists() {
		if (phoneNumber == null) {
			return false;
		}
		if (myUserParseObject == null) {
			return false;
		}
		if (myUserParseObject.getNumber("userId") == null) {
			return false;
		}
		return true;
	}

	public String phoneNumber() {
		return phoneNumber;
	}

	public Number id() {
		myUserId = myUserParseObject.getNumber("userId");
		return myUserId;
	}

	public ParseObject parseObject() {
		return myUserParseObject;
	}

	public Number schoolId() {
		ParseQuery<ParseObject> qUserSchool = ParseQuery.getQuery("UserSchool");
		qUserSchool.whereEqualTo("userId", myUserId);
		qUserSchool.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					mySchoolId = object.getNumber("schoolId");
				}
			}
		});
		return mySchoolId;
	}

}

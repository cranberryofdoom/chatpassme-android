package me.chatpass.chatpassme;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ConfirmGradeDialogFragment extends DialogFragment {
	private int newUserGradeNewId;
	private Integer userGradeInt;

	static ConfirmGradeDialogFragment newInstance() {
		return new ConfirmGradeDialogFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		
		// Use the Builder class for convenient dialog construction
		
		//get user grade from bunder and set to string
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String userGrade = getArguments().getString("userGrade");
		
		//get only the digits from the grade and make equal to new variable UserGradeInt
		if (userGrade.length() == 3) {
			userGrade = userGrade.substring(0,1);
		} else {
			userGrade = userGrade.substring(0,2);
		}
		userGradeInt = Integer.parseInt(userGrade);
		updateGradeInParse(userGradeInt);
		builder.setMessage("Confirm that you are in " + userGrade + "th grade.")
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(getActivity(),
										MainActivity.class);
								startActivity(intent);
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismiss();
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}
	
	private void updateGradeInParse(final int userGradeInt) {
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("UserGradeNew");

		// Retrieve the 2nd most recent user, which has most recent userGradeNewId
		qUsers.orderByDescending("createdAt");
		qUsers.setLimit(2);
		
        qUsers.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				ParseObject mostRecent = objects.get(0);
				ParseObject secondMostRecent = objects.get(1);
				newUserGradeNewId = secondMostRecent.getNumber("userGradeNewId").intValue() + 1;
				mostRecent.put("userGradeNewId", newUserGradeNewId);
				mostRecent.put("gradeId", userGradeInt);
				mostRecent.saveInBackground();
			}
		});
	}
}


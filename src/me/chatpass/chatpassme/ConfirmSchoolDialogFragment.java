package me.chatpass.chatpassme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ConfirmSchoolDialogFragment extends DialogFragment{
	private String schoolName;
	private Integer userSchoolId;

	static ConfirmSchoolDialogFragment newInstance() {
		return new ConfirmSchoolDialogFragment();
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		schoolName = getArguments().getString("iSchoolName");
		getUserSchoolId(schoolName);
		builder.setMessage("Confirm that you are in " + schoolName)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								Intent intent = new Intent(getActivity(),
										ChooseGrade.class);
								updateParseWithUserSchool(schoolName);
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
	
	private void getUserSchoolId(String schoolName) {
		String userSchool = schoolName;
		
		//query School class
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("School");
		
		//find row where school is equal to user school
		qUsers.whereEqualTo("name", userSchool);
		
		// Retrieve the most recent user, which has most recent userGradeNewId
		qUsers.getFirstInBackground(new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
				  //create new string containing schoolId
				  userSchoolId = object.getInt("schoolId");	
				  Log.i("schoolId",userSchoolId.toString());
			}
		});
	}	

	private void updateParseWithUserSchool(String schoolName){
				
		//query UserGradeNew class
		ParseQuery<ParseObject> query = ParseQuery.getQuery("UserGradeNew");
		query.orderByDescending("createdAt");
		query.setLimit(1);
		
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject myParseObject, ParseException e2) {
				// TODO Auto-generated method stub
				myParseObject.put("schoolId", userSchoolId);
				myParseObject.saveInBackground();
			}
		});
	}

}

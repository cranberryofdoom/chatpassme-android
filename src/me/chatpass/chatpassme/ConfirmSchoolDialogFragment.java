package me.chatpass.chatpassme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ConfirmSchoolDialogFragment extends DialogFragment{
	static ConfirmSchoolDialogFragment newInstance() {
		return new ConfirmSchoolDialogFragment();
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Confirm that you are in xxx high school")
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent intent = new Intent(getActivity(),
										ChooseGrade.class);
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
}

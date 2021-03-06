package me.chatpass.chatpassme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class ReportWhistleDialogFragment extends DialogFragment {
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		View view = inflater.inflate(R.layout.dialog_report_whistle, null);
		builder.setView(view);

		builder.setMessage(R.string.report_whistle).setNegativeButton(
				R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// User cancelled the dialog
						dismiss();
					}
				});
		
		// Create the AlertDialog object and return it
		return builder.create();
	}
}

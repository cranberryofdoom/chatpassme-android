package me.chatpass.chatpassme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class WhistleTextWarningDialogFragment extends DialogFragment {
	 @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(R.string.whistle_text_warning)
	               .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   dismiss();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
}

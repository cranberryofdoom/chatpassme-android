package me.chatpass.chatpassme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class ChooseProfilePictureDialogFragment extends DialogFragment {
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    builder.setView(inflater.inflate(R.layout.dialog_choose_profile_picture, null));
		
		builder.setMessage(R.string.choose_image).setNegativeButton(
				R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});

		return builder.create();
	}
}

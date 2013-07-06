package me.chatpass.chatpassme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

public class ReportWhistleFragment extends DialogFragment {

//	public interface ReportWhistleListener {
//		public void reportInappropriate(DialogFragment dialog);
//		public void reportRepetitive(DialogFragment dialog);
//	}
//	
//	 // Use this instance of the interface to deliver action events
//    ReportWhistleListener mListener;
//    
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        
//        // Verify that the host activity implements the callback interface
//        try {
//        	
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (ReportWhistleListener) activity;
//        } catch (ClassCastException e) {
//        	
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement ReportWhistleListener");
//        }
//    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.dialog_report_whistle, null));

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

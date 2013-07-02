package me.chatpass.chatpassme;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CreateFragment extends Fragment {
	
	private TabHost tabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_create,
				container, false);

		tabHost = (TabHost) view.findViewById(R.id.tabhost);
		tabHost.setup();

		TabSpec spec1 = tabHost.newTabSpec("Text");
		spec1.setContent(R.id.text);
		spec1.setIndicator("Text");

		TabSpec spec2 = tabHost.newTabSpec("Photo");
		spec2.setIndicator("Photo");
		spec2.setContent(R.id.photo);

		TabSpec spec3 = tabHost.newTabSpec("Rating");
		spec3.setContent(R.id.rating);
		spec3.setIndicator("Rating");

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		
		return view;

	}
	
	public void chooseImage(View view){
		DialogFragment newFragment = new ChooseImageDialogFragment();
	    newFragment.show(getFragmentManager(), "createImage");
	}
	
}

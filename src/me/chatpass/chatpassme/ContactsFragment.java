package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ContactsFragment extends Fragment {

	private GridView gridView;

	private ArrayList<String> phoneNumberArray = new ArrayList<String>();
	private List<ParseObject> mObjects;

	private GetPlaceholder placeholder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_contacts, container,
				false);

		setHasOptionsMenu(true);

		gridView = (GridView) view.findViewById(R.id.contacts_grid_view);

		placeholder = new GetPlaceholder(getActivity());

		return view;
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.invite_contacts, menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.invite_contacts:
			Intent intent = new Intent(getActivity(), AddContactsActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Cursor phones = getActivity().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);
		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			phoneNumber = phoneNumber.replaceAll("[\\D]", "");
			phoneNumberArray.add(phoneNumber);
		}
		phones.close();

		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		for (int i = 0; i < phoneNumberArray.size(); i++) {
			queries.add(ParseQuery.getQuery("Users").whereEqualTo(
					"phoneNumber", phoneNumberArray.get(i)));
		}
		ParseQuery<ParseObject> qContacts = ParseQuery.or(queries);
		qContacts.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					mObjects = objects;

					ContactsGridAdapter gridAdapter = new ContactsGridAdapter(
							getActivity(), mObjects);

					gridView.setAdapter(gridAdapter);

					gridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v,
								int position, long id) {
							final Intent intent = new Intent(getActivity(),
									ViewOtherProfileActivity.class);
							final Bundle b = new Bundle();

							b.putInt("iUserId", mObjects.get(position)
									.getNumber("userId").intValue());

							ParseFile pUserImage = mObjects.get(position)
									.getParseFile("imageFile");
							if (pUserImage == null) {
								b.putByteArray("iUserImg",
										placeholder.getByteArray());
							} else {
								pUserImage
										.getDataInBackground(new GetDataCallback() {

											@Override
											public void done(byte[] data,
													ParseException e) {
												b.putByteArray("iUserImg", data);
												intent.putExtras(b);
												startActivity(intent);
											}
										});
							}
						}
					});
				}
			}
		});
	}
}

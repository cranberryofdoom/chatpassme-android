package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ContactsFragment extends Fragment {

	private GridView gridView;

	private ArrayList<String> phoneNumberArray = new ArrayList<String>();
	private ArrayList<String> userFirstName = new ArrayList<String>();
	private ArrayList<Bitmap> userImg = new ArrayList<Bitmap>();
	private ArrayList<Number> userId = new ArrayList<Number>();
	private ArrayList<byte[]> dUserImg = new ArrayList<byte[]>();

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();
	
	private int dimensions;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_contacts, container,
				false);

		setHasOptionsMenu(true);
		
		gridView = (GridView) view.findViewById(R.id.contacts_grid_view);

		// Inflate the layout for this fragment
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

		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		qUsers.setLimit(1000);
		qUsers.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					for (int i = 0; i < phoneNumberArray.size(); i++) {
						for (int j = 0; j < objects.size(); j++) {
							if (objects.get(j).getString("phoneNumber")
									.equals(phoneNumberArray.get(i))) {
								userFirstName.add(objects.get(j).getString(
										"firstName"));
								userId.add(objects.get(j).getNumber("userId"));
								
								ParseFile imageFile = objects.get(j)
										.getParseFile("imageFile");
								try {
									byte[] dImageFile = imageFile.getData();
									dUserImg.add(dImageFile);
									userImg.add(decode.decodeSampledBitmap(
											dImageFile, 100,
											100));
									DisplayMetrics metrics = new DisplayMetrics();
									getActivity().getWindowManager()
											.getDefaultDisplay()
											.getMetrics(metrics);
									dimensions = metrics.widthPixels/4;
								} catch (ParseException e1) {
									e1.printStackTrace();
								}
								break;
							}
						}
					}

					// Gets a CursorAdapter
					ContactsGridAdapter gridAdapter = new ContactsGridAdapter(
							getActivity(), userFirstName, userImg, dimensions);

					// Sets the adapter for the GridView
					gridView.setAdapter(gridAdapter);
					
					gridView.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
							Intent intent = new Intent(getActivity(), ViewOtherProfileActivity.class);
							intent.putExtra("iUserId", userId.get(position).intValue());
							intent.putExtra("iUserImg", dUserImg.get(position));
							startActivity(intent);
						}
					});
				}
			}
		});
	}
}

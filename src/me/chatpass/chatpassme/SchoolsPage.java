package me.chatpass.chatpassme;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class SchoolsPage extends Activity {
	@SuppressLint("NewApi")
	
	private ConfirmSchoolDialogFragment confirmSchool = new ConfirmSchoolDialogFragment();
	ListView myListView;
	ArrayList<String>  schoolsArrayList = new ArrayList<String>();
	ParseGeoPoint userLoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_schools_page);
		// Show the Up button in the action bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }	
        setTitle("CHOOSE YOUR SCHOOL");
        myListView = (ListView) findViewById(R.id.schoolsListView);
    	findUserLocation();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_schools_page, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void findUserLocation() {			
		
		
		//This is for using on a real device
//		LocationResult locationResult = new LocationResult(){
//		    @Override
//		    public void gotLocation(Location location){
//		        lat = location.getLatitude();
//		        longi = location.getLongitude();
//		        Log.i("d",lat + "");
//		        Log.i("de",longi + "");
//		    }
//		};
//		MyLocation myLocation = new MyLocation();
//		myLocation.getLocation(this, locationResult);
//        Log.i("d",lat + "");
//        Log.i("de",longi + "");
	
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,ll);
	}
	
	public void findClosestSchools(ParseGeoPoint userLocation) {
		if (userLocation == null) {
		}
 		ParseQuery<ParseObject> query = ParseQuery.getQuery("School");
		query.whereNear("location", userLocation);
        query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> closeSchools, ParseException e) {
				
				//create a new array adapter
		        SchoolListAdapter schoolAdapter = new SchoolListAdapter(SchoolsPage.this, closeSchools);
				
		        //set the adapter
		        myListView.setAdapter(schoolAdapter);
		        
		        myListView.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						confirmSchool.show(getFragmentManager(),"confirmSchool");
						
//						Intent intent = new Intent(SchoolsPage.this, ConfirmAge.class);
//						intent.putExtra("iSchoolName", closeSchools.get(position).getString("name"));
//						startActivity(intent);
					}
		        });
			}
		});
	}


	public class mylocationlistener implements LocationListener {
		//public ParseGeoPoint userLoc;

		@Override
		public void onLocationChanged(Location location) {
			
			//Log.i("dsad","DOES THIS EVEN RUN ANYMORE WTDFFDNSKLJNFDS");
			// get and set current user location
			if (location != null) {
				double plong = location.getLongitude();
				double plat = location.getLatitude();
				Log.i("long",plong + "");
				Log.i("lat",plat + "");
			
				//create new geopoint for user location
				userLoc = new ParseGeoPoint(plat,plong);
				findClosestSchools(userLoc);
				Log.i("userLoc",userLoc.getLatitude() + "");
				
			} else {
				Log.i("fail","location is null");
			}
		}
	
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
	
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
	}
}
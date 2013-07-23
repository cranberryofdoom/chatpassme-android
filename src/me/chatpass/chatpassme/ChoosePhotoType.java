package me.chatpass.chatpassme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class ChoosePhotoType extends Activity {
	
	ImageView iv;
	
  public final static String DEBUG_TAG = "ChoosePhotoType";


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_choose_photo_type);
		
		//assigns imageView in the xml to the variable iv
		iv=(ImageView) findViewById(R.id.imageView1);
		
		// Show the Up button in the action bar.	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }	
        
        setTitle("Chatpassme");
        
        //assigns "take a new photo" button to btn
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// opens the camera when user clicks on "take a new photo" button
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 0);
			}
        });
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//retrieves bitmap image and then sets the image view to that bitmap
		Log.i("dis", "dis executed");
		
		if (requestCode == 0) {
			Bitmap myImage = (Bitmap) data.getExtras().get("data");
			iv.setImageBitmap(myImage);
		}
	}
//	
//	public void moveToProfilePage (View view){
//		Intent intent = new Intent(this,CreateProfile.class);
//		startActivity(intent);
//	}	
		  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_photo_type, menu);
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

}

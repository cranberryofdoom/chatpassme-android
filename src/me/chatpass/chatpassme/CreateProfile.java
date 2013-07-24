package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class CreateProfile extends Activity {

	public String firstName;
	public String lastName;
	private DialogFragment cameraDialog;
	private Bitmap myImage;
	private byte[] photoByteArray;
	private ParseFile myProfilePictureFile;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_profile);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void inviteContactsPage(View view) {
		Intent intent = new Intent(this, ContactPage.class);

		// retrieve first name from profile page
		EditText editText = (EditText) findViewById(R.id.editText1);
		final String firstName = editText.getText().toString();

		// retrieve last name from profile page
		EditText editText2 = (EditText) findViewById(R.id.editText2);
		final String lastName = editText2.getText().toString();

		// get user birthday, and break into day,month,year
		EditText month = (EditText) findViewById(R.id.editText3);
		EditText day = (EditText) findViewById(R.id.editText4);
		EditText year = (EditText) findViewById(R.id.editText5);

		Integer birthdayMonth = Integer.parseInt(month.getText().toString());
		Integer birthdayDay = Integer.parseInt(day.getText().toString());
		Integer birthdayYear = Integer.parseInt(year.getText().toString());
		Log.i("bdayyr", birthdayYear.toString());
		Log.i("bdaymonth", birthdayMonth.toString());
		Log.i("bdayday", birthdayDay.toString());

		// get current time
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		Integer currentDay = Integer.parseInt(timeStamp.substring(6, 8));
		Integer currentMonth = Integer.parseInt(timeStamp.substring(4, 6));
		Integer currentYear = Integer.parseInt(timeStamp.substring(0, 4));
		Log.i("currentyr", currentYear.toString());
		Log.i("currentmonth", currentMonth.toString());
		Log.i("currentday", currentDay.toString());

		// check if they are 13, if not, send them to empty screen
		if (birthdayYear + 13 > currentYear) {
			Log.i("it can do math", "it can do math");
			Intent intent2 = new Intent(this, TrollPage.class);
			startActivity(intent2);
		}
		if (birthdayYear + 13 == currentYear) {
			Log.i("sdasdas", "WOOooOOOOoooo");
			if (birthdayMonth > currentMonth) {
				Log.i("iuoiuo", "HOWDYHOWDYHOWDY");
				Intent intent3 = new Intent(this, TrollPage.class);
				startActivity(intent3);
			}
		}
		if (birthdayYear + 13 == currentYear) {
			Log.i("dasdasd", "YOYOYOYOYOY");
			if (birthdayMonth == currentMonth) {
				Log.i("dsadas", "WXXXXXX");
				if (birthdayDay > currentDay) {
					Log.i("dgfdgd", "CLOSE");
					Intent intent4 = new Intent(this, TrollPage.class);
					startActivity(intent4);
				}
			}
		}

		if (myImage == null || firstName.length() == 0
				|| lastName.length() == 0) {
			Toast.makeText(getApplicationContext(),
					"Please fill in all of the information", Toast.LENGTH_LONG)
					.show();
		//move onto next screen
		} else {
			// convert Bitmap myImage into byte array
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			myImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			photoByteArray = stream.toByteArray();

			// convert byte array into a ParseFile
			String timeStamp2 = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(Calendar.getInstance().getTime());
			myProfilePictureFile = new ParseFile(
					"TList" + timeStamp2 + ".jpeg", photoByteArray);

			final ParseInstallation id = ParseInstallation
					.getCurrentInstallation();
			ThisUser user = new ThisUser(id);
			user.parseObject().put("firstName", firstName);
			user.parseObject().put("lastName", lastName);
			user.parseObject().put("imageFile", myProfilePictureFile);
			user.parseObject().saveInBackground();

			// move onto contacts page
			startActivity(intent);
		}
	}

	public void takeProfilePicture(View view) {
		cameraDialog = new CameraDialog();
		cameraDialog.show(getFragmentManager(), "cameraDialog");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_create_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void goToCamera(View view) {
		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, 0);

	}

	public void goToPhotos(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, 1);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// request code is 0 if user took a new photo
		if (requestCode == 0) {
			// retrieves bitmap image and then sets the image view to that
			// bitmap

			// checks to see whether user canceled photo
			if (resultCode == 0) {
				cameraDialog.dismiss();
			}

			// checks to see whether user took a photo, and then assigns photo
			// to user image
			if (requestCode == 0 && resultCode == -1) {
				myImage = (Bitmap) data.getExtras().get("data");
				ImageButton userImage = (ImageButton) findViewById(R.id.create_profile_user_image);
				userImage.setImageBitmap(myImage);
			}
			// dismisses popup window
			cameraDialog.dismiss();
		}
		// request Code is 1 if user chose existing photo
		if (requestCode == 1) {

			if (resultCode == 0) {
				cameraDialog.dismiss();
			}
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == RESULT_OK) {
				Uri chosenImageUri = data.getData();
				try {
					myImage = Media.getBitmap(this.getContentResolver(),
							chosenImageUri);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ImageButton userImage = (ImageButton) findViewById(R.id.create_profile_user_image);
				userImage.setImageBitmap(myImage);
				cameraDialog.dismiss();
			}
		}

	}

}

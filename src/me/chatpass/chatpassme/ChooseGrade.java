package me.chatpass.chatpassme;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChooseGrade extends Activity {
	@SuppressLint("NewApi")

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_choose_grade);
		// Show the Up button in the action bar.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);	
       }	
       setTitle("SELECT YOUR GRADE");
       
   
    }
	
	public void moveToConfirmGradePage(View view) {
			switch(view.getId())
			{
			//determines which grade user chose, moves to next page
			//keeps their grade choice saved in the intent
			case R.id.button1:
				Intent intent = new Intent (this, ConfirmGrade.class);
				intent.putExtra("Grade", "12th");
				startActivity(intent);
			// handle button A click;
			break;
			
			case R.id.button2:
				Intent intent2 = new Intent (this, ConfirmGrade.class);
				intent2.putExtra("Grade", "9th");
				startActivity(intent2);			
			break;
			
			case R.id.button3:
				Intent intent3 = new Intent (this, ConfirmGrade.class);
				intent3.putExtra("Grade", "10th");
				startActivity(intent3);			
			break;
			
			case R.id.button4:
				Intent intent4 = new Intent (this, ConfirmGrade.class);
				intent4.putExtra("Grade", "11th");
				startActivity(intent4);			
			break;
			
			default:
			throw new RuntimeException("Unknow button ID");
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_choose_grade, menu);
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

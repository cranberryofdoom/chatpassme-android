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
	private ConfirmGradeDialogFragment confirmGrade = new ConfirmGradeDialogFragment();

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
		switch (view.getId()) {
		// determines which grade user chose, moves to next page
		// keeps their grade choice saved in the intent
		case R.id.button1:
			confirmGrade.show(getFragmentManager(), "confirmGrade");
			break;

		case R.id.button2:
			confirmGrade.show(getFragmentManager(), "confirmGrade");
			break;

		case R.id.button3:
			confirmGrade.show(getFragmentManager(), "confirmGrade");
			break;

		case R.id.button4:
			confirmGrade.show(getFragmentManager(), "confirmGrade");
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

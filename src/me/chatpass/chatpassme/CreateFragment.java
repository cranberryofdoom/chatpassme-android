package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import me.chatpass.chatpassme.ChooseImageDialogFragment.ChooseImageDialogFragmentListener;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CreateFragment extends Fragment implements
		ChooseImageDialogFragmentListener {

	private TabHost tabHost;
	private Bitmap mWhistleImage;
	private Bitmap[] mPhotoAnswerImages = new Bitmap[4];
	private Bitmap mRatingImage;
	private ArrayList<String> textAnswers = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_create, container, false);

		setTabs(view);

		setHasOptionsMenu(true);

		setButtonListeners(view);

		return view;
	}

	private void setTabs(View view) {
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
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.select_a_pod, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.select_a_pod:
			Intent intent = new Intent(getActivity(), SelectAPodActivity.class);
			String ansType = tabHost.getCurrentTabTag();
			EditText whistleText = (EditText) getView().findViewById(
					R.id.whistle_text);

			// There must be whistle text
			if (whistleText.getText().length() > 0) {
				String quesTxt = whistleText.getText().toString();
				intent.putExtra("iQuesTxt", quesTxt);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mWhistleImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
				byte[] byteArray = stream.toByteArray();
				intent.putExtra("iQuesImg", byteArray);
				Log.i("CREATE FRAGMENT WOES ;__; ", byteArray.toString());


				if (ansType.equals("Text")) {
					intent.putExtra("iAnsType", "TXT");

					// Get all the text answers
					EditText textAnswer1 = (EditText) getView().findViewById(
							R.id.text_answer1);
					EditText textAnswer2 = (EditText) getView().findViewById(
							R.id.text_answer2);
					EditText textAnswer3 = (EditText) getView().findViewById(
							R.id.text_answer3);
					EditText textAnswer4 = (EditText) getView().findViewById(
							R.id.text_answer4);

					// Add them to the textAnswers ArrayList
					if (textAnswer1.getText().length() > 0) {
						String ansOptTxt1 = textAnswer1.getText().toString();
						textAnswers.add(ansOptTxt1);
					}
					if (textAnswer2.getText().length() > 0) {
						String ansOptTxt2 = textAnswer2.getText().toString();
						textAnswers.add(ansOptTxt2);
					}
					if (textAnswer3.getText().length() > 0) {
						String ansOptTxt3 = textAnswer3.getText().toString();
						textAnswers.add(ansOptTxt3);
					}
					if (textAnswer4.getText().length() > 0) {
						String ansOptTxt4 = textAnswer4.getText().toString();
						textAnswers.add(ansOptTxt4);
					}

					// There must be two or more text answers
					if (textAnswers.size() < 2) {
						MinimumAnswerWarningDialogFragment minimumAnswerWarning = new MinimumAnswerWarningDialogFragment();
						minimumAnswerWarning.show(getFragmentManager(),
								"minimumAnswerWarning");
						textAnswers.clear();
					}

					// If there are...
					else {
						intent.putExtra("iAnsOptTxtArray", textAnswers);
						for (int i = 0; i < textAnswers.size(); i++) {
							intent.putExtra("iAnsOptTxt" + i, textAnswers.get(i));
						}
						intent.putExtra("iAnsOptTxtCount", textAnswers.size());
						textAnswers.clear();
						startActivity(intent);
					}
				}

				else if (ansType.equals("Photo")) {
					intent.putExtra("iAnsType", "PHOT");

					// Do a quick count of images
					int countImages = 0;
					for (int i = 0; i < mPhotoAnswerImages.length; i++) {
						if (mPhotoAnswerImages[i] != null) {
							countImages++;
						}
					}

					if (countImages < 2) {
						MinimumAnswerWarningDialogFragment minimumAnswerWarning = new MinimumAnswerWarningDialogFragment();
						minimumAnswerWarning.show(getFragmentManager(),
								"minimumAnswerWarning");
					} else {
						int count = 1;
						for (int i = 0; i < countImages; i++) {
							if (mPhotoAnswerImages[i] != null) {
								ByteArrayOutputStream photoStream = new ByteArrayOutputStream();
								mPhotoAnswerImages[i].compress(Bitmap.CompressFormat.PNG, 0, stream);
								byte[] photoByteArray = photoStream.toByteArray();								
								intent.putExtra("iAnsOptImg" + count,
										photoByteArray);
								count++;
							}
						}
						intent.putExtra("iAnsOptImgCount", countImages);
						startActivity(intent);
					}
				}

				else if (ansType.equals("Rate")) {
					intent.putExtra("iAnsType", "RATE");
					if (mRatingImage != null) {
						ByteArrayOutputStream rateStream = new ByteArrayOutputStream();
						mRatingImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
						byte[] rateByteArray = rateStream.toByteArray();
						intent.putExtra("iRateOptImg", rateByteArray);
					}
				}
			} else {
				WhistleTextWarningDialogFragment whistleTextWarning = new WhistleTextWarningDialogFragment();
				whistleTextWarning.show(getFragmentManager(),
						"whistleTextWarning");
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setButtonListeners(View view) {

		ImageButton whistlePhoto = (ImageButton) view
				.findViewById(R.id.whistle_image);
		whistlePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});

		ImageButton imageAnswer1 = (ImageButton) view
				.findViewById(R.id.image_answer1);
		imageAnswer1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});

		ImageButton imageAnswer2 = (ImageButton) view
				.findViewById(R.id.image_answer2);
		imageAnswer2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});

		ImageButton imageAnswer3 = (ImageButton) view
				.findViewById(R.id.image_answer3);
		imageAnswer3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});

		ImageButton imageAnswer4 = (ImageButton) view
				.findViewById(R.id.image_answer4);
		imageAnswer4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});

		ImageButton ratingPhoto = (ImageButton) view
				.findViewById(R.id.rating_photo);
		ratingPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				chooseImage(view);
			}
		});
	}

	public void chooseImage(View view) {
		int imageButtonId = view.getId();
		ChooseImageDialogFragment imageDialog = new ChooseImageDialogFragment();
		Bundle mBundle = new Bundle();
		mBundle.putInt("iImageButtonId", imageButtonId);
		imageDialog.setArguments(mBundle);
		imageDialog.setTargetFragment(this, 1);
		imageDialog.show(getFragmentManager(), "chooseImage");
	}

	public void addWhistleImage(Bitmap whistleImage) {
		mWhistleImage = whistleImage;
	}

	public void addPhotoAnswerImage(Bitmap photoAnswerImage, int position) {
		mPhotoAnswerImages[position] = photoAnswerImage;
	}

	public void addRatingImage(Bitmap ratingImage) {
		mRatingImage = ratingImage;
	}
}

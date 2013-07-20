package me.chatpass.chatpassme;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ChooseImageDialogFragment extends DialogFragment {

	private int imageButtonId;
	private ChooseImageDialogFragmentListener fragment;
	private ArrayList<View> imageButtonIds = new ArrayList<View>();

	static ChooseImageDialogFragment newInstance() {
		return new ChooseImageDialogFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_choose_image, null);
		builder.setView(view);
		builder.setMessage(R.string.choose_image).setNegativeButton(
				R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});

		fragment = (ChooseImageDialogFragmentListener) getTargetFragment();
		Bundle mBundle = getArguments();
		imageButtonId = mBundle.getInt("iImageButtonId");

		imageButtonIds.add(getTargetFragment().getView().findViewById(
				R.id.image_answer1));
		imageButtonIds.add(getTargetFragment().getView().findViewById(
				R.id.image_answer2));
		imageButtonIds.add(getTargetFragment().getView().findViewById(
				R.id.image_answer3));
		imageButtonIds.add(getTargetFragment().getView().findViewById(
				R.id.image_answer4));

		setDialogListeners(view);

		return builder.create();
	}

	private void setDialogListeners(View view) {
		ImageButton chooseImageUpload = (ImageButton) view
				.findViewById(R.id.choose_image_upload);

		chooseImageUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 0);
			}
		});

		ImageButton chooseImageTake = (ImageButton) view
				.findViewById(R.id.choose_image_take);

		chooseImageTake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == 0) {
				dismiss();
			}

			if (resultCode == -1) {
				if (imageButtonId == getTargetFragment().getView()
						.findViewById(R.id.whistle_image).getId()) {
					Uri chosenImageUri = data.getData();
					try {
						Bitmap myImage = Media.getBitmap(getActivity()
								.getContentResolver(), chosenImageUri);
						ImageButton imageButton = (ImageButton) getTargetFragment()
								.getView().findViewById(R.id.whistle_image);
						imageButton.setImageBitmap(myImage);
						fragment.addWhistleImage(myImage);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				else if (imageButtonId == getTargetFragment().getView()
						.findViewById(R.id.rating_photo).getId()) {
					Uri chosenImageUri = data.getData();
					try {
						Bitmap myImage = Media.getBitmap(getActivity()
								.getContentResolver(), chosenImageUri);
						ImageButton imageButton = (ImageButton) getTargetFragment()
								.getView().findViewById(R.id.rating_photo);
						imageButton.setImageBitmap(myImage);
						fragment.addRatingImage(myImage);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				else {
					for (int i = 0; i < imageButtonIds.size(); i++) {
						if (imageButtonId == imageButtonIds.get(i).getId()) {
							Uri chosenImageUri = data.getData();
							try {
								Bitmap myImage = Media.getBitmap(getActivity()
										.getContentResolver(), chosenImageUri);
								ImageButton imageButton = (ImageButton) imageButtonIds
										.get(i);
								imageButton.setImageBitmap(myImage);
								fragment.addPhotoAnswerImage(myImage, i);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				dismiss();
			}
		}
		if (requestCode == 1) {
			if (resultCode == 0) {
				dismiss();
			}

			if (resultCode == -1) {
				if (imageButtonId == getTargetFragment().getView()
						.findViewById(R.id.whistle_image).getId()) {
					Bitmap myImage = (Bitmap) data.getExtras().get("data");
					ImageButton imageButton = (ImageButton) getTargetFragment()
							.getView().findViewById(R.id.whistle_image);
					imageButton.setImageBitmap(myImage);
					fragment.addWhistleImage(myImage);
				}

				else if (imageButtonId == getTargetFragment().getView()
						.findViewById(R.id.rating_photo).getId()) {
					Bitmap myImage = (Bitmap) data.getExtras().get("data");
					ImageButton imageButton = (ImageButton) getTargetFragment()
							.getView().findViewById(R.id.rating_photo);
					imageButton.setImageBitmap(myImage);
					fragment.addRatingImage(myImage);
				}

				else {
					for (int i = 0; i < imageButtonIds.size(); i++) {
						if (imageButtonId == imageButtonIds.get(i).getId()) {
							Bitmap myImage = (Bitmap) data.getExtras().get(
									"data");
							ImageButton imageButton = (ImageButton) imageButtonIds
									.get(i);
							imageButton.setImageBitmap(myImage);
							fragment.addPhotoAnswerImage(myImage, i);
						}
					}
				}
			}
			dismiss();
		}
	}

	public interface ChooseImageDialogFragmentListener {
		void addWhistleImage(Bitmap whistleImage);

		void addPhotoAnswerImage(Bitmap photoAnswerImage, int position);

		void addRatingImage(Bitmap ratingImage);
	}
}

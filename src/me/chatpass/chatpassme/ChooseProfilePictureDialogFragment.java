package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ChooseProfilePictureDialogFragment extends DialogFragment {

	private DecodeSampledBitmap decode = new DecodeSampledBitmap();

	static ChooseProfilePictureDialogFragment newInstance() {
		return new ChooseProfilePictureDialogFragment();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_choose_profile_picture,
				null);
		builder.setView(view);
		builder.setMessage(R.string.choose_image).setNegativeButton(
				R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dismiss();
					}
				});

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
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode == -1) {
				Uri chosenImageUri = data.getData();
				try {
					Bitmap myImage = Media.getBitmap(getActivity()
							.getContentResolver(), chosenImageUri);
					ImageView imageButton = (ImageView) getTargetFragment()
							.getView().findViewById(R.id.profile_picture);

					pushNewPictureToParse(imageButton, myImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				dismiss();
			}
		}
		if (requestCode == 1) {
			if (resultCode == 0) {
				dismiss();
			}

			if (resultCode == -1) {
				Bitmap myImage = (Bitmap) data.getExtras().get("data");
				ImageView imageButton = (ImageView) getTargetFragment()
						.getView().findViewById(R.id.profile_picture);
				pushNewPictureToParse(imageButton, myImage);
			}
			dismiss();
		}
	}

	private void pushNewPictureToParse(ImageView imageButton, Bitmap myImage) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		myImage.compress(Bitmap.CompressFormat.PNG, 0, stream);
		byte[] byteArray = stream.toByteArray();
		ParseFile pImageFile = new ParseFile(byteArray);
		ParseQuery<ParseObject> qUsers = ParseQuery.getQuery("Users");
		qUsers.whereEqualTo("userId", 257);
		try {
			ParseObject users = qUsers.getFirst();
			users.put("imageFile", pImageFile);
			users.saveInBackground();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Bitmap cropped;
		Bitmap source = decode.decodeSampledBitmap(byteArray, 150, 150);
		if (source.getWidth() >= source.getHeight()) {
			cropped = Bitmap.createBitmap(source, source.getWidth() / 2
					- source.getHeight() / 2, 0, source.getHeight(),
					source.getHeight());
		} else {
			cropped = Bitmap.createBitmap(source, 0, source.getHeight() / 2
					- source.getWidth() / 2, source.getWidth(),
					source.getWidth());
		}
		Bitmap scaled = Bitmap.createScaledBitmap(cropped, 150, 150, true);
		imageButton.setImageBitmap(scaled);
	}
}

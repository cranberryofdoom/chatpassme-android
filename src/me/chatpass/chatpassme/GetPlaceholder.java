package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class GetPlaceholder {

	private Drawable mDrawable;

	public GetPlaceholder(Activity activity) {
		Resources res = activity.getResources();
		mDrawable = res.getDrawable(R.drawable.whistle_placeholder);
	}

	public Bitmap getBitmap() {
		Bitmap bitmapPlaceholder = ((BitmapDrawable) mDrawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmapPlaceholder.compress(Bitmap.CompressFormat.JPEG, 100,
				stream);
		return bitmapPlaceholder;

	}

	public byte[] getByteArray() {
		Bitmap bitmapPlaceholder = ((BitmapDrawable) mDrawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmapPlaceholder.compress(Bitmap.CompressFormat.JPEG, 100,
				stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;

	}

}

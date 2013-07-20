package me.chatpass.chatpassme;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;
import android.util.Log;

public class FlickrFetchr {
	public static final String TAG = "PhotoFetcher";

	private static final String ENDPOINT = "http://api.flickr.com/services/rest/";
	private static final String API_KEY = "4f721bbafa75bf6d2cb5af54f937bb70";
	private static final String METHOD_GET_RECENT = "flickr.photos.getRecent";
	private static final String METHOD_GET_RECENT = "flickr.photos.search";
	private static final String PARAM_EXTRAS = "extras";
	private static final String PARAM_TEXT = "text";
	private static final String EXTRA_SMALL_URL = "url_s";

	public ArrayList<GalleryItem> downloadGalleryItems(String url) {
		ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();
		try {
			String xmlString = getUrl(url);
			Log.i(TAG, "Received xml: " + xmlString);
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = factory.newPullParser();
			parser.setInput(new StringReader(xmlString));

			parseItems(items, parser);
		} catch (IOException ioe) {
			Log.e(TAG, "Failed to fetch items", ioe);
		} catch (XmlPullParserException xppe) {
			Log.e(TAG, "Failed to parse items", xppe);
		}
		return items;
		
	}

	public ArrayList<GalleryItem> fetchItems() {
		String url = Uri.parse(ENDPOINT).buildUpon()
				.appendQueryParameter("method", METHOD_GET_RECENT)
				.appendQueryParameter("api_key", API_KEY)
				.appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
				.build().toString();
		return downloadGalleryItems(url);
	}

	public ArrayList<GalleryItem> search(String query) {
		String url = Uri.parse(ENDPOINT).buildUpon()
						.appendQueryParameter("method", METHOD_SEARCH)
						.appendQueryParameter("api_key", API_KEY)
						.appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL)
						.appendQueryParameter(PARAM_TEXT, query)
						.build().toString();
		return downloadGalleryItems(url);
	}
}

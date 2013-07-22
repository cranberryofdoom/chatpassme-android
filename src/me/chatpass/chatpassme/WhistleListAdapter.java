package me.chatpass.chatpassme;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

public class WhistleListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ParseObject> mObjects;
	
	public WhistleListAdapter(Context c, List<ParseObject> objects){
		mContext = c;
		mObjects = objects;
	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			textView = new TextView(mContext);
			textView.setTextAppearance(mContext, android.R.attr.textAppearanceLarge);
			textView.setPadding(10, 10, 10, 10);
		}
		else {
			textView = (TextView) convertView;
		}
		textView.setText(mObjects.get(position).getString("quesTxt"));
		return textView;
	}

}

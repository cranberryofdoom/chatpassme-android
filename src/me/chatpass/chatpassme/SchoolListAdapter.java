package me.chatpass.chatpassme;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

public class SchoolListAdapter extends BaseAdapter {
	private Context mContext;
	private List<ParseObject> mCloseSchools;
	
	public SchoolListAdapter(Context c, List<ParseObject> closeSchools) {
		mContext = c;
		mCloseSchools = closeSchools;
	}

	@Override
	public int getCount() {
		return mCloseSchools.size();
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
		TextView listView;
		if (convertView == null) {
			listView = new TextView(mContext);
		} else {
			listView = (TextView) convertView;
		}
		listView.setTextAppearance(mContext, android.R.attr.textAppearanceLarge);
		listView.setText(mCloseSchools.get(position).getString("name"));
		return listView;
	}

}

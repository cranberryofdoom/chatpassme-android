package me.chatpass.chatpassme;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class VoteQues {
	public VoteQues() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("MyClass");
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> objects, ParseException e) {
				if (e == null) {
					//objectsWereRetrievedSuccessfully(objects);
				} else {
					//objectRetrievalFailed();
				}
			}
		});
	}
}

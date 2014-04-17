package com.appnet.app.operations;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.appnet.app.datasets.PostTable;
import com.appnet.app.models.Post;
import com.appnet.app.providers.AppNetContentProvider;
import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;
import com.google.gson.Gson;

public class PostTask extends Task<String> {

	private final String mId;
	
	public PostTask(final String id) {
		mId = id;
	}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>(String.format("post::%s", mId));
	}
	
	@Override
	public String onExecuteNetworking(final Context context) throws Exception {
		throw new Exception("Override this method to return a json string for a Post.");
	}

	@Override
	public void onExecuteProcessing(final Context context, final String data) throws Exception {
		final Post item = new Gson().fromJson(data, Post.class);
		final ContentValues values = PostTable.getContentValues(item);
		final ContentResolver resolver = context.getContentResolver();
		resolver.insert(AppNetContentProvider.Uris.POSTS_URI, values);
	}
}

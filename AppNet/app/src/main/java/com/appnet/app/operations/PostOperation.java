package com.appnet.app.operations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import io.pivotal.arca.dispatcher.ErrorBroadcaster;
import io.pivotal.arca.service.Operation;
import io.pivotal.arca.service.ServiceError;
import io.pivotal.arca.service.Task;

public class PostOperation extends Operation {

	public PostOperation(final Uri uri) {
		super(uri);
	}

	public PostOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		final String id = getUri().getLastPathSegment();
		set.add(new PostTask(id));
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(getUri(), null);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		ErrorBroadcaster.broadcast(context, getUri(), error.getCode(), error.getMessage());
	}
	
	public static final Parcelable.Creator<PostOperation> CREATOR = new Parcelable.Creator<PostOperation>() {
		@Override
		public PostOperation createFromParcel(final Parcel in) {
			return new PostOperation(in);
		}

		@Override
		public PostOperation[] newArray(final int size) {
			return new PostOperation[size];
		}
	};

}

package com.rottentomatoes.app.operations;

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
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;

public class MovieListOperation extends Operation {


	public MovieListOperation(final Uri uri) {
		super(uri);
	}

	public MovieListOperation(final Parcel in) {
		super(in);
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		super.writeToParcel(dest, flags);
	}

	@Override
	public Set<Task<?>> onCreateTasks() {
		final Set<Task<?>> set = new HashSet<Task<?>>();
		final String type = getUri().getLastPathSegment();
		set.add(new MovieListTask(type));
		return set;
	}

	@Override
	public void onSuccess(final Context context, final List<Task<?>> completed) {
		final ContentResolver resolver = context.getContentResolver();
		resolver.notifyChange(RottenTomatoesContentProvider.Uris.MOVIES_URI, null, false);
		resolver.notifyChange(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, null, false);
	}

	@Override
	public void onFailure(final Context context, final ServiceError error) {
		ErrorBroadcaster.broadcast(context, getUri(), error.getCode(), error.getMessage());
	}
	
	public static final Parcelable.Creator<MovieListOperation> CREATOR = new Parcelable.Creator<MovieListOperation>() {
		@Override
		public MovieListOperation createFromParcel(final Parcel in) {
			return new MovieListOperation(in);
		}

		@Override
		public MovieListOperation[] newArray(final int size) {
			return new MovieListOperation[size];
		}
	};

}

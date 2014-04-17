package com.rottentomatoes.app.operations;

import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import io.pivotal.arca.provider.DataUtils;
import io.pivotal.arca.service.Task;
import io.pivotal.arca.threading.Identifier;
import com.rottentomatoes.app.application.RottenTomatoesRequests;
import com.rottentomatoes.app.datasets.MovieTypeTable;
import com.rottentomatoes.app.models.Movie;
import com.rottentomatoes.app.models.MovieType;
import com.rottentomatoes.app.models.MoviesResponse;
import com.rottentomatoes.app.providers.RottenTomatoesContentProvider;

public class MovieListTask extends Task<List<Movie>> {

	private final String mType;
	
	public MovieListTask(final String type) {
		mType = type;
	}

	@Override
	public Identifier<?> onCreateIdentifier() {
		return new Identifier<String>("movie_list:" + mType);
	}
	
	@Override
	public List<Movie> onExecuteNetworking(final Context context) throws Exception {
		final MoviesResponse response = RottenTomatoesRequests.getMovieList(mType, 50, "ca");
		return response.getMovies();
	}

	@Override
	public void onExecuteProcessing(final Context context, final List<Movie> data) throws Exception {
		final ContentResolver resolver = context.getContentResolver();
		
		final ContentValues[] movieValues = DataUtils.getContentValues(data);
		resolver.bulkInsert(RottenTomatoesContentProvider.Uris.MOVIES_URI, movieValues);
		
		final String whereClause = MovieTypeTable.Columns.TYPE + "=?";
		final String[] whereArgs = new String[] { mType };
		resolver.delete(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, whereClause, whereArgs);

		final ContentValues[] movieTypeValues = MovieType.getContentValues(data, mType);
		resolver.bulkInsert(RottenTomatoesContentProvider.Uris.MOVIE_TYPES_URI, movieTypeValues);
	}
}

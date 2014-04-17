package com.appnet.app.fragments;

import java.util.Arrays;
import java.util.Collection;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appnet.app.R;
import com.appnet.app.datasets.PostTable;
import com.appnet.app.providers.AppNetContentProvider;
import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.SupportItemAdapter;
import io.pivotal.arca.adapters.ViewBinder;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.fragments.ArcaItemSupportFragment;

public class PostFragment extends ArcaItemSupportFragment implements ViewBinder {
	
	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.post_text, PostTable.Columns.TEXT.name),
	});

	private String mId;

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_post, container, false);
	}
	
	@Override
	public CursorAdapter onCreateAdapter(final View view, final Bundle savedInstanceState) {
		final SupportItemAdapter adapter = new SupportItemAdapter(getActivity(), BINDINGS);
		adapter.setViewBinder(this);
		return adapter;
	}

	public void setId(final String id) {
		mId = id;
		loadPost(mId);
	}
	
	private void loadPost(final String id) {
		final Uri baseUri = AppNetContentProvider.Uris.POSTS_URI;
		final Uri contentUri = Uri.withAppendedPath(baseUri, id);
		execute(new Query(contentUri));
	}
	
	@Override
	public void onContentError(final Error error) {
		Toast.makeText(getActivity(), "ERROR: " + error.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onContentChanged(final QueryResult result) {
		final CursorAdapter adapter = getCursorAdapter();
		if (adapter.getCount() > 0) {
			showResults();
		} else {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.post_container).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
		return false;
	}
	
}

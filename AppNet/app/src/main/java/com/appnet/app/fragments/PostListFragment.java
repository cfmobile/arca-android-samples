package com.appnet.app.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.appnet.app.R;
import com.appnet.app.activities.PostActivity;
import com.appnet.app.monitors.PostListMonitor;
import com.appnet.app.providers.AppNetContentProvider;
import com.appnet.app.providers.AppNetContentProvider.PostTable;
import com.xtremelabs.imageutils.ImageLoader;

import java.util.Arrays;
import java.util.Collection;

import io.pivotal.arca.adapters.Binding;
import io.pivotal.arca.adapters.SupportCursorAdapter;
import io.pivotal.arca.adapters.ViewBinder;
import io.pivotal.arca.dispatcher.Error;
import io.pivotal.arca.dispatcher.Query;
import io.pivotal.arca.dispatcher.QueryResult;
import io.pivotal.arca.fragments.ArcaAdapterSupportFragment;
import io.pivotal.arca.monitor.ArcaDispatcher;

public class PostListFragment extends ArcaAdapterSupportFragment implements OnItemClickListener, ViewBinder {

	private static final Collection<Binding> BINDINGS = Arrays.asList(new Binding[] { 
		new Binding(R.id.list_item_post_text, PostTable.Columns.TEXT),
		new Binding(R.id.list_item_post_image, PostTable.Columns.IMAGE_URL),
		new Binding(R.id.list_item_post_created_at, PostTable.Columns.CREATED_AT),
	});
	
	private ImageLoader mImageLoader;
	
	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_post_list, container, false);
		((AbsListView) view.findViewById(R.id.post_list)).setOnItemClickListener(this);
		return view;
	}
	
	@Override
	public CursorAdapter onCreateAdapter(final AdapterView<CursorAdapter> adapterView, final Bundle savedInstanceState) {
		final SupportCursorAdapter adapter = new SupportCursorAdapter(getActivity(), R.layout.list_item_post, BINDINGS);
		adapter.setViewBinder(this);
		return adapter;
	}
	
	@Override
	public ArcaDispatcher onCreateDispatcher(final Bundle savedInstaceState) {
		final ArcaDispatcher dispatcher = super.onCreateDispatcher(savedInstaceState);
		dispatcher.setRequestMonitor(new PostListMonitor());
		return dispatcher;
	}
	
	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
	}
	
	@Override
	public void onDestroyView() {
		mImageLoader.destroy();
		super.onDestroyView();
	}
	
	@Override
	public int getAdapterViewId() {
		return R.id.post_list;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		loadPosts();
	}
	
	private void loadPosts() {
		final Uri contentUri = AppNetContentProvider.Uris.POSTS_URI;
		final Query request = new Query(contentUri);
		request.setSortOrder(PostTable.Columns.CREATED_AT + " desc");
		execute(request);
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
		} else if (!result.isSyncing()) {
			hideLoading();
		}
	}
	
	private void showResults() {
		getView().findViewById(R.id.post_list).setVisibility(View.VISIBLE);
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	private void hideLoading() {
		getView().findViewById(R.id.loading).setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long id) {
		final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
		final String itemId = cursor.getString(cursor.getColumnIndex(PostTable.Columns.ID));
		PostActivity.newInstance(getActivity(), itemId);
	}

	@Override
	public boolean setViewValue(final View view, final Cursor cursor, final Binding binding) {
	
		switch (view.getId()) {
		case R.id.list_item_post_image:
		    final String url = cursor.getString(binding.getColumnIndex());
		    mImageLoader.loadImage((ImageView) view, url);
		    return true;

		default:
		    return false;
		}
	}
}

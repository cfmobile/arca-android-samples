package com.rottentomatoes.app.activities;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.rottentomatoes.app.R;
import com.rottentomatoes.app.fragments.MovieListFragment;

public class MovieListActivity extends FragmentActivity {

	private static final class Types {
		public static final String UPCOMING = "upcoming";
		public static final String OPENING = "opening";
		public static final String IN_THEATERS = "in_theaters";
		public static final String BOX_OFFICE = "box_office";
	}
	
	private static final class Titles {
		public static final String UPCOMING = "Upcoming";
		public static final String OPENING = "Opening This Weekend";
		public static final String IN_THEATERS = "In Theaters";
		public static final String BOX_OFFICE = "Box Office";
	}
	
	private static final String[] TYPES = new String[] {
		Types.BOX_OFFICE, Types.IN_THEATERS, Types.OPENING, Types.UPCOMING,
	};
	
	private static final String[] TITLES = new String[] {
		Titles.BOX_OFFICE, Titles.IN_THEATERS, Titles.OPENING, Titles.UPCOMING,
	};

	public static final void newInstance(final Context context) {
		final Intent intent = new Intent(context, MovieListActivity.class);
		context.startActivity(intent);
	}

	private SpinnerAdapter mAdapter; 
	private MovieListFragment mFragment;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_list);
		
		final FragmentManager manager = getSupportFragmentManager();
		mFragment = (MovieListFragment) manager.findFragmentById(R.id.fragment_movie_list);
		
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, TITLES) {
			
			@Override
			public View getView(final int position, final View convertView, final ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				final TextView textView = (TextView) view.findViewById(android.R.id.text1);
				textView.setTextColor(Color.WHITE);
				return view;
			};
			
			@Override
			public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
				final View view = super.getDropDownView(position, convertView, parent);
				final TextView textView = (TextView) view.findViewById(android.R.id.text1);
				textView.setTextColor(Color.WHITE);
				return view;
			}
			
		};

		setupActionBar();
	}
	
	private void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(mAdapter, mNavigationListener);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		return false;
	}

	private final OnNavigationListener mNavigationListener = new OnNavigationListener() {

        @Override
        public boolean onNavigationItemSelected(final int itemPosition, final long itemId) {
        	mFragment.setType(TYPES[itemPosition]);
        	return true;
        }
    };

}

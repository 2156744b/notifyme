package uk.gla.mobilehci.notifyme;

import uk.gla.mobilehci.notifyme.fragments.EditAddFriends;
import uk.gla.mobilehci.notifyme.fragments.FindPublicEventsFragment;
import uk.gla.mobilehci.notifyme.fragments.FriendEvents;
import uk.gla.mobilehci.notifyme.fragments.SavedEvents;
import uk.gla.mobilehci.notifyme.fragments.SimpleNotification;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	public static final int SETTINGS_RESULT = 1;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	public static CharSequence mTitle;
	private String[] drawerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);

		mTitle = mDrawerTitle = getTitle();

		drawerOptions = getResources().getStringArray(R.array.drawer_options);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, drawerOptions));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState != null) {
			if (savedInstanceState.getString("title") != null) {
				mTitle = mDrawerTitle = savedInstanceState.getString("title");
				getActionBar().setTitle(mTitle);
			}
		} else {
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new FindPublicEventsFragment())
					.commit();
			Resources res = getResources();
			String[] drawerOptions = res.getStringArray(R.array.drawer_options);
			setTitle(drawerOptions[0]);

		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {

		Fragment newFragment = null;
		Resources res = getResources();
		String[] drawerOptions = res.getStringArray(R.array.drawer_options);
		switch (position) {
		case 0:
			newFragment = new FindPublicEventsFragment();
			setTitle(drawerOptions[0]);
			break;
		case 1:
			newFragment = new SavedEvents();
			setTitle(drawerOptions[1]);
			break;
		case 2:
			newFragment = new FriendEvents();
			setTitle(drawerOptions[2]);
			break;
		case 3:
			newFragment = new EditAddFriends();
			setTitle(drawerOptions[3]);
			break;
		case 4:
			Intent i = new Intent(getApplicationContext(), AppSettings.class);
			startActivityForResult(i, SETTINGS_RESULT);
			break;
		case 5:
			newFragment = new SimpleNotification();
			setTitle(drawerOptions[4]);
			break;
		default:
			break;
		}
		if (position != 4) {
			// Create new fragment and transaction
			getFragmentManager().beginTransaction()
					.replace(R.id.content_frame, newFragment).commit();
		}
		mDrawerLayout.closeDrawer(mDrawerList);
		// THIS FUCKIN LINE can deactivate selection... kind of reseting the
		// already selected choice
		mDrawerList.setItemChecked(position, false);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("title", getActionBar().getTitle().toString());
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

}

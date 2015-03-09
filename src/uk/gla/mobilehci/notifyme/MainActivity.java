package uk.gla.mobilehci.notifyme;

import uk.gla.mobilehci.notifyme.fragments.AllEventsFragment;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] drawerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_layout);

		mTitle = mDrawerTitle = getTitle();
		drawerOptions = getResources().getStringArray(R.array.drawer_options);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, drawerOptions));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener(
				drawerOptions, mDrawerLayout, this, mDrawerList));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState != null) {

		} else {
			Fragment newFragment = new AllEventsFragment();
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.content_frame, newFragment);
			transaction.commit();

		}

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		// mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		// mDrawerLayout, /* DrawerLayout object */
		// R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		// R.string.drawer_open, /* "open drawer" description for accessibility
		// */
		// R.string.drawer_close /* "close drawer" description for accessibility
		// */
		// ) {
		// public void onDrawerClosed(View view) {
		// getActionBar().setTitle(mTitle);
		// invalidateOptionsMenu(); // creates call to
		// // onPrepareOptionsMenu()
		// }
		//
		// public void onDrawerOpened(View drawerView) {
		// getActionBar().setTitle(mDrawerTitle);
		// invalidateOptionsMenu(); // creates call to
		// // onPrepareOptionsMenu()
		// }
		// };
		// mDrawerLayout.setDrawerListener(mDrawerToggle);

	}
}

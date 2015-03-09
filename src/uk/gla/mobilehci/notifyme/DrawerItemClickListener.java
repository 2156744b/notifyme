package uk.gla.mobilehci.notifyme;

import uk.gla.mobilehci.notifyme.fragments.AllEventsFragment;
import uk.gla.mobilehci.notifyme.fragments.EditAddFriends;
import uk.gla.mobilehci.notifyme.fragments.FriendEvents;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Rafael on 3/8/2015.
 */
public class DrawerItemClickListener implements ListView.OnItemClickListener {

	public static final int SETTINGS_RESULT = 1;
	private String[] drawerOptions;
	private DrawerLayout drawerLayout;
	private Activity context;
	private ListView mDrawerList;

	public DrawerItemClickListener(String[] drawerOptions,
			DrawerLayout drawerLayout, Activity context, ListView mDrawerList) {
		this.drawerOptions = drawerOptions;
		this.drawerLayout = drawerLayout;
		this.mDrawerList = mDrawerList;
		this.context = context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position);
	}

	/**
	 * Swaps fragments in the main content view
	 */
	private void selectItem(int position) {
		// Create a new fragment and specify the planet to show based on
		// position
		// position 3 is the settings
		Fragment newFragment = null;
		switch (position) {
		case 0:
			newFragment = new AllEventsFragment();
			break;
		case 1:
			newFragment = new FriendEvents();
			break;
		case 2:
			newFragment = new EditAddFriends();
			break;
		case 3:
			Intent i = new Intent(context.getApplicationContext(),
					AppSettings.class);
			context.startActivityForResult(i, SETTINGS_RESULT);
			break;
		default:
			break;
		}

		if (position != 3) {
			// Create new fragment and transaction
			FragmentTransaction transaction = context.getFragmentManager()
					.beginTransaction();

			// Replace whatever is in the fragment_container view with this
			// fragment,
			// and add the transaction to the back stack
			transaction.replace(R.id.content_frame, newFragment);
			// transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

		drawerLayout.closeDrawer(mDrawerList);
		// THIS FUCKIN LINE can deactivate selection... kind of reseting the
		// already selected choice
		mDrawerList.setItemChecked(position, false);
	}

}

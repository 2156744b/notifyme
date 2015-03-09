package uk.gla.mobilehci.notifyme.fragments;

import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.FriendModel;
import uk.gla.mobilehci.notifyme.listview.FriendListArrayAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditAddFriends extends Fragment {

	private Activity activity;
	private ArrayList<FriendModel> data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState == null) {

			data = new ArrayList<FriendModel>();
			for (int i = 0; i < 20; i++) {
				data.add(new FriendModel("username" + i, "username" + i
						+ "@a.aaaaaaaa"));
			}
			Toast.makeText(getActivity(), "Creating friends", Toast.LENGTH_LONG)
					.show();
		} else {
			data = savedInstanceState.getParcelableArrayList("friendslist");
			Toast.makeText(getActivity(), "Loading friends", Toast.LENGTH_LONG)
					.show();
		}

		activity = getActivity();
		// Inflate the layout for this fragment

		View rootView = inflater.inflate(R.layout.friend_list_fragment,
				container, false);

		ListView friendListView = (ListView) rootView
				.findViewById(R.id.listViewFriends);

		FriendListArrayAdapter adapter = new FriendListArrayAdapter(activity,
				R.layout.listview_item_row, data);
		friendListView.setAdapter(adapter);

		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.friend_list_menu, menu);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("friendslist", data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_friend:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

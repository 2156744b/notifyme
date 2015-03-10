package uk.gla.mobilehci.notifyme.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.FriendModel;
import uk.gla.mobilehci.notifyme.listview.FriendListArrayAdapter;
import uk.gla.mobilehci.notifyme.listview.InserFriendDialog;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class EditAddFriends extends Fragment {

	private Activity activity;
	private ArrayList<FriendModel> data;
	private FriendListArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState == null) {
			readFriendList();
			Toast.makeText(getActivity(), "Reading friend list",
					Toast.LENGTH_LONG).show();
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

		adapter = new FriendListArrayAdapter(activity,
				R.layout.listview_item_row, data);
		friendListView.setAdapter(adapter);

		setHasOptionsMenu(true);

		
		return rootView;
	}

	private void readFriendList() {
		BufferedReader reader = null;
		File file = new File(getActivity().getFilesDir(), "friendList.txt");
		data = new ArrayList<FriendModel>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			while ((line = reader.readLine()) != null) {
				split = line.split(";");
				data.add(new FriendModel(split[0], split[1]));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		writeFriendList();
	}

	private void writeFriendList() {
		File file = new File(getActivity().getFilesDir(), "friendList.txt");
		if (file.exists())
			file.delete();
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (FriendModel f : data) {
				printWriter.write(f.toString() + "\n");
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	public void addFriendToList(String username, String email) {
		data.add(new FriendModel(username, email));
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Resources res = getResources();
		String[] drawerOptions = res.getStringArray(R.array.drawer_options);
		getActivity().getActionBar().setTitle(drawerOptions[2]);

		switch (item.getItemId()) {
		case R.id.action_add_friend:
			InserFriendDialog dialogF = new InserFriendDialog(
					EditAddFriends.this);
			dialogF.show(getActivity().getFragmentManager(), "dialog");

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

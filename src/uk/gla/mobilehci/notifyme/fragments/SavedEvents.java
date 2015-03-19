package uk.gla.mobilehci.notifyme.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.listview.SavedEventsArrayAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class SavedEvents extends Fragment {
	private Activity activity;
	private ArrayList<PublicEvent> data;
	private SavedEventsArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (savedInstanceState == null) {
			readSavedEvents();
			Toast.makeText(getActivity(), "Reading saved Events",
					Toast.LENGTH_LONG).show();
		} else {
			data = savedInstanceState.getParcelableArrayList("savedEvents");
			Toast.makeText(getActivity(), "Loading friends", Toast.LENGTH_LONG)
					.show();
		}

		activity = getActivity();
		View rootView = inflater.inflate(R.layout.saved_events_list_fragment,
				container, false);

		ListView savedEventsListView = (ListView) rootView
				.findViewById(R.id.listA);

		adapter = new SavedEventsArrayAdapter(activity,
				R.layout.listview_item_row_saved_public, data);
		savedEventsListView.setAdapter(adapter);
		setHasOptionsMenu(true);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		writeSavedEvents();
		readSavedEvents();
	}

	private void readSavedEvents() {
		BufferedReader reader = null;
		File file = new File(getActivity().getFilesDir(), "savedEvents.txt");
		data = new ArrayList<PublicEvent>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			PublicEvent publicEvent;
			while ((line = reader.readLine()) != null) {
				split = line.split(";");
				publicEvent = new PublicEvent();
				publicEvent.setId(Integer.parseInt(split[0]));
				publicEvent.setLon(Double.parseDouble(split[1]));
				publicEvent.setLat(Double.parseDouble(split[2]));
				publicEvent.setPhone(split[3]);
				publicEvent.setLocationDescription(split[4]);
				publicEvent.setDescription(split[5]);
				publicEvent.setPosterUrl(split[6]);
				publicEvent.setDate(split[7]);
				publicEvent.setType(Integer.parseInt(split[8]));
				publicEvent.setUrl(split[9]);
				publicEvent.setCreator(split[10]);
				data.add(publicEvent);
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
	}

	public void writeSavedEvents() {

		File file = new File(getActivity().getFilesDir(), "savedEvents.txt");
		if (file.exists())
			file.delete();
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (PublicEvent f : data) {
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
		// inflater.inflate(R.menu.friend_list_menu, menu);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList("savedEvents", data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_add_friend:
			// InserFriendDialog dialogF = new InserFriendDialog(
			// SavedEvents.this);
			// dialogF.show(getActivity().getFragmentManager(), "dialog");
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

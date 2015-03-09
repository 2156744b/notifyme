package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AllEventsFragment extends Fragment {

	private String toShow;
	private Activity activityHelper;
	
	public AllEventsFragment(String toShow, Activity activity) {
		// TODO Auto-generated constructor stub
		this.toShow = toShow;
		this.activityHelper = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment

		View rootView = inflater.inflate(R.layout.all_event_fragment,
				container, false);

		TextView textToShow = (TextView) rootView.findViewById(R.id.text1);
		textToShow.setText(toShow);

		return rootView;
	}
}

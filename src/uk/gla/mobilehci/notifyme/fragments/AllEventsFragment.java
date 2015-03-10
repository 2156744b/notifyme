package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.R;
import android.app.Fragment;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class AllEventsFragment extends Fragment {

	private MapView mapView;
	private GoogleMap map;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment

		View rootView = inflater.inflate(R.layout.all_event_fragment,
				container, false);

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(getActivity());
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();

	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();

	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
}

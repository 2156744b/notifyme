package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.MainActivity;
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.helpers.GetLonLat;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

public class FriendEvents extends Fragment {

	private MapView mapView;
	private GoogleMap map;
	private GetLonLat getLonLat;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment

		View rootView = inflater.inflate(R.layout.friends_event_fragment,
				container, false);

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(getActivity());
		getLonLat = new GetLonLat(getActivity());
		autoFocus();
		return rootView;
	}

	public void autoFocus() {
		getLonLat.prepareLonLat();
		if (getLonLat.isCanGetLocation()) {
			double lon = getLonLat.getLon();
			double lat = getLonLat.getLat();
			LatLng coordinate = new LatLng(lat, lon);
			CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
			map.moveCamera(center);
			map.animateCamera(zoom);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		autoFocus();
		getActivity().getActionBar().setTitle(MainActivity.mTitle);
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

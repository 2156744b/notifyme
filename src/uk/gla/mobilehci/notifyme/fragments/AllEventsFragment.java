package uk.gla.mobilehci.notifyme.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import uk.gla.mobilehci.notifyme.R;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllEventsFragment extends Fragment implements LocationListener,
		InfoWindowAdapter {

	private MapView mapView;
	private GoogleMap map;
	private LocationManager locationManager;
	private HashMap<Marker, ArrayList<String>> markerData = new HashMap<Marker, ArrayList<String>>();

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
		createDummyData();
		return rootView;
	}

	public void createDummyData() {
		MarkerOptions m1 = new MarkerOptions();
		m1.position(new LatLng(55.864594, -4.295654));
		m1.icon(BitmapDescriptorFactory.fromResource(R.drawable.art));

		// 55.864811,-4.293573
		MarkerOptions m2 = new MarkerOptions();
		m2.position(new LatLng(55.864811, -4.293573));
		m2.icon(BitmapDescriptorFactory.fromResource(R.drawable.club));

		ArrayList<String> m1Array = new ArrayList<String>();
		m1Array.add("20/03/2015");
		m1Array.add("ANTE GEIA!!!");
		ArrayList<String> m2Array = new ArrayList<String>();
		m2Array.add("23/03/2015");
		m2Array.add("ANTE GEIA REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

		markerData.put(map.addMarker(m1), m1Array);
		markerData.put(map.addMarker(m2), m2Array);

	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		map = mapView.getMap();
		map.setInfoWindowAdapter(this);
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				(long) 1000, (float) 10.0, this);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, (long) 1000, (float) 10.0,
				this);
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
		locationManager.removeUpdates(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	public void onLocationChanged(Location arg0) {
		LatLng coordinate = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		CameraUpdate center = CameraUpdateFactory.newLatLng(coordinate);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		map.moveCamera(center);
		map.animateCamera(zoom);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public View getInfoContents(Marker mark) {
		// TODO Auto-generated method stub

		// date, eikona,
		if (markerData.containsKey(mark)) {

			ArrayList<String> toShow = markerData.get(mark);

			View v = getActivity().getLayoutInflater().inflate(R.layout.marker,
					null);
			TextView date = (TextView) v.findViewById(R.id.txtDate);
			ImageView image = (ImageView) v.findViewById(R.id.imageToShow);
			TextView description = (TextView) v
					.findViewById(R.id.txtDescription);

			date.setText(toShow.get(0));
			image.setBackgroundResource(R.drawable.ic_launcher);
			description.setText(toShow.get(1));

			return v;
		}

		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}

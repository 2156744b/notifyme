package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.MainActivity;
import uk.gla.mobilehci.notifyme.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class FriendEvents extends Fragment implements LocationListener {

	private MapView mapView;
	private GoogleMap map;
	private LocationManager locationManager;
	private SharedPreferences pref;
	private Marker personalMarker;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

		rootView = inflater.inflate(R.layout.friends_event_fragment, container,
				false);

		mapView = (MapView) rootView.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);

		map = mapView.getMap();
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.setMyLocationEnabled(true);

		MapsInitializer.initialize(getActivity());
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		Location lastKnown = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastKnown != null) {
			CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(
					lastKnown.getLatitude(), lastKnown.getLongitude()));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
			map.moveCamera(center);
			map.animateCamera(zoom);
		}
		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng location) {

				if (personalMarker != null)
					personalMarker.remove();

				personalMarker = map.addMarker(new MarkerOptions()
						.position(location)
						.flat(true)
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.friend_inv)));

				rootView.findViewById(R.id.create_friendEv_menu).setVisibility(
						View.VISIBLE);

			}
		});

		rootView.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						rootView.findViewById(R.id.create_friendEv_menu)
								.setVisibility(View.GONE);
						personalMarker.remove();

					}
				});

		rootView.findViewById(R.id.create_friendEv).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						View create = getActivity().getLayoutInflater()
								.inflate(R.layout.friend_event_layout, null);
						
						TimePicker picker = (TimePicker) create.findViewById(R.id.time);
						picker.setIs24HourView(true);

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle(R.id.create_friendEv);
						builder.setView(create);
						builder.setPositiveButton("Create",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						builder.show();

					}

				});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		getActivity().getActionBar().setTitle(MainActivity.mTitle);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				(long) (pref.getInt("time_interval", 20) * 1000),
				(float) (pref.getInt("distance_interval", 200)), this);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER,
				(long) (pref.getInt("time_interval", 20) * 1000),
				(float) (pref.getInt("distance_interval", 200)), this);
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
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

}

package uk.gla.mobilehci.notifyme.fragments;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.helpers.ApplicationSettings;
import uk.gla.mobilehci.notifyme.request.ShowMarkers;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	private SharedPreferences pref;

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

		// 55.866027,-4.289914
		MarkerOptions m3 = new MarkerOptions();
		m3.position(new LatLng(55.866027, -4.289914));
		m3.icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant));

		ArrayList<String> m1Array = new ArrayList<String>();
		m1Array.add("20/03/2015");
		m1Array.add("ANTE GEIA!!!");
		ArrayList<String> m2Array = new ArrayList<String>();
		m2Array.add("23/03/2015");
		m2Array.add("ANTE GEIA REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");

		ArrayList<String> m3Array = new ArrayList<String>();
		m3Array.add("30/03/2015");
		m3Array.add("Curt gios tis ");

		markerData.put(map.addMarker(m1), m1Array);
		markerData.put(map.addMarker(m2), m2Array);
		markerData.put(map.addMarker(m3), m3Array);

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

		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
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

		new GetNearbyPublicEvent().execute(arg0.getLatitude() + "",
				arg0.getLongitude() + "",
				pref.getString("radiusSettings", "1000"));
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

	private class GetNearbyPublicEvent extends
			AsyncTask<String, Void, HttpResponse> {

		private ProgressDialog progDialog = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progDialog.setMessage("Loading...");
			// progDialog.setIndeterminate(false);
			// progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// progDialog.setCancelable(true);
			// progDialog.show();
		}

		@Override
		protected HttpResponse doInBackground(String... params) {

			try {
				// df.get("lat"), df.get("lon"), df.get("radius"));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(ApplicationSettings.serverUrl
						+ "nearbyPublicEvents");

				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("lat", params[0]));
				data.add(new BasicNameValuePair("lon", params[1]));
				data.add(new BasicNameValuePair("radius", params[2]));
				// type na to skeftoume
				// data.add(new BasicNameValuePair("lon", params[1]));

				post.setEntity(new UrlEncodedFormEntity(data));

				return client.execute(post);

			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.toString());
				return null;
			}
		}

		@Override
		protected void onPostExecute(HttpResponse response) {

			if (response == null) {

				Toast.makeText(
						(AllEventsFragment.this).getActivity()
								.getApplicationContext(), "Server error",
						Toast.LENGTH_LONG).show();

			} else {
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String data = reader.readLine();

					JSONObject obj = new JSONObject(data);
					int status = obj.getInt("rstatus");

					if (status == -1) {

						Toast.makeText(
								(AllEventsFragment.this).getActivity()
										.getApplicationContext(),
								"Error adding friend, email may not be correct",
								Toast.LENGTH_LONG).show();

					} else if (status == 200) {

						Toast.makeText(getActivity(), obj.toString(),
								Toast.LENGTH_SHORT).show();

						Toast.makeText(
								(AllEventsFragment.this).getActivity()
										.getApplicationContext(),
								"Friend successfully added", Toast.LENGTH_LONG)
								.show();
					} else {
						new Exception();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(
							(AllEventsFragment.this).getActivity()
									.getApplicationContext(),
							"Error adding friend", Toast.LENGTH_LONG).show();
				}
			}
			// progDialog.dismiss();
		}

	}

}

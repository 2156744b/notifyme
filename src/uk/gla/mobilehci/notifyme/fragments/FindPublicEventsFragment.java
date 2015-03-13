package uk.gla.mobilehci.notifyme.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.gla.mobilehci.notifyme.MainActivity;
import uk.gla.mobilehci.notifyme.PublicEventActivity;
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.helpers.ApplicationSettings;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("InflateParams")
public class FindPublicEventsFragment extends Fragment implements
		LocationListener, InfoWindowAdapter {

	private MapView mapView;
	private GoogleMap map;
	private LocationManager locationManager;
	private HashMap<Marker, PublicEvent> markerData = new HashMap<Marker, PublicEvent>();
	private SharedPreferences pref;
	public static HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

		View rootView = inflater.inflate(R.layout.all_event_fragment,
				container, false);

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
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker mark) {

				Intent i = new Intent(getActivity().getApplicationContext(),
						PublicEventActivity.class);

				i.putExtra(PublicEventActivity.PUBLIC_EVENT,
						markerData.get(mark));
				i.putExtra(PublicEventActivity.TO_SHOW, true);

				startActivity(i);

			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
		map = mapView.getMap();
		map.setInfoWindowAdapter(this);

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

		new GetNearbyPublicEvent().execute(arg0.getLatitude() + "",
				arg0.getLongitude() + "",
				pref.getString("radiusSettings", "1000"));
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public View getInfoContents(Marker mark) {
		if (markerData.containsKey(mark)) {

			PublicEvent toShow = markerData.get(mark);

			View v = getActivity().getLayoutInflater().inflate(R.layout.marker,
					null);

			TextView date = (TextView) v.findViewById(R.id.txtDate);
			ImageView image = (ImageView) v.findViewById(R.id.imageToShow);
			TextView description = (TextView) v
					.findViewById(R.id.txtDescription);

			Bitmap imageToSet = images.get(toShow.getPosterUrl());
			date.setText(toShow.getDate());
			image.setImageBitmap(imageToSet);
			description.setText(toShow.getDescription());

			return v;
		}

		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	private class GetNearbyPublicEvent extends
			AsyncTask<String, Void, ArrayList<PublicEvent>> {

		@Override
		protected ArrayList<PublicEvent> doInBackground(String... params) {

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

				HttpResponse response = client.execute(post);

				return processJSONArray(response);

			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.toString());
				return null;
			}
		}

		public ArrayList<PublicEvent> processJSONArray(HttpResponse response) {
			ArrayList<PublicEvent> dataToSend = null;
			if (response == null) {

				Toast.makeText(
						(FindPublicEventsFragment.this).getActivity()
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
								(FindPublicEventsFragment.this).getActivity()
										.getApplicationContext(),
								"Error retrieving events", Toast.LENGTH_LONG)
								.show();
					} else if (status == 200) {
						PublicEvent publicEvent;
						dataToSend = new ArrayList<PublicEvent>();
						JSONArray arrayToProcess = obj.getJSONArray("events");
						for (int i = 0; i < arrayToProcess.length(); i++) {
							publicEvent = new PublicEvent();
							dataToSend.add(publicEvent);
							publicEvent.setId(arrayToProcess.getJSONObject(i)
									.getInt("id"));
							publicEvent
									.setLon(Double.parseDouble(arrayToProcess
											.getJSONObject(i).getString("lon")));
							publicEvent
									.setLat(Double.parseDouble(arrayToProcess
											.getJSONObject(i).getString("lat")));
							publicEvent.setPhone(arrayToProcess
									.getJSONObject(i).getString("phone"));

							publicEvent.setLocationDescription(arrayToProcess
									.getJSONObject(i).getString(
											"locationDescription"));

							publicEvent.setDescription(arrayToProcess
									.getJSONObject(i).getString("description"));

							publicEvent.setPosterUrl(arrayToProcess
									.getJSONObject(i).getString("posterUrl"));
							publicEvent.setDate(arrayToProcess.getJSONObject(i)
									.getString("date"));
							publicEvent.setType(arrayToProcess.getJSONObject(i)
									.getInt("type"));
							publicEvent.setUrl(arrayToProcess.getJSONObject(i)
									.getString("url"));
							publicEvent.setCreator(arrayToProcess
									.getJSONObject(i).getString("creator"));

							if (!images.containsKey(publicEvent.getUrl())) {
								URL url = new URL(publicEvent.getPosterUrl());
								Bitmap image = BitmapFactory.decodeStream(url
										.openConnection().getInputStream());
								saveFile(image, publicEvent.getId()+".PNG");
								images.put(publicEvent.getPosterUrl(), image);
							}
						}
					} else {
						new Exception();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(
							(FindPublicEventsFragment.this).getActivity()
									.getApplicationContext(),
							"Error retrieving events", Toast.LENGTH_LONG)
							.show();
				}
			}
			return dataToSend;
		}

		public void saveFile(Bitmap b, String picName) {
			FileOutputStream fos;
			try {
				fos = getActivity().openFileOutput(picName,
						Context.MODE_PRIVATE);
				b.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(ArrayList<PublicEvent> result) {
			if (result != null) {
				if (!markerData.isEmpty()) {
					map.clear();
					markerData.clear();
				}
				MarkerOptions m1;
				for (PublicEvent publicEvent : result) {
					m1 = new MarkerOptions();
					m1.position(new LatLng(publicEvent.getLat(), publicEvent
							.getLon()));

					switch (publicEvent.getType()) {
					case 1:
						m1.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.club));
						break;
					case 2:
						m1.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.theatre));
						break;
					case 3:
						m1.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.music));
						break;
					case 4:
						m1.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.restaurant));
						break;
					case 5:
						m1.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.art));
						break;
					default:
						break;
					}
					markerData.put(map.addMarker(m1), publicEvent);
					System.out.println("Added Marker");
				}
			}
		}
	}
}

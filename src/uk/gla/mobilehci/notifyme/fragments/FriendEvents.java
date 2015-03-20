package uk.gla.mobilehci.notifyme.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
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
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.FriendEvent;
import uk.gla.mobilehci.notifyme.datamodels.FriendModel;
import uk.gla.mobilehci.notifyme.helpers.ApplicationSettings;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("InflateParams")
public class FriendEvents extends Fragment implements LocationListener,
		InfoWindowAdapter {

	private MapView mapView;
	private GoogleMap map;
	private LocationManager locationManager;
	private SharedPreferences pref;
	private Marker personalMarker;
	private View rootView;
	private ArrayList<FriendModel> data;
	private HashMap<Marker, FriendEvent> markerData = new HashMap<Marker, FriendEvent>();

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

						final ScrollView create = (ScrollView) getActivity()
								.getLayoutInflater().inflate(
										R.layout.friend_event_layout, null);
						final LinearLayout checkboxWrapper = ((LinearLayout) create
								.findViewById(R.id.checkboxWrapper));

						readFriendList();

						for (int f = 0; f < data.size(); f++) {
							CheckBox cb = new CheckBox(getActivity());
							cb.setText(data.get(f).getUsername());

							checkboxWrapper.addView(cb);

						}

						TimePicker picker = (TimePicker) create
								.findViewById(R.id.time);
						picker.setIs24HourView(true);

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Create friend event");
						builder.setView(create);
						builder.setPositiveButton("Create",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										DatePicker datePicker = ((DatePicker) create
												.findViewById(R.id.date));

										TimePicker timePicker = ((TimePicker) create
												.findViewById(R.id.time));

										Calendar cal = Calendar.getInstance();
										cal.set(Calendar.DAY_OF_MONTH,
												datePicker.getDayOfMonth());
										cal.set(Calendar.MONTH,
												datePicker.getMonth());
										cal.set(Calendar.YEAR,
												datePicker.getYear());
										cal.set(Calendar.HOUR_OF_DAY,
												timePicker.getCurrentHour());
										cal.set(Calendar.MINUTE,
												timePicker.getCurrentMinute());

										long timestamp = cal.getTimeInMillis();

										String evdesc = ((EditText) create
												.findViewById(R.id.txtEventDescription))
												.getText().toString();
										String locdesc = ((EditText) create
												.findViewById(R.id.txtLocationDescription))
												.getText().toString();

										String friends = "";

										for (int i = 0; i < checkboxWrapper
												.getChildCount(); i++) {
											CheckBox c = (CheckBox) checkboxWrapper
													.getChildAt(i);
											if (c.isChecked())
												friends += data.get(i)
														.getEmail() + ";";

										}
										friends = friends.substring(0,
												friends.length() - 1);

										new CreateFriendsEvent().execute(
												String.valueOf(personalMarker
														.getPosition().latitude),
												String.valueOf(personalMarker
														.getPosition().longitude),
												pref.getString("userEmail", ""),
												String.valueOf(timestamp),
												locdesc, evdesc, friends);

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

		new GetFriendEvents().execute(arg0.getLatitude() + "",
				arg0.getLongitude() + "");

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
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View getInfoContents(Marker mark) {
		if (markerData.containsKey(mark)) {

			FriendEvent toShow = markerData.get(mark);

			View v = getActivity().getLayoutInflater().inflate(
					R.layout.friend_event_info_layout, null);

			TextView date = (TextView) v.findViewById(R.id.txtDate);
			TextView author = (TextView) v.findViewById(R.id.txtAuthor);
			author.setText(toShow.creator);
			date.setText(toShow.timestamp);
			TextView locdes = (TextView) v
					.findViewById(R.id.txtLocationDescription);

			locdes.setText(toShow.locdescription);
			TextView des = (TextView) v
					.findViewById(R.id.txtListEventDescription);
			des.setText(toShow.description);

			return v;
		}
		return null;

	}

	@Override
	public View getInfoWindow(Marker mark) {

		return null;
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

	private class CreateFriendsEvent extends
			AsyncTask<String, Void, HttpResponse> {

		@Override
		protected void onPostExecute(HttpResponse response) {
			super.onPostExecute(response);

			if (response == null) {

				Toast.makeText(getActivity(), "Error creating event",
						Toast.LENGTH_LONG).show();
				personalMarker.remove();

			} else {
				try {

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String data = reader.readLine();

					JSONObject obj = new JSONObject(data);
					int status = obj.getInt("rstatus");

					if (status == -1) {

						Toast.makeText(getActivity(), "Error creating event",
								Toast.LENGTH_LONG).show();
						personalMarker.remove();

					}

					else {
						Toast.makeText(getActivity(),
								"Successfully created event", Toast.LENGTH_LONG)
								.show();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(getActivity(), "Error creating event",
							Toast.LENGTH_LONG).show();
					personalMarker.remove();
				}

			}

			rootView.findViewById(R.id.create_friendEv_menu).setVisibility(
					View.GONE);
		}

		@Override
		protected HttpResponse doInBackground(String... params) {

			try {
				// df.get("lat"), df.get("lon"), df.get("radius"));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(ApplicationSettings.serverUrl
						+ "createFriendEvent");

				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("lat", params[0]));
				data.add(new BasicNameValuePair("lon", params[1]));
				data.add(new BasicNameValuePair("creator", params[2]));
				data.add(new BasicNameValuePair("timestamp", params[3]));
				data.add(new BasicNameValuePair("locdescription", params[4]));
				data.add(new BasicNameValuePair("description", params[5]));
				data.add(new BasicNameValuePair("friends", params[6]));

				post.setEntity(new UrlEncodedFormEntity(data));

				HttpResponse response = client.execute(post);

				return response;

			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.toString());
				return null;
			}
		}

	}

	private class GetFriendEvents extends
			AsyncTask<String, Void, ArrayList<FriendEvent>> {

		@Override
		protected ArrayList<FriendEvent> doInBackground(String... params) {

			try {
				// df.get("lat"), df.get("lon"), df.get("radius"));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(ApplicationSettings.serverUrl
						+ "getFriendEvents");

				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("lat", params[0]));
				data.add(new BasicNameValuePair("lon", params[1]));

				post.setEntity(new UrlEncodedFormEntity(data));

				HttpResponse response = client.execute(post);
				System.out.println(response);
				return processJSONArray(response);

			} catch (Exception e) {
				Log.e(this.getClass().getName(), e.toString());
				return null;
			}
		}

		public ArrayList<FriendEvent> processJSONArray(HttpResponse response) {
			ArrayList<FriendEvent> dataToSend = null;
			if (response == null) {

				Toast.makeText(
						(FriendEvents.this).getActivity()
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
								(FriendEvents.this).getActivity()
										.getApplicationContext(),
								"Error retrieving events", Toast.LENGTH_LONG)
								.show();

					} else if (status == 200) {

						dataToSend = new ArrayList<FriendEvent>();
						JSONArray arrayToProcess = obj.getJSONArray("event");
						for (int i = 0; i < arrayToProcess.length(); i++) {
							FriendEvent friendEvent = new FriendEvent(
									arrayToProcess.getJSONObject(i)
											.getInt("id"),
									arrayToProcess.getJSONObject(i).getString(
											"creator"),
									arrayToProcess.getJSONObject(i).getString(
											"description"),
									arrayToProcess.getJSONObject(i).getString(
											"timestamp"),
									Double.parseDouble(arrayToProcess
											.getJSONObject(i).getString("lat")),
									Double.parseDouble(arrayToProcess
											.getJSONObject(i).getString("lon")),
									arrayToProcess.getJSONObject(i).getString(
											"locdescription"));
							dataToSend.add(friendEvent);

						}
					} else {
						new Exception();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(
							(FriendEvents.this).getActivity()
									.getApplicationContext(),
							"Error retrieving events", Toast.LENGTH_LONG)
							.show();
				}
			}
			return dataToSend;
		}

		@Override
		protected void onPostExecute(ArrayList<FriendEvent> result) {
			if (result != null) {
				if (!markerData.isEmpty()) {
					map.clear();
					markerData.clear();
				}
				MarkerOptions m1;
				for (FriendEvent event : result) {
					m1 = new MarkerOptions();
					m1.position(new LatLng(event.lat, event.lon));
					m1.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.friend_inv));

					markerData.put(map.addMarker(m1), event);

				}
			}

		}
	}

}

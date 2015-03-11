package uk.gla.mobilehci.notifyme.request;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;

public class ShowMarkers extends AsyncTask<String, Void, ArrayList<LatLng>> {

	private GoogleMap map;

	public ShowMarkers(GoogleMap map_) {
		this.map = map_;
	}

	@Override
	protected ArrayList<LatLng> doInBackground(String... url) {
		ArrayList<LatLng> allLatLong = new ArrayList<LatLng>();

		// make http request here...
		
		
		LatLng temp1 = new LatLng(55.872121099999990000, -4.288200500000016000);
		allLatLong.add(temp1);
		LatLng temp2 = new LatLng(55.865168, -4.296212);
		allLatLong.add(temp2);
		LatLng temp3 = new LatLng(55.873740, -4.302735);
		allLatLong.add(temp3);

		return allLatLong;
	}

	@Override
	protected void onPostExecute(ArrayList<LatLng> result) {

		for (int i = 0; i < result.size(); i++) {
			drawMarker(result.get(i));
		}

	}

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		map.addMarker(markerOptions);
	}

}

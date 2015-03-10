package uk.gla.mobilehci.notifyme.helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Rafael on 9/16/13.
 */
public class GetLonLat {

	public String tagString = "GetLonLat";
	private LocationManager locationManager;
	private Location location;
	private double longitude;
	private double latitude;
	private Context context;
	private boolean canGetLocation;
	public boolean isCanGetLocation() {
		return canGetLocation;
	}

	public void setCanGetLocation(boolean canGetLocation) {
		this.canGetLocation = canGetLocation;
	}

	public int MIN_TIME_BW_UPDATES = 0;
	public int MIN_DISTANCE_CHANGE_FOR_UPDATES = 10000;
	public float accuracy;

	public GetLonLat(Context c) {
		context = c;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean prepareLonLat() {
		try {
			// getting GPS status
			boolean isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			boolean isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
				Log.e(tagString, "OUTE GPS OUTE NETWORK");
				return this.canGetLocation = false;
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
					// locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					// MIN_TIME_BW_UPDATES,
					// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d(tagString, "Network");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
							accuracy = location.getAccuracy();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						// locationManager.requestLocationUpdates(
						// LocationManager.GPS_PROVIDER,
						// MIN_TIME_BW_UPDATES,
						// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d(tagString, "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
								accuracy = location.getAccuracy();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			Log.e(tagString, "EXCEPTION!!");
			return false;
		}

		return true;
	}

	public double getLon() {
		return longitude;
	}

	public double getLat() {
		return latitude;
	}

	public float getAccuracy() {
		return accuracy;
	}
}

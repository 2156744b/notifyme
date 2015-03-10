package uk.gla.mobilehci.notifyme;

import helpers.ApplicationSettings;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class Register extends Activity {

	final int ACCOUNT_PICK = 2;
	final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

	private String accountName;
	private SharedPreferences prefs = null;
	private ConnectivityManager cm;
	private NetworkInfo activeNetwork;

	private GoogleCloudMessaging gcm;
	private String regid;

	EditText userid;
	EditText username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		userid = (EditText) findViewById(R.id.userid);
		username = (EditText) findViewById(R.id.username);

	}

	@Override
	protected void onResume() {
		super.onResume();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		if (checkPlayServices()) {

			if (prefs.getString("userEmail", null) == null
					|| prefs.getString("username", null) == null
					|| prefs.getString("gcmID", null) == null) {

				if (accountName == null) {

					Intent intent = AccountPicker.newChooseAccountIntent(null,
							null, new String[] { "com.google" }, true, null,
							null, null, null);
					startActivityForResult(intent, ACCOUNT_PICK);

				}

				else {

					userid.setText(accountName);

					final Button registerButton = (Button) findViewById(R.id.registerbutton);
					registerButton.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							activeNetwork = cm.getActiveNetworkInfo();

							if (activeNetwork != null
									&& activeNetwork.isConnected()) {

								SharedPreferences.Editor editor = prefs.edit();

								editor.putString("userEmail", userid.getText()
										.toString());
								editor.putString("username", username.getText()
										.toString().trim());

								editor.commit();

								loading(true);

								new RegisterInBackground().execute();

							} else {
								Toast.makeText(
										getApplicationContext(),
										"Application needs active Internet connection",
										Toast.LENGTH_LONG).show();
							}

						}
					});
					if (username.getText().length() > 0) {
						registerButton.setEnabled(true);
					} else {
						registerButton.setEnabled(false);
					}

					username.addTextChangedListener(new TextWatcher() {

						@Override
						public void afterTextChanged(Editable s) {
							/* Nothing to do here */

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							/* Nothing to do here */

						}

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							if (username.getText().toString().trim().length() > 0) {
								registerButton.setEnabled(true);
							} else {
								registerButton.setEnabled(false);
							}

						}
					});

				}

			} else {

				loading(true);

				userid.setText(prefs.getString("userEmail", null));
				username.setText(prefs.getString("username", null));
				username.setClickable(false);
				username.setFocusable(false);

				gcm = GoogleCloudMessaging.getInstance(this);
				regid = getRegistrationId(getApplicationContext());

				activeNetwork = cm.getActiveNetworkInfo();

				if (regid == null) {

					if (activeNetwork != null && activeNetwork.isConnected()) {
						new RegisterInBackground().execute();
					} else {
						loading(false);
						Toast.makeText(getApplicationContext(),
								"Application needs active Internet connection",
								Toast.LENGTH_LONG).show();
					}

				} else {
					if (activeNetwork != null && activeNetwork.isConnected()) {
						new RegisterInBackground().execute();
					} else {
						loading(false);
						Toast.makeText(getApplicationContext(),
								"Application needs active Internet connection",
								Toast.LENGTH_LONG).show();
					}
					moveToNextActivity();
				}

			}
		}
		;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == ACCOUNT_PICK && resultCode == RESULT_OK) {
			accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
		}
	}

	private String getRegistrationId(Context context) {

		String registrationId = prefs.getString("registrationId", null);

		if (registrationId == null) {
			return null;
		}

		int registeredVersion = Integer.parseInt(prefs.getString(
				"registeredVersion", "-1"));
		int currentVersion = getAppVersion(context);

		if (registeredVersion != currentVersion) {
			return null;
		}

		return registrationId;
	}

	private int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode, this,
						PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

	private void loading(boolean isLoading) {

		if (isLoading) {
			findViewById(R.id.loading).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.loading).setVisibility(View.INVISIBLE);
		}

	}

	private void moveToNextActivity() {

		Intent mainActivity = new Intent(this, MainActivity.class);
		startActivity(mainActivity);
		finish();

	}

	private class RegisterInBackground extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging
							.getInstance(getApplicationContext());
				}
				regid = gcm.register(ApplicationSettings.gcmSenderID);

				// String NAMESPACE = "http://WebServices/";
				// String URL = ApplicationSettings.serverUrl
				// + "RegisterServices?wsdl";
				// String METHOD_NAME = "UpdateGCMId";
				// String SOAP_ACTION = "http://WebServices/UpdateGCMId";
				//
				// SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				//
				// PropertyInfo p1 = new PropertyInfo();
				// PropertyInfo p2 = new PropertyInfo();
				// PropertyInfo p3 = new PropertyInfo();
				//
				// p1.setName("appId");
				// p1.setType(String.class);
				// p1.setValue(prefs.getString("appId", null));
				// request.addProperty(p1);
				//
				// p2.setName("gcmId");
				// p2.setType(String.class);
				// p2.setValue(regid);
				// request.addProperty(p2);
				//
				// p3.setName("token");
				// p3.setType(String.class);
				// p3.setValue(token);
				// request.addProperty(p3);
				//
				// SoapSerializationEnvelope envelope = new
				// SoapSerializationEnvelope(
				// SoapEnvelope.VER11);
				// envelope.implicitTypes = true;
				// envelope.setOutputSoapObject(request);
				// envelope.encodingStyle = SoapSerializationEnvelope.XSD;
				// envelope.setOutputSoapObject(request);
				//
				// HttpTransportSE androidHttpTransport = new
				// HttpTransportSE(URL);
				// androidHttpTransport.call(SOAP_ACTION, envelope);
				//
				// return (SoapObject) envelope.getResponse();

				return regid;

			} catch (Exception ex) {
				Log.e(this.getClass().getName(), ex.toString());
				return null;
			}

		}

		@Override
		protected void onPostExecute(String response) {

			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("gcmID", response);
			editor.putString("registeredVersion", String.valueOf(getAppVersion(getApplicationContext())));
			editor.commit();

			loading(false);
			Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
					.show();
			moveToNextActivity();
			// if (response == null) {
			// Toast.makeText(getApplicationContext(), "Did not get response",
			// Toast.LENGTH_LONG).show();
			//
			// loading(false);
			// } else {
			//
			// int responseCode = Integer.parseInt(response.getProperty(
			// "rCode").toString());
			//
			// if (responseCode == 401) {
			// Toast.makeText(getApplicationContext(),
			// "Authentication error", Toast.LENGTH_LONG).show();
			//
			// loading(false);
			// } else if (responseCode == 200) {
			// Toast.makeText(getApplicationContext(),
			// "You have been successfully registered",
			// Toast.LENGTH_LONG).show();
			//
			// SharedPreferences.Editor editor = prefs.edit();
			// editor.putString("registrationId", regid);
			// editor.putString("registeredVersion", String
			// .valueOf(getAppVersion(getApplicationContext())));
			// editor.commit();
			//
			// loading(false);
			//
			// moveToNextActivity();
			// }
			//
			// }
		}

	}
}

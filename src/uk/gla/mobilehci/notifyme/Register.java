package uk.gla.mobilehci.notifyme;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import uk.gla.mobilehci.notifyme.helpers.ApplicationSettings;
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

@SuppressWarnings("deprecation")
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

	private class RegisterInBackground extends
			AsyncTask<Void, Void, HttpResponse> {

		@Override
		protected HttpResponse doInBackground(Void... params) {

			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging
							.getInstance(getApplicationContext());
				}

				regid = gcm.register(ApplicationSettings.gcmSenderID);

				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("gcmID", regid);
				editor.commit();

				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(ApplicationSettings.serverUrl
						+ "register");

				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("email", prefs.getString(
						"userEmail", null)));
				data.add(new BasicNameValuePair("username", prefs.getString(
						"username", null)));
				data.add(new BasicNameValuePair("gcmid", prefs.getString(
						"gcmID", null)));

				post.setEntity(new UrlEncodedFormEntity(data));

				return client.execute(post);

			} catch (Exception ex) {
				Log.e(this.getClass().getName(), ex.toString());
				return null;
			}

		}

		@Override
		protected void onPostExecute(HttpResponse response) {

			if (response == null) {

				Toast.makeText(getApplicationContext(),
						"Error registering to server", Toast.LENGTH_LONG)
						.show();
				loading(false);
			} else {
				try {

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String data = reader.readLine();

					JSONObject obj = new JSONObject(data);
					int status = obj.getInt("rstatus");

					if (status == -1) {

						Toast.makeText(getApplicationContext(),
								"Error registering to server",
								Toast.LENGTH_LONG).show();
						loading(false);
					}

					else {

						SharedPreferences.Editor editor = prefs.edit();

						editor.putString(
								"registeredVersion",
								String.valueOf(getAppVersion(getApplicationContext())));
						editor.commit();

						Toast.makeText(getApplicationContext(),
								"Successfully registered to server",
								Toast.LENGTH_LONG).show();
						loading(false);
						moveToNextActivity();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(getApplicationContext(),
							"Error registering to server", Toast.LENGTH_LONG)
							.show();
					loading(false);
				}
			}
		}

	}

}

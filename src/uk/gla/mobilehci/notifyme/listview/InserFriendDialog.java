package uk.gla.mobilehci.notifyme.listview;

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

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.fragments.EditAddFriends;
import uk.gla.mobilehci.notifyme.helpers.ApplicationSettings;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InserFriendDialog extends DialogFragment {

	private Fragment fragment;

	public InserFriendDialog(Fragment fragment) {
		this.fragment = fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				getActivity());

		final View dialogLayout = View.inflate(getActivity(),
				R.layout.dialog_add_friend, null);

		builder.setView(dialogLayout);

		final EditText email = (EditText) dialogLayout.findViewById(R.id.email);

		builder.setTitle(R.string.addFriendTitle)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								new AddFriend().execute(email.getText()
										.toString());

							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	private class AddFriend extends AsyncTask<String, Void, HttpResponse> {

		private ProgressDialog progDialog = new ProgressDialog(getActivity());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			progDialog.setMessage("Loading...");
			progDialog.setIndeterminate(false);
			progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDialog.setCancelable(true);
			progDialog.show();
		}

		@Override
		protected HttpResponse doInBackground(String... params) {

			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(ApplicationSettings.serverUrl
						+ "register");

				ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
				data.add(new BasicNameValuePair("email", params[0]));

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
						((EditAddFriends) fragment).getActivity()
								.getApplicationContext(), "Server error",
						Toast.LENGTH_LONG).show();

			} else {
				try {

					BufferedReader reader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent(), "UTF-8"));
					String data = reader.readLine();
					System.out.println(data);
					JSONObject obj = new JSONObject(data);
					int status = obj.getInt("rstatus");

					if (status == -1) {

						Toast.makeText(
								((EditAddFriends) fragment).getActivity()
										.getApplicationContext(),
								"Error adding friend, email may not be correct",
								Toast.LENGTH_LONG).show();

					}

					else if (status == 200) {

						Toast.makeText(
								((EditAddFriends) fragment).getActivity()
										.getApplicationContext(),
								"Friend successfully added", Toast.LENGTH_LONG)
								.show();

						((EditAddFriends) fragment).addFriendToList(
								obj.getString("email"),
								obj.getString("username"));

					} else {
						new Exception();
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.toString());
					Toast.makeText(
							((EditAddFriends) fragment).getActivity()
									.getApplicationContext(),
							"Error adding friend", Toast.LENGTH_LONG).show();
				}
			}

			progDialog.dismiss();
		}

	}
}

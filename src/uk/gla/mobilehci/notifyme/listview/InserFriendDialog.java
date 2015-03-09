package uk.gla.mobilehci.notifyme.listview;

import uk.gla.mobilehci.notifyme.R;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InserFriendDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(
				getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.dialog_add_friend, null));

		final View dialogLayout = View.inflate(getActivity(),
				R.layout.dialog_add_friend, null);

		final EditText username = (EditText) dialogLayout
				.findViewById(R.id.username);
		final EditText password = (EditText) dialogLayout
				.findViewById(R.id.email);

		builder.setMessage("Add Friend").setPositiveButton("Submit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!

						String f1 = username.getText().toString();
						String f2 = password.getText().toString();

						Toast.makeText(getActivity(), f1 + "#" + f2 + "#",
								Toast.LENGTH_SHORT).show();

					}
				});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}

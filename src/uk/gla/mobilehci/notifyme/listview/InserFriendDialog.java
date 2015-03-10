package uk.gla.mobilehci.notifyme.listview;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.fragments.EditAddFriends;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
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
								// FIRE ZE MISSILES!
								((EditAddFriends) fragment).addFriendToList(
										email.getText().toString(), email
												.getText().toString());
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}
}

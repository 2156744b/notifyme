package uk.gla.mobilehci.notifyme.listview;

import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.FriendModel;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendListArrayAdapter extends ArrayAdapter<FriendModel> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<FriendModel> data;
	private View.OnLongClickListener rowListenerOptions;

	/**
	 * class ToDoHolder This class is used to hold the ids of an item of the
	 * listview
	 * 
	 * @author Rafael
	 * 
	 */
	public static class ToDoHolder {
		TextView txtUsername;
		TextView txtEmailAddress;
	}

	/**
	 * Constructor of the ToDoArrayAdapter
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param data
	 */
	public FriendListArrayAdapter(Context context, int layoutResourceId,
			ArrayList<FriendModel> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ToDoHolder holder;
		final FriendModel obj = data.get(position);

		// get all the ids only once!! and store it in the holder class. then
		// each time a new row will be created the ids will exist in the holder
		// object
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();

			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ToDoHolder();
			holder.txtUsername = (TextView) row.findViewById(R.id.txtUsername);
			holder.txtEmailAddress = (TextView) row.findViewById(R.id.txtEmail);
			row.setTag(holder);
		} else {

			holder = (ToDoHolder) row.getTag();
		}

		holder.txtUsername.setText(obj.getUsername());
		holder.txtEmailAddress.setText(obj.getEmail());
		rowListenerOptions = new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("Edit Friend").setItems(
						R.array.friend_list_dialog_option,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									data.remove(position);
									FriendListArrayAdapter.this.notifyDataSetChanged();
									break;

								default:
									break;
								}
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				return false;
			}
		};

		row.setOnLongClickListener(rowListenerOptions);
		return row;

	}
}

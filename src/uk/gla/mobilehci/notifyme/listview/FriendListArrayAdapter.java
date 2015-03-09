package uk.gla.mobilehci.notifyme.listview;

import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.FriendModel;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListArrayAdapter extends ArrayAdapter<FriendModel> {

	private String tagString = "FriendListArrayAdapter";
	private Context context;
	private int layoutResourceId;
	private ArrayList<FriendModel> data;
	private FragmentManager manager;
	private Activity activity;
	private View.OnLongClickListener rowListenerOptions;

	/**
	 * class ToDoHolder This class is used to hold the ids of an item of the
	 * listview!!!
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
	public View getView(int position, View convertView, ViewGroup parent) {
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
				// TODO Auto-generated method stub
				Toast.makeText(getContext(),obj.getEmail() + "to be deleted",Toast.LENGTH_SHORT).show();
				return false;
			}
		};

		row.setOnLongClickListener(rowListenerOptions);
//		row.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		})
		return row;

	}
}

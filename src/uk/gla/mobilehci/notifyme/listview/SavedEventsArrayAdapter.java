package uk.gla.mobilehci.notifyme.listview;

import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SavedEventsArrayAdapter extends ArrayAdapter<PublicEvent> {

	private Context context;
	private int layoutResourceId;
	private ArrayList<PublicEvent> data;

	/**
	 * class ToDoHolder This class is used to hold the ids of an item of the
	 * listview
	 * 
	 * @author Rafael
	 * 
	 */
	public static class ToDoHolder {
		ImageView imageListSaved;
		TextView txtListSavedDescription;
		TextView txtListSavedDate;
	}

	/**
	 * Constructor of the ToDoArrayAdapter
	 * 
	 * @param context
	 * @param layoutResourceId
	 * @param data
	 */
	public SavedEventsArrayAdapter(Context context, int layoutResourceId,
			ArrayList<PublicEvent> data) {
		super(context, layoutResourceId, data);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ToDoHolder holder;
		final PublicEvent obj = data.get(position);

		// get all the ids only once!! and store it in the holder class. then
		// each time a new row will be created the ids will exist in the holder
		// object
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();

			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ToDoHolder();
			holder.imageListSaved = (ImageView) row
					.findViewById(R.id.imageListSaved);
			holder.txtListSavedDescription = (TextView) row
					.findViewById(R.id.txtListSavedDescription);
			holder.txtListSavedDate = (TextView) row
					.findViewById(R.id.txtListSavedDate);
			row.setTag(holder);
		} else {

			holder = (ToDoHolder) row.getTag();
		}

		holder.txtListSavedDescription.setText(obj.getDescription());
		holder.txtListSavedDate.setText(obj.getDate());

		switch (obj.getType()) {
		case 1:
			holder.imageListSaved.setBackgroundResource(R.drawable.club);
			break;
		case 2:
			holder.imageListSaved.setBackgroundResource(R.drawable.theatre);
			break;
		case 3:
			holder.imageListSaved.setBackgroundResource(R.drawable.music);
			break;
		case 4:
			holder.imageListSaved.setBackgroundResource(R.drawable.restaurant);
			break;
		case 5:
			holder.imageListSaved.setBackgroundResource(R.drawable.art);
			break;
		default:
			break;
		}
		return row;

	}
}
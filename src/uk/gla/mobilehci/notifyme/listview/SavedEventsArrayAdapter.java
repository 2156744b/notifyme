package uk.gla.mobilehci.notifyme.listview;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.PublicEventActivity;
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.fragments.SavedEvents;
import uk.gla.mobilehci.notifyme.helpers.ShowNotification;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
		TextView txtListSavedLocation;
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
			holder.txtListSavedLocation = (TextView) row
					.findViewById(R.id.txtListSavedLocation);
			row.setTag(holder);
		} else {

			holder = (ToDoHolder) row.getTag();
		}

		holder.txtListSavedDescription.setText(obj.getDescription());
		holder.txtListSavedDate.setText(obj.getDate());
		holder.txtListSavedLocation.setText(obj.getLocationDescription());

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

		// Bitmap imagePoster = ShowNotification.loadBitmap(context, obj.getId()
		// + ".PNG");
		//
		// holder.imageListSaved.setImageBitmap(imagePoster);

		row.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(context, PublicEventActivity.class);
				i.putExtra(PublicEventActivity.PUBLIC_EVENT, data.get(position));
				i.putExtra(PublicEventActivity.TO_SHOW, false);
				context.startActivity(i);
			}
		});

		View.OnLongClickListener rowListenerOptions = new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("Edit Saved Events").setItems(
						R.array.friend_list_dialog_option,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									data.remove(position);
									writeSavedEvents();
									SavedEventsArrayAdapter.this
											.notifyDataSetChanged();
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

	public void writeSavedEvents() {

		File file = new File(context.getFilesDir(), "savedEvents.txt");
		if (file.exists())
			file.delete();
		try {
			PrintWriter printWriter = new PrintWriter(file);
			for (PublicEvent f : data) {
				printWriter.write(f.toString() + "\n");
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package uk.gla.mobilehci.notifyme.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import uk.gla.mobilehci.notifyme.PublicEventActivity;
import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ShowNotification extends BroadcastReceiver {

	private ArrayList<PublicEvent> data;

	@Override
	public void onReceive(Context context, Intent intent) {
		readSavedEvents(context);
		// TODO Auto-generated method stub
		PublicEvent publicEvent = intent.getParcelableExtra("publicEvent");
		System.out.println("Receiver" + publicEvent.getLocationDescription());
		// if (publicEvent != null
		// && ISO8601.returnTime(publicEvent.getDate()) < System
		// .currentTimeMillis() + 2000) {
		// int i;
		// for (i = 0; i < data.size(); i++) {
		// if (data.get(i).getId() == publicEvent.getId())
		// break;
		// }
		// if (i != data.size()) {
		// // standard notification code
		// // / code here!!!
		// }
		// }
		int resID = 0;

		switch (publicEvent.getType()) {
		case 1:
			resID = R.drawable.club;
			break;
		case 2:
			resID = R.drawable.theatre;
			break;
		case 3:
			resID = R.drawable.music;
			break;
		case 4:
			resID = R.drawable.restaurant;
			break;
		case 5:
			resID = R.drawable.art;
			break;
		default:
			break;
		}

		// Build intent for notification content
		Intent viewIntent = new Intent(context, PublicEventActivity.class);

		viewIntent.putExtra(PublicEventActivity.PUBLIC_EVENT, publicEvent);
		viewIntent.putExtra(PublicEventActivity.TO_SHOW, false);

		PendingIntent viewPendingIntent = PendingIntent.getActivity(context, 0,
				viewIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		Bitmap b = loadBitmap(context, publicEvent.getId() + ".PNG");
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(resID).setLargeIcon(b)
				.setContentTitle(publicEvent.getLocationDescription())
				.setContentText(publicEvent.getDate())
				.setContentIntent(viewPendingIntent);

		// Get an instance of the NotificationManager service
		NotificationManagerCompat notificationManager = NotificationManagerCompat
				.from(context);

		// Build the notification and issues it with notification manager.
		notificationManager.notify(publicEvent.getId(),
				notificationBuilder.build());

	}

	private void readSavedEvents(Context context) {
		BufferedReader reader = null;
		File file = new File(context.getFilesDir(), "savedEvents.txt");
		data = new ArrayList<PublicEvent>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			PublicEvent readpublicEvent;
			while ((line = reader.readLine()) != null) {
				split = line.split(";");
				readpublicEvent = new PublicEvent();
				readpublicEvent.setId(Integer.parseInt(split[0]));
				readpublicEvent.setLon(Double.parseDouble(split[1]));
				readpublicEvent.setLat(Double.parseDouble(split[2]));
				readpublicEvent.setPhone(split[3]);
				readpublicEvent.setLocationDescription(split[4]);
				readpublicEvent.setDescription(split[5]);
				readpublicEvent.setPosterUrl(split[6]);
				readpublicEvent.setDate(split[7]);
				readpublicEvent.setType(Integer.parseInt(split[8]));
				readpublicEvent.setUrl(split[8]);
				readpublicEvent.setCreator(split[9]);
				data.add(readpublicEvent);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Bitmap loadBitmap(Context context, String picName) {
		Bitmap b = null;
		FileInputStream fis;
		try {
			fis = context.openFileInput(picName);
			b = BitmapFactory.decodeStream(fis);
			fis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

}

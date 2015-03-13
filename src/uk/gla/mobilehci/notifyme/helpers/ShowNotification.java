package uk.gla.mobilehci.notifyme.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import uk.gla.mobilehci.notifyme.R;
import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

public class ShowNotification extends BroadcastReceiver {

	private ArrayList<PublicEvent> data;

	@Override
	public void onReceive(Context context, Intent intent) {
		readSavedEvents(context);
		// TODO Auto-generated method stub
		PublicEvent get = intent.getParcelableExtra("publicEvent");
		if (get != null
				&& ISO8601.returnTime(get.getDate()) < System
						.currentTimeMillis() + 2000) {
			int i;
			for (i = 0; i < data.size(); i++) {
				if (data.get(i).getId() == get.getId())
					break;
			}
			if (i != data.size()) {
				// standard notification code
				// / code here!!!
			}
		}
		int resID = 0;

		switch (get.getType()) {
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
		
		// diavazw to bitmap KAI SET STO LARGE ICON
		//https://developer.android.com/training/wearables/notifications/creating.html
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
			.setSmallIcon(resID)
				.setLargeIcon(
						BitmapFactory.decodeResource(context.getResources(),
								R.drawable.friend_inv))
				.setContentTitle("NotifyMe App")
				.setContentText("Click To View More");
	
		
		NotificationManagerCompat notificationManager =
		        NotificationManagerCompat.from(context);

		notificationManager.notify(get.getId(), mBuilder.build());
		Toast.makeText(context, "EMPIKA OMWS", Toast.LENGTH_SHORT).show();
	}

	private void readSavedEvents(Context context) {
		BufferedReader reader = null;
		File file = new File(context.getFilesDir(), "savedEvents.txt");
		data = new ArrayList<PublicEvent>();
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			String[] split;
			PublicEvent publicEvent;
			while ((line = reader.readLine()) != null) {
				split = line.split(";");
				publicEvent = new PublicEvent();
				publicEvent.setId(Integer.parseInt(split[0]));
				publicEvent.setLon(Double.parseDouble(split[1]));
				publicEvent.setLat(Double.parseDouble(split[2]));
				publicEvent.setPhone(split[3]);
				publicEvent.setLocationDescription(split[4]);
				publicEvent.setDescription(split[5]);
				publicEvent.setPosterUrl(split[6]);
				publicEvent.setDate(split[7]);
				publicEvent.setType(Integer.parseInt(split[8]));
				publicEvent.setUrl(split[8]);
				publicEvent.setCreator(split[9]);
				data.add(publicEvent);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

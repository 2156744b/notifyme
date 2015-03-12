package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.helpers.Receiver;
import uk.gla.mobilehci.notifyme.R;

import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SimpleNotification extends Fragment {
	
	public static final String ACTION_DEMAND = "com.androidweardocs.ACTION_DEMAND";
	public static final String EXTRA_MESSAGE = "com.androidweardocs.EXTRA_MESSAGE";
	public static final String EXTRA_VOICE_REPLY = "com.androidweardocs.EXTRA_VOICE_REPLY";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//setContentView(R.layout.wearable);
		
		// Inflate the layout for this fragment

		View wearableView = inflater.inflate(R.layout.wearable,
				container, false);

		Button button = (Button) wearableView.findViewById(R.id.sendNotification);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Create an Intent for the demand
				Intent demandIntent = new Intent(v.getContext(),
						Receiver.class).putExtra(EXTRA_MESSAGE,
						"Reply icon selected.").setAction(ACTION_DEMAND);

				// Create a pending intent using the local broadcast receiver
				PendingIntent demandPendingIntent = PendingIntent.getBroadcast(
						v.getContext(), 0, demandIntent, 0);

				// Create RemoteInput object for a voice reply (demand)
				String replyLabel = getResources().getString(R.string.app_name);
				RemoteInput remoteInput = new RemoteInput.Builder(
						EXTRA_VOICE_REPLY).setLabel(replyLabel).build();

				// Create a wearable action
				NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
						R.drawable.ic_reply_icon,
						getString(R.string.reply_label), demandPendingIntent)
						.addRemoteInput(remoteInput).build();

				// Create a wearable extender for a notification
				NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
						.addAction(replyAction);

				// Create a notification and extend it for the wearable
				Notification notification = new NotificationCompat.Builder(v.getContext())
						.setContentTitle("Hello Wearable!")
						.setContentText("First wearable demand.")
						.setSmallIcon(R.drawable.ic_launcher)
						.extend(wearableExtender).build();
				// Get the notification manager
				NotificationManagerCompat notificationManager = NotificationManagerCompat
						.from(v.getContext());

				// Dispatch the extended notification
				int notificationId = 1;
				notificationManager.notify(notificationId, notification);
			}
		});
		
		return wearableView;
	}


}

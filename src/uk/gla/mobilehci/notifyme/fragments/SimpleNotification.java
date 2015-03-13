package uk.gla.mobilehci.notifyme.fragments;

import uk.gla.mobilehci.notifyme.helpers.Receiver;
import uk.gla.mobilehci.notifyme.helpers.ShowNotification;
import uk.gla.mobilehci.notifyme.R;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class SimpleNotification extends Fragment {

	public static final String EXTRA_MESSAGE = "uk.gla.mobilehci.notifyme.EXTRA_MESSAGE";
	public static final String EXTRA_VOICE_REPLY = "uk.gla.mobilehci.notifyme.EXTRA_VOICE_REPLY";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View wearableView = inflater.inflate(R.layout.wearable, container,
				false);

		Button button = (Button) wearableView
				.findViewById(R.id.sendNotification);

		Button button1 = (Button) wearableView.findViewById(R.id.test);

		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getActivity(), ShowNotification.class);
				myIntent.putExtra("value", "dikse toooo");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getActivity(), 0, myIntent, 0);

				AlarmManager alarmManager = (AlarmManager) getActivity()
						.getSystemService(Context.ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC,
						System.currentTimeMillis() + 2000, pendingIntent);
			}
		});

		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// standard notification code
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						v.getContext()).setSmallIcon(R.drawable.ic_launcher)
						.setContentTitle("Content TItle")
						.setContentText("setContentText");

				Intent firstIntent = new Intent(v.getContext(), Receiver.class);
				firstIntent.putExtra(Receiver.NOTIFICATION_ID_STRING,
						"firstIntent icon selected");
				firstIntent.putExtra(Receiver.WEAR_ACTION,
						Receiver.NOTIFICATION_1);

				PendingIntent pendingIntentNotify = PendingIntent.getBroadcast(
						v.getContext(), 1, firstIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// text shown in notification
				String notifyAgainText = "Put string of the notification that will be presented";

				// Wear action - this action will be shown only on Android Wear
				// devices
				// Set action icon, text and pending intent which will be
				// executed on click
				// When user clicks on this icon he will "snooze" notification
				NotificationCompat.Action actionNotifyNextTime = new NotificationCompat.Action.Builder(
						R.drawable.ic_launcher, notifyAgainText,
						pendingIntentNotify).build();

				// ----------------Second Menu on
				// Wathc------------------------------

				Intent secondIntent = new Intent(v.getContext(), Receiver.class);
				secondIntent.putExtra(Receiver.NOTIFICATION_ID_STRING,
						"firstIntent icon selected");
				secondIntent.putExtra(Receiver.WEAR_ACTION,
						Receiver.NOTIFICATION_2);

				PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(
						v.getContext(), 2, secondIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// When user clicks on this icon he will cancel his ticket and
				// remove himself from the queue
				NotificationCompat.Action actionCancel = new NotificationCompat.Action.Builder(
						R.drawable.ic_launcher, "Cancel", pendingIntentCancel)
						.build();

				// ----------------Third menu on
				// watch------------------------------
				Intent thirdIntent = new Intent(v.getContext(), Receiver.class);
				thirdIntent.putExtra(Receiver.NOTIFICATION_ID_STRING,
						"Reply icon selected.");
				thirdIntent.putExtra(Receiver.WEAR_ACTION,
						Receiver.NOTIFICATION_3);

				PendingIntent replyPending = PendingIntent.getBroadcast(
						v.getContext(), 3, secondIntent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// Create RemoteInput object for a voice reply (demand)
				String replyLabel = getResources().getString(R.string.app_name);
				RemoteInput remoteInput = new RemoteInput.Builder(
						EXTRA_VOICE_REPLY).setLabel(replyLabel).build();

				NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
						R.drawable.ic_launcher,
						getString(R.string.reply_label), replyPending)
						.addRemoteInput(remoteInput).build();

				// Create new WearableExtender object and add actions
				NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
				extender.addAction(actionNotifyNextTime);
				extender.addAction(actionCancel);
				extender.addAction(replyAction);

				// Extend Notification builder
				mBuilder.extend(extender);

				v.getContext();
				// Get notification manager
				NotificationManager mNotificationManager = (NotificationManager) v
						.getContext().getSystemService(
								Context.NOTIFICATION_SERVICE);

				// show notification
				mNotificationManager.notify(222, mBuilder.build());
			}
		});

		return wearableView;
	}

}

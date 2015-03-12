package uk.gla.mobilehci.notifyme.helpers;

import uk.gla.mobilehci.notifyme.fragments.SimpleNotification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

	public static final String NOTIFICATION_ID_STRING = "NotificationId";
	public static final String WEAR_ACTION = "WearAction";
	public static final int NOTIFICATION_1 = 0;
	public static final int NOTIFICATION_2 = 1;
	public static final int NOTIFICATION_3 = 2;
	

	@Override
	public void onReceive(Context context, Intent intent) {

		if (intent != null) {

			int notificationId = intent.getIntExtra(NOTIFICATION_ID_STRING, 0);
			Log.v("MyTag", "action value:  " + String.valueOf(notificationId));
			
		//	NotificationManager manager = (NotificationManager) context
		//			.getSystemService(Context.NOTIFICATION_SERVICE);
		//	manager.cancel(notificationId);

			int action = intent.getIntExtra(WEAR_ACTION, 0);
			Log.v("MyTag", "action value:  " + String.valueOf(action));
			switch (action) {
			case NOTIFICATION_1:
				// Code for NOTIFICATION_1
				// Record voice command---

			case NOTIFICATION_2:
				// code for NOTIFICATION_2
				break;
			case NOTIFICATION_3:
				// code for NOTIFICATION_3
				//voice message
				String message = intent
						.getStringExtra(SimpleNotification.EXTRA_MESSAGE);
				Log.v("MyTag", "Extra message from intent = " + message);
				Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
				CharSequence reply = remoteInput
						.getCharSequence(SimpleNotification.EXTRA_VOICE_REPLY);
				Log.v("MyTag", "User reply from wearable: " + reply);

				Toast.makeText(context, reply, Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}

		}

	}
}

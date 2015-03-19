package uk.gla.mobilehci.notifyme.gcmservice;

import uk.gla.mobilehci.notifyme.R;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService {
	public static final int NOTIFICATION_ID = -1;
	private final String TAG = "notification";

	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle

			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				Log.i(TAG, "Received: " + extras.toString());

				NotificationManager mNotificationManager = (NotificationManager) this
						.getSystemService(Context.NOTIFICATION_SERVICE);

				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
						this)
						.setAutoCancel(true)
						.setContentTitle(
								String.valueOf(extras.getLong("timestamp")))
						.setContentText(
								extras.getString("description") + " "
										+ extras.getString("locDescription"))
						.setSmallIcon(R.drawable.friend_inv)
						.setLargeIcon(
								((BitmapDrawable) this.getResources()
										.getDrawable(R.drawable.friend_inv))
										.getBitmap());

				mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}
}

// 03-19 23:09:52.330: I/notification(32249): Received:
// Bundle[{locDescription=hshsssh, lat=55.87597327769168,
// lon=-4.273384585976601, from=509418195746, timestamp=1426806586594,
// android.support.content.wakelockid=1, collapse_key=do_not_collapse}]

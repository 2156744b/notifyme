package uk.gla.mobilehci.notifyme.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;
import android.widget.Toast;

import uk.gla.mobilehci.notifyme.fragments.SimpleNotification;

public class Receiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SimpleNotification.ACTION_DEMAND)) {String message =
                intent.getStringExtra(SimpleNotification.EXTRA_MESSAGE);
            Log.v("MyTag", "Extra message from intent = " + message);
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            CharSequence reply = remoteInput.getCharSequence(SimpleNotification.EXTRA_VOICE_REPLY);
            Log.v("MyTag", "User reply from wearable: " + reply);
            
            Toast.makeText(context, reply, Toast.LENGTH_LONG).show();
        }
    }

}

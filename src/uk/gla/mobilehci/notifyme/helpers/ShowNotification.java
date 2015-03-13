package uk.gla.mobilehci.notifyme.helpers;

import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ShowNotification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
//		PublicEvent get = intent.getParcelableExtra("publicEvent");
//		if (get != null
//				&& ISO8601.returnTime(get.getDate()) < System
//						.currentTimeMillis() + 2000)
//			Toast.makeText(context, get.getId(), Toast.LENGTH_SHORT).show();
		Toast.makeText(context, "EMPIKA OMWS", Toast.LENGTH_SHORT).show();
	}

}

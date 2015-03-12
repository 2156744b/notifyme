package uk.gla.mobilehci.notifyme;

import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.fragments.FindPublicEventsFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PublicEventActivity extends Activity {

	public static final String PUBLIC_EVENT = "getPublicEvent";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_event_layout);

		Intent intent = getIntent();
		PublicEvent publicEvent = (PublicEvent) intent
				.getParcelableExtra(PUBLIC_EVENT);

		TextView dateTime = (TextView) findViewById(R.id.txtDateTime);
		ImageView eventPoster = (ImageView) findViewById(R.id.imagePublicEvent);
		TextView locationDescription = (TextView) findViewById(R.id.txtLocationDescription);
		TextView eventDescription = (TextView) findViewById(R.id.txtEventDescription);
		TextView url = (TextView) findViewById(R.id.txtURL);
		TextView tel = (TextView) findViewById(R.id.txtTel);
		ImageView typeOfEvent = (ImageView) findViewById(R.id.imageTypeOfEvent);

		dateTime.setText(publicEvent.getDate());
		locationDescription.setText(publicEvent.getLocationDescription());
		eventDescription.setText(publicEvent.getDescription());
		url.setText(publicEvent.getUrl());
		tel.setText(publicEvent.getPhone());
		typeOfEvent.setBackgroundResource(R.drawable.music);

		switch (publicEvent.getType()) {
		case 1:
			typeOfEvent.setImageResource(R.drawable.club);
			break;
		case 2:
			typeOfEvent.setImageResource(R.drawable.theatre);
			break;
		case 3:
			typeOfEvent.setImageResource(R.drawable.music);
			break;
		case 4:
			typeOfEvent.setImageResource(R.drawable.restaurant);
			break;
		case 5:
			typeOfEvent.setImageResource(R.drawable.art);
			break;
		default:
			break;
		}
		eventPoster.setImageBitmap(FindPublicEventsFragment.images.get(publicEvent
				.getPosterUrl()));
	}
}

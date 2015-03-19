package uk.gla.mobilehci.notifyme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.fragments.FindPublicEventsFragment;
import uk.gla.mobilehci.notifyme.helpers.ShowNotification;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PublicEventActivity extends Activity {

	private ArrayList<PublicEvent> data = new ArrayList<PublicEvent>();
	public static final String PUBLIC_EVENT = "getPublicEvent";
	public static final String TO_SHOW = "toShow";

	private PublicEvent publicEvent;
	private boolean toShowSave = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_event_layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		Intent intent = getIntent();
		publicEvent = (PublicEvent) intent.getParcelableExtra(PUBLIC_EVENT);
		toShowSave = intent.getBooleanExtra(TO_SHOW, true);

		int toRemove = intent.getIntExtra("toCancel", -1);

		if (toRemove != -1) {
			NotificationManager manager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.cancel(toRemove);
		}

		TextView dateTime = (TextView) findViewById(R.id.txtDateTime);
		ImageView eventPoster = (ImageView) findViewById(R.id.imagePublicEvent);
		TextView locationDescription = (TextView) findViewById(R.id.txtLocationDescription);
		TextView eventDescription = (TextView) findViewById(R.id.txtEventDescription);
		TextView url = (TextView) findViewById(R.id.txtURL);
		TextView tel = (TextView) findViewById(R.id.txtTel);
		ImageView typeOfEvent = (ImageView) findViewById(R.id.imageTypeOfEvent);
		LinearLayout layoutSave = (LinearLayout) findViewById(R.id.layoutSave);
		LinearLayout layoutHelper = (LinearLayout) findViewById(R.id.layoutHelper);

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

		Bitmap imagePoster = ShowNotification.loadBitmap(
				getApplicationContext(), publicEvent.getId() + ".PNG");
		eventPoster.setImageBitmap(imagePoster);

		if (toShowSave) {

			layoutSave.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					readSavedEvents();
					boolean proceed = writeSavedEvents();

					if (proceed) {

						Intent myIntent = new Intent(getApplicationContext(),
								ShowNotification.class);
						myIntent.putExtra("publicEvent", publicEvent);
						System.out.println("Get"
								+ publicEvent.getLocationDescription());
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getApplicationContext(), 0,
										myIntent,
										PendingIntent.FLAG_UPDATE_CURRENT);
						long milis = -1;
						try {
							Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm")
									.parse(publicEvent.getDate());
							milis = date.getTime();

						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						AlarmManager alarmManager = (AlarmManager) getApplicationContext()
								.getSystemService(Context.ALARM_SERVICE);
						alarmManager
								.set(AlarmManager.RTC, milis, pendingIntent);
					}
					finish();
				}
			});
		} else {
			layoutHelper.removeView(layoutSave);
		}
	}

	private void readSavedEvents() {
		BufferedReader reader = null;
		File file = new File(this.getFilesDir(), "savedEvents.txt");
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
				readpublicEvent.setUrl(split[9]);
				readpublicEvent.setCreator(split[10]);
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

	private boolean writeSavedEvents() {
		int i;
		for (i = 0; i < data.size(); i++) {
			if (data.get(i).getId() == publicEvent.getId())
				return false;
		}
		if (i == data.size() || data.size() == 0) {
			data.add(publicEvent);
			File file = new File(this.getFilesDir(), "savedEvents.txt");
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
		return true;
	}
}

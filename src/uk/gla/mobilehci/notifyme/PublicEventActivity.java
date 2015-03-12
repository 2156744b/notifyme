package uk.gla.mobilehci.notifyme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import uk.gla.mobilehci.notifyme.datamodels.PublicEvent;
import uk.gla.mobilehci.notifyme.fragments.FindPublicEventsFragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PublicEventActivity extends Activity {

	private ArrayList<PublicEvent> data = new ArrayList<PublicEvent>();
	public static final String PUBLIC_EVENT = "getPublicEvent";
	private PublicEvent publicEvent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.public_event_layout);
		Intent intent = getIntent();
		publicEvent = (PublicEvent) intent.getParcelableExtra(PUBLIC_EVENT);

		TextView dateTime = (TextView) findViewById(R.id.txtDateTime);
		ImageView eventPoster = (ImageView) findViewById(R.id.imagePublicEvent);
		TextView locationDescription = (TextView) findViewById(R.id.txtLocationDescription);
		TextView eventDescription = (TextView) findViewById(R.id.txtEventDescription);
		TextView url = (TextView) findViewById(R.id.txtURL);
		TextView tel = (TextView) findViewById(R.id.txtTel);
		ImageView typeOfEvent = (ImageView) findViewById(R.id.imageTypeOfEvent);
		LinearLayout layoutSave = (LinearLayout) findViewById(R.id.layoutSave);

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
		eventPoster.setImageBitmap(FindPublicEventsFragment.images
				.get(publicEvent.getPosterUrl()));

		layoutSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				readSavedEvents();
				writeSavedEvents();
				finish();
			}
		});
	}

	private void readSavedEvents() {
		BufferedReader reader = null;
		File file = new File(this.getFilesDir(), "savedEvents.txt");
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

	private void writeSavedEvents() {
		int i;
		for (i = 0; i < data.size(); i++) {
			if (data.get(i).getId() == publicEvent.getId())
				break;
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

	}
}

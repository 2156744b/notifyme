package uk.gla.mobilehci.notifyme.datamodels;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class PublicEvent implements Parcelable {

	private int id;
	private double lon;
	private double lat;
	private String phone;
	private String locationDescription;
	private String description;
	private String posterUrl;
	private String date;
	private int type;
	private String url;
	private String creator;

	@Override
	public String toString() {
		return id + ";" + lon + ";" + lat + ";" + phone + ";"
				+ locationDescription + ";" + description + ";" + posterUrl
				+ ";" + date + ";" + type + ";" + url + ";" + creator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeDouble(lon);
		dest.writeDouble(lat);
		dest.writeString(phone);
		dest.writeString(locationDescription);
		dest.writeString(description);
		dest.writeString(posterUrl);
		dest.writeString(date);
		dest.writeInt(type);
		dest.writeString(url);
		dest.writeString(url);

	}

	public static final Parcelable.Creator<PublicEvent> CREATOR = new Parcelable.Creator<PublicEvent>() {
		public PublicEvent createFromParcel(Parcel in) {
			PublicEvent publicEvent = new PublicEvent();
			publicEvent.setId(in.readInt());
			publicEvent.setLon(in.readDouble());
			publicEvent.setLat(in.readDouble());
			publicEvent.setPhone(in.readString());
			publicEvent.setLocationDescription(in.readString());
			publicEvent.setDescription(in.readString());
			publicEvent.setPosterUrl(in.readString());
			publicEvent.setDate(in.readString());
			publicEvent.setType(in.readInt());
			publicEvent.setUrl(in.readString());
			publicEvent.setCreator(in.readString());
			return publicEvent;
		}

		public PublicEvent[] newArray(int size) {
			return new PublicEvent[size];
		}
	};
}

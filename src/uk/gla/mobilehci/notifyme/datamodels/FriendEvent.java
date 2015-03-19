package uk.gla.mobilehci.notifyme.datamodels;

public class FriendEvent {

	public int id;
	public String creator;
	public String description;
	public String timestamp;
	public double lat;
	public double lon;
	public String locdescription;

	public FriendEvent(int id, String creator, String description,
			String timestamp, double lat, double lon, String locdesc) {
		this.id = id;
		this.creator = creator;
		this.description = description;
		this.timestamp = timestamp;
		this.lat = lat;
		this.lon = lon;
		this.locdescription = locdesc;
	}

}

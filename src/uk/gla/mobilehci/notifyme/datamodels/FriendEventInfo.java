package uk.gla.mobilehci.notifyme.datamodels;

import java.util.ArrayList;

public class FriendEventInfo {

	private String id;
	private String locationDescription;
	private String eventDescription;
	private ArrayList<String> friendList;

	public String toString() {
		String out = id + ";" + locationDescription + ";" + eventDescription;
		for (String f : friendList) {
			out += ";" + f;
		}
		return out;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public ArrayList<String> getFriendList() {
		return friendList;
	}

	public void setFriendList(ArrayList<String> friendList) {
		this.friendList = friendList;
	}

}

package uk.gla.mobilehci.notifyme.datamodels;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendModel implements Parcelable {

	private String username;
	private String email;

	public FriendModel(String username, String email) {
		this.username = username;
		this.email = email;
	}

	public FriendModel(Parcel in) {
		username = in.readString();
		email = in.readString();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return username + ";" + email;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(email);
	}

	public static final Parcelable.Creator<FriendModel> CREATOR = new Parcelable.Creator<FriendModel>() {
		public FriendModel createFromParcel(Parcel in) {
			return new FriendModel(in);
		}

		public FriendModel[] newArray(int size) {
			return new FriendModel[size];
		}
	};

}

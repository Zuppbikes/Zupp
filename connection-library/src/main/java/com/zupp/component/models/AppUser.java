package com.zupp.component.models;


import android.os.Parcel;
import android.os.Parcelable;

public class AppUser implements Parcelable {
    public String id;
    public String fullName;
    public String access_token;
    public String countryCode;
    public String phoneNumber;
    public String email;
    public String profileImage;
    public String username;
    public boolean isVerified;
    public String dateCreated;
    public String lastUpdated;
    public String initiative;
    public String joinDate;
    public String userType;
    public String city;
    public double weekPoints;
    public double dayPoints;
    public double impactPoints;

    public AppUser() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.access_token);
        dest.writeString(this.countryCode);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.profileImage);
        dest.writeString(this.username);
        dest.writeByte(this.isVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.dateCreated);
        dest.writeString(this.lastUpdated);
        dest.writeString(this.initiative);
        dest.writeString(this.joinDate);
        dest.writeString(this.userType);
        dest.writeString(this.city);
        dest.writeDouble(this.weekPoints);
        dest.writeDouble(this.dayPoints);
        dest.writeDouble(this.impactPoints);
    }

    protected AppUser(Parcel in) {
        this.id = in.readString();
        this.fullName = in.readString();
        this.access_token = in.readString();
        this.countryCode = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.profileImage = in.readString();
        this.username = in.readString();
        this.isVerified = in.readByte() != 0;
        this.dateCreated = in.readString();
        this.lastUpdated = in.readString();
        this.initiative = in.readString();
        this.joinDate = in.readString();
        this.userType = in.readString();
        this.city = in.readString();
        this.weekPoints = in.readDouble();
        this.dayPoints = in.readDouble();
        this.impactPoints = in.readDouble();
    }

    public static final Creator<AppUser> CREATOR = new Creator<AppUser>() {
        @Override
        public AppUser createFromParcel(Parcel source) {
            return new AppUser(source);
        }

        @Override
        public AppUser[] newArray(int size) {
            return new AppUser[size];
        }
    };
}

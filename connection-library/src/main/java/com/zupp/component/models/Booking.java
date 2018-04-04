package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by riteshdubey on 3/18/18.
 */

public class Booking implements Parcelable {
    public String id;
    public String expectedTimeOfReturn;
    public float freeKms;
    public float deposit;
    public boolean fuelExcluded;
    public String timeOfRent;
    public String customerPhoneNumber;
    public String status;
    public Vehicle vehicle;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.expectedTimeOfReturn);
        dest.writeFloat(this.freeKms);
        dest.writeFloat(this.deposit);
        dest.writeByte(this.fuelExcluded ? (byte) 1 : (byte) 0);
        dest.writeString(this.timeOfRent);
        dest.writeString(this.customerPhoneNumber);
        dest.writeString(this.status);
        dest.writeParcelable(this.vehicle, flags);
    }

    public Booking() {
    }

    protected Booking(Parcel in) {
        this.id = in.readString();
        this.expectedTimeOfReturn = in.readString();
        this.freeKms = in.readFloat();
        this.deposit = in.readFloat();
        this.fuelExcluded = in.readByte() != 0;
        this.timeOfRent = in.readString();
        this.customerPhoneNumber = in.readString();
        this.status = in.readString();
        this.vehicle = in.readParcelable(Vehicle.class.getClassLoader());
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel source) {
            return new Booking(source);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };
}

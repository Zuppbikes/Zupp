package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {
    public String id;
    public String createdOn;
    public boolean active;
    public String image;
    public String vendorId;
    public int __v;
    public String brand;
    public String model;
    public String status;
    public String lastServiceDate;
    public double lastServiceDistanceReading;
    public String trackingDeviceIMEI;
    public String emissionExpiry;
    public String emissionNumber;
    public String permitExpiry;
    public String permitNumber;
    public String insuranceExpiry;
    public String insuranceNumber;
    public String registrationExpiry;
    public String registrationNumber;
    public String _id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.createdOn);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
        dest.writeString(this.image);
        dest.writeString(this.vendorId);
        dest.writeInt(this.__v);
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.status);
        dest.writeString(this.lastServiceDate);
        dest.writeDouble(this.lastServiceDistanceReading);
        dest.writeString(this.trackingDeviceIMEI);
        dest.writeString(this.emissionExpiry);
        dest.writeString(this.emissionNumber);
        dest.writeString(this.permitExpiry);
        dest.writeString(this.permitNumber);
        dest.writeString(this.insuranceExpiry);
        dest.writeString(this.insuranceNumber);
        dest.writeString(this.registrationExpiry);
        dest.writeString(this.registrationNumber);
        dest.writeString(this._id);
    }

    public Vehicle() {
    }

    protected Vehicle(Parcel in) {
        this.id = in.readString();
        this.createdOn = in.readString();
        this.active = in.readByte() != 0;
        this.image = in.readString();
        this.vendorId = in.readString();
        this.__v = in.readInt();
        this.brand = in.readString();
        this.model = in.readString();
        this.status = in.readString();
        this.lastServiceDate = in.readString();
        this.lastServiceDistanceReading = in.readDouble();
        this.trackingDeviceIMEI = in.readString();
        this.emissionExpiry = in.readString();
        this.emissionNumber = in.readString();
        this.permitExpiry = in.readString();
        this.permitNumber = in.readString();
        this.insuranceExpiry = in.readString();
        this.insuranceNumber = in.readString();
        this.registrationExpiry = in.readString();
        this.registrationNumber = in.readString();
        this._id = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}

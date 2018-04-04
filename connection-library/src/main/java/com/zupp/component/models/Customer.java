package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Customer implements Parcelable {
    public String id;
    public String _id;
    public String token;
    public String drivingLicenseNumber;
    public String email;
    public String phoneNumber;
    public String countryCode;
    public String name;
    @SerializedName("token-id")
    public int token_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this._id);
        dest.writeString(this.token);
        dest.writeString(this.drivingLicenseNumber);
        dest.writeString(this.email);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.countryCode);
        dest.writeString(this.name);
        dest.writeInt(this.token_id);
    }

    public Customer() {
    }

    protected Customer(Parcel in) {
        this.id = in.readString();
        this._id = in.readString();
        this.token = in.readString();
        this.drivingLicenseNumber = in.readString();
        this.email = in.readString();
        this.phoneNumber = in.readString();
        this.countryCode = in.readString();
        this.name = in.readString();
        this.token_id = in.readInt();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel source) {
            return new Customer(source);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };
}

package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CustomerData implements Parcelable {
    public Booking booking;
    public boolean hasBooking;
    public Customer customer;
    public String svcToken;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.booking, flags);
        dest.writeByte(this.hasBooking ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.customer, flags);
        dest.writeString(this.svcToken);
    }

    public CustomerData() {
    }

    protected CustomerData(Parcel in) {
        this.booking = in.readParcelable(Booking.class.getClassLoader());
        this.hasBooking = in.readByte() != 0;
        this.customer = in.readParcelable(Customer.class.getClassLoader());
        this.svcToken = in.readString();
    }

    public static final Parcelable.Creator<CustomerData> CREATOR = new Parcelable.Creator<CustomerData>() {
        @Override
        public CustomerData createFromParcel(Parcel source) {
            return new CustomerData(source);
        }

        @Override
        public CustomerData[] newArray(int size) {
            return new CustomerData[size];
        }
    };
}

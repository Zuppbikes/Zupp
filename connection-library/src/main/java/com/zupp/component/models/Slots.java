package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Slots implements Parcelable {
    public float amount;
    public String type;
    public String date;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.amount);
        dest.writeString(this.type);
        dest.writeString(this.date);
    }

    public Slots() {
    }

    protected Slots(Parcel in) {
        this.amount = in.readFloat();
        this.type = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Slots> CREATOR = new Parcelable.Creator<Slots>() {
        @Override
        public Slots createFromParcel(Parcel source) {
            return new Slots(source);
        }

        @Override
        public Slots[] newArray(int size) {
            return new Slots[size];
        }
    };
}

package com.zupp.component.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Invoice implements Parcelable {
    public String customerEmail;
    public String customerName;
    public String invoiceId;
    public float totalTax;
    public List<Slots> slots;
    public float cgst;
    public float sgst;
    public float finalBill;
    public float total;
    public String actualTimeOfReturn;
    public float discount;
    public float taxableTotal;
    public int __v;
    public String vendorId;
    public String status;
    public String expectedTimeOfReturn;
    public String customerPhoneNumber;
    public String timeOfRent;
    public int freeKms;
    public boolean fuelExcluded;
    public int deposit;
    public Customer customerId;
    public Vehicle vehicleId;
    public String _id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.customerEmail);
        dest.writeString(this.customerName);
        dest.writeString(this.invoiceId);
        dest.writeFloat(this.totalTax);
        dest.writeTypedList(this.slots);
        dest.writeFloat(this.cgst);
        dest.writeFloat(this.sgst);
        dest.writeFloat(this.finalBill);
        dest.writeFloat(this.total);
        dest.writeString(this.actualTimeOfReturn);
        dest.writeFloat(this.discount);
        dest.writeFloat(this.taxableTotal);
        dest.writeInt(this.__v);
        dest.writeString(this.vendorId);
        dest.writeString(this.status);
        dest.writeString(this.expectedTimeOfReturn);
        dest.writeString(this.customerPhoneNumber);
        dest.writeString(this.timeOfRent);
        dest.writeInt(this.freeKms);
        dest.writeByte(this.fuelExcluded ? (byte) 1 : (byte) 0);
        dest.writeInt(this.deposit);
        dest.writeParcelable(this.customerId, flags);
        dest.writeParcelable(this.vehicleId, flags);
        dest.writeString(this._id);
    }

    public Invoice() {
    }

    protected Invoice(Parcel in) {
        this.customerEmail = in.readString();
        this.customerName = in.readString();
        this.invoiceId = in.readString();
        this.totalTax = in.readFloat();
        this.slots = in.createTypedArrayList(Slots.CREATOR);
        this.cgst = in.readFloat();
        this.sgst = in.readFloat();
        this.finalBill = in.readFloat();
        this.total = in.readFloat();
        this.actualTimeOfReturn = in.readString();
        this.discount = in.readFloat();
        this.taxableTotal = in.readFloat();
        this.__v = in.readInt();
        this.vendorId = in.readString();
        this.status = in.readString();
        this.expectedTimeOfReturn = in.readString();
        this.customerPhoneNumber = in.readString();
        this.timeOfRent = in.readString();
        this.freeKms = in.readInt();
        this.fuelExcluded = in.readByte() != 0;
        this.deposit = in.readInt();
        this.customerId = in.readParcelable(Customer.class.getClassLoader());
        this.vehicleId = in.readParcelable(Vehicle.class.getClassLoader());
        this._id = in.readString();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel source) {
            return new Invoice(source);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };
}

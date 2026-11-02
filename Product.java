package com.example.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private int id;
    private String name;
    private double price;
    private int quantity;

    public Product(int id, String name, double price, int quantity) {
        this.id = id; this.name = name; this.price = price; this.quantity = quantity;
    }

    protected Product(Parcel in) {
        id = in.readInt(); name = in.readString(); price = in.readDouble(); quantity = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override public Product createFromParcel(Parcel in) { return new Product(in); }
        @Override public Product[] newArray(int size) { return new Product[size]; }
    };

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id); parcel.writeString(name); parcel.writeDouble(price); parcel.writeInt(quantity);
    }
}

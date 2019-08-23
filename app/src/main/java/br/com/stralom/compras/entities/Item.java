package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Item implements Parcelable {
    protected double amount;
    protected double total;

    Item(double amount) {
        this.amount = amount;
    }

    Item(double amount, double total) {
        this.amount = amount;
        this.total = total;
    }

    Item() {
    }


    protected Item(Parcel in) {
        amount = in.readDouble();
        total = in.readDouble();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    protected String formatAmount(double amount){
        if(total % 1 == 0){
            return String.valueOf((int) amount);
        } else {
            return String.format("%.2f",amount);
        }
    }



    public String getFormattedAmount(){
        return formatAmount(this.amount);
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotal(double amount, double productPrice){
        this.total =  amount * productPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(amount);
        parcel.writeDouble(total);
    }
}

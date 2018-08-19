package br.com.stralom.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Item implements Parcelable {
    protected Long id;
    protected double amount;
    protected double total;
    protected Product product;

    Item(double amount, Product product) {
        this.amount = amount;
        setTotal(amount,product.getPrice());
        this.product = product;
    }

    Item(Long id, double amount, double total, Product product) {
        this.id = id;
        this.amount = amount;
        this.total = total;
        this.product = product;
    }

    Item() {
    }


    protected Item(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        amount = in.readDouble();
        total = in.readDouble();
        product = in.readParcelable(Product.class.getClassLoader());
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(id);
        }
        parcel.writeDouble(amount);
        parcel.writeDouble(total);
        parcel.writeParcelable(product, i);
    }
}

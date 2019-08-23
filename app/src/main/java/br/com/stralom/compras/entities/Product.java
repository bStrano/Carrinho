package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class Product implements Parcelable,Cloneable {
    private String id;
    private String name;
    private double price;
    private Category category;

    private ItemCart itemCart;
    private ItemStock itemStock;


    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        price = in.readDouble();
        category = in.readParcelable(Category.class.getClassLoader());
        itemCart = in.readParcelable(ItemCart.class.getClassLoader());
        itemStock = in.readParcelable(ItemStock.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(itemCart, flags);
        dest.writeParcelable(itemStock, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public ItemCart getItemCart() {
        return itemCart;
    }

    public void setItemCart(ItemCart itemCart) {
        this.itemCart = itemCart;
    }

    public Product() {
    }


    public Product getClone(){
        try {
            return (Product) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product(String id, String name, double price, Category category, ItemCart itemCart, ItemStock itemStock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.itemCart = itemCart;
        this.itemStock = itemStock;
    }

    public Product(String id,String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.itemCart = new ItemCart();
        this.itemStock = new ItemStock();
    }


    public Map<String,Object> toJson(){
        Map<String, Object> product = new HashMap<>();
        Map<String,Object> itemcart = new HashMap<>();
        itemcart.put("amount",this.itemCart.amount);
        Map<String,Object> itemStock = new HashMap<>();
        itemStock.put("actualAmount",this.itemStock.getActualAmount());
        itemStock.put("maxAmount",this.itemStock.getAmount());


        product.put("name", this.name);
        product.put("price", this.price);
        product.put("category", this.category.getTag() );
        product.put("itemcart", itemcart );
        product.put("itemstock", itemStock );

        return product;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }




    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return getName();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ItemStock getItemStock() {
        return itemStock;
    }

    public void setItemStock(ItemStock itemStock) {
        this.itemStock = itemStock;
    }
}


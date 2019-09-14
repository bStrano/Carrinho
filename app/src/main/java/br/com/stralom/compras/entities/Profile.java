package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

public class Profile implements Parcelable, Comparable {
    private ArrayList<Product> products;
    private ArrayList<Recipe> recipes;
    private String id;


    private boolean active;
    private boolean shared;
    private String name;

    public Profile(String id, String name, boolean active, boolean shared, ArrayList<Product> products, ArrayList<Recipe> recipes) {
        this.id = id;
        this.products = products;
        this.recipes = recipes;
        this.active = active;
        this.shared = shared;
        this.name = name;
    }

    public String getShareCode(){
        Log.d("AAAA", this.id);
        String sharecode = String.valueOf(this.id.charAt(0));
        sharecode = sharecode + this.id.substring(this.id.length() - 6).toUpperCase();
        return sharecode;
    }
    protected Profile(Parcel in) {
        products = in.createTypedArrayList(Product.CREATOR);
        recipes = in.createTypedArrayList(Recipe.CREATOR);
        id = in.readString();
        active = in.readByte() != 0;
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
        dest.writeTypedList(recipes);
        dest.writeString(id);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public int getProductsNumber(){
        return this.products.size();
    }

    public int getRecipesNumber(){
        return this.recipes.size();
    }

    public int getItemCartNumber(){
        int counter = 0;
        for(Product product : products){
            if(product.getItemCart().getAmount() > 0){
                counter++;
            }
        }
        return counter;
    }

    public int getItemStockNumber(){
        int counter = 0;
        for(Product product : products){
            if(product.getItemStock().getAmount() > 0){
                counter++;
            }
        }
        return counter;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Profile{" +
                ", name='" + name + '\'' +
                "products=" + products +
                ", recipes=" + recipes +
                ", id='" + id + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.getId().compareTo(((Profile) o).getId());
    }
}

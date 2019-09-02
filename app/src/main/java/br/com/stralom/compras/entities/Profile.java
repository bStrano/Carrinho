package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Profile implements Parcelable {
    private ArrayList<Product> products;
    private ArrayList<Recipe> recipes;

    public Profile(ArrayList<Product> products, ArrayList<Recipe> recipes) {
        this.products = products;
        this.recipes = recipes;
    }

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
    private Profile(Parcel in) {
        products = in.createTypedArrayList(Product.CREATOR);
        recipes = in.createTypedArrayList(Recipe.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(products);
        parcel.writeTypedList(recipes);
    }
}

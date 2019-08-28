package br.com.stralom.compras.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.stralom.compras.R;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Recipe implements Parcelable {
    private String id;
    private String name;
    private double total;
    private int ingredientCount;
    private List<ItemRecipe> ingredients;

    public int getCartAmount() {
        return cartAmount;
    }

    public void setCartAmount(int cartAmount) {
        this.cartAmount = cartAmount;
    }

    // private String imagePath;
    private int cartAmount;

    public Recipe(){
    }

    public Recipe(String id, String name, List<ItemRecipe> ingredients) {
        this.id = id;
        this.name = name;
        this.ingredientCount = ingredients.size();
        this.ingredients = ingredients;
        this.total =  calculateTotal();
        this.cartAmount = 0;
    }

    public Recipe(String name, List<ItemRecipe> ingredients) {
        this.name = name;
        this.ingredients = ingredients;
        this.total = calculateTotal();
        this.ingredientCount = ingredients.size();
        //this.imagePath = imagePath;
        this.cartAmount = 0;
    }
    protected Recipe(Parcel in) {
        id = in.readString();
        name = in.readString();
        total = in.readDouble();
        ingredientCount = in.readInt();
        ingredients = in.createTypedArrayList(ItemRecipe.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(total);
        dest.writeInt(ingredientCount);
        dest.writeTypedList(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Map<String,Object> toJson(String profileIdentifier, FirebaseFirestore dbFirebase){
        Map<String, Object> recipe = new HashMap<>();
        recipe.put("name",this.name);
        recipe.put("total",this.total);

        ArrayList<Object> ingredients = new ArrayList<>();
        for(ItemRecipe itemRecipe: this.ingredients){
            Map<String, Object> itemRecipeHash = new HashMap<>();
            itemRecipeHash.put("amount",itemRecipe.getAmount());
            itemRecipeHash.put("total",itemRecipe.getTotal());

            DocumentReference productRef= dbFirebase.collection("profiles").document(profileIdentifier).collection("products").document(itemRecipe.getProduct().getName());
            itemRecipeHash.put("productRef",productRef);
            itemRecipeHash.put("productName", itemRecipe.getProduct().getName());
            ingredients.add(itemRecipeHash);
        }
        recipe.put("ingredients", ingredients);


        return recipe;
    }


    private double calculateTotal(){
        double total = 0;
        for (ItemRecipe ingredient: ingredients) {
            total += ingredient.getTotal();
        }
        return total;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(int ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public List<ItemRecipe> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ItemRecipe> ingredients) {
        this.ingredients = ingredients;
    }


    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", total=" + total +
                ", ingredientCount=" + ingredientCount +
                ", ingredients=" + ingredients +
                '}';
    }

}

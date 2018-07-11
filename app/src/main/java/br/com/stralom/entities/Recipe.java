package br.com.stralom.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Recipe implements Parcelable {
    private Long id;
    private String name;
    private double total;
    private int igredientCount;
    private List<ItemRecipe> ingredients;
    private String imagePath;

    public Recipe(){
    }



    public Recipe(Long id, String name, List<ItemRecipe> ingredients) {
        this.id = id;
        this.name = name;
        this.total =  calculateTotal();
        this.igredientCount = ingredients.size();
        this.ingredients = ingredients;
    }

    public Recipe(String name, List<ItemRecipe> ingredients, String imagePath) {
        this.name = name;
        this.ingredients = ingredients;
        this.total = calculateTotal();
        this.igredientCount = ingredients.size();
        this.imagePath = imagePath;
    }

    protected Recipe(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        total = in.readDouble();
        igredientCount = in.readInt();
        imagePath = in.readString();
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

    private double calculateTotal(){
        double total = 0;
        for (ItemRecipe ingredient: ingredients) {
            total += ingredient.getTotal();
        }
        return total;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getIgredientCount() {
        return igredientCount;
    }

    public void setIgredientCount(int igredientCount) {
        this.igredientCount = igredientCount;
    }

    public List<ItemRecipe> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<ItemRecipe> ingredients) {
        this.ingredients = ingredients;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", total=" + total +
                ", igredientCount=" + igredientCount +
                ", ingredients=" + ingredients +
                ", imagePath='" + imagePath + '\'' +
                '}';
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
        parcel.writeString(name);
        parcel.writeDouble(total);
        parcel.writeInt(igredientCount);
        parcel.writeString(imagePath);
    }
}

package br.com.stralom.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Recipe implements Serializable {
    private Long id;
    private String name;
    private double total;
    private int igredientCount;
    private List<ItemRecipe> ingredients;
    private String imagePath;

    public Recipe(){
    }

    public Recipe(String name, double total, List<ItemRecipe> ingredients) {
        this.name = name;
        this.total = total;
        this.igredientCount = ingredients.size();
        this.ingredients = ingredients;
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

    public double calculateTotal(){
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
}

package br.com.stralom.entities;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class ItemRecipe extends Item {
    private Recipe recipe;

    public ItemRecipe(){}

    public ItemRecipe(int amount, Product product) {
        super(amount, product);
    }

    public ItemRecipe(int amount, Product product, Recipe recipe) {
        super(amount, product);
        this.recipe = recipe;
    }


    public Recipe getRecipe() {
        return recipe;
    }



    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return product.getName() + " (" + this.amount + ") - " + this.total + " R$";
    }

    public ItemCart convertToItemCart(Cart cart){
        return new ItemCart(product, amount, cart);
    }


}

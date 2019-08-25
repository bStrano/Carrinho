package br.com.stralom.compras.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class ItemRecipe extends Item implements Parcelable {
    private Recipe recipe;
    private Product product;


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemRecipe> CREATOR = new Creator<ItemRecipe>() {
        @Override
        public ItemRecipe createFromParcel(Parcel in) {
            return new ItemRecipe(in);
        }

        @Override
        public ItemRecipe[] newArray(int size) {
            return new ItemRecipe[size];
        }
    };

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ItemRecipe(){}

    public ItemRecipe(double amount, Product product) {
        super(amount, product.getPrice());
        this.product = product;
    }

    public ItemRecipe(double amount, Product product, Recipe recipe) {
        super(amount, product.getPrice());
        this.recipe = recipe;
        this.product = product;
    }


    protected ItemRecipe(Parcel in) {
        super(in);
        recipe = in.readParcelable(Recipe.class.getClassLoader());
        product = in.readParcelable(Product.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(recipe, flags);
        dest.writeParcelable(product, flags);
    }

    public Recipe getRecipe() {
        return recipe;
    }



    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        String productName;
        if(this.product == null){
            productName = "Null Produict";
        } else {
            productName = this.product.getName();
        }
        return productName + " (" + this.amount + ") - " + this.total + " R$";
    }

    public ItemCart convertToItemCart(Cart cart) throws Exception {
        throw new Exception("Em refatoração");
     //    return new ItemCart(product, amount, cart);
    }


}

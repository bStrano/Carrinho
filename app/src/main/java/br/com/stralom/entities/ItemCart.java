package br.com.stralom.entities;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCart extends Item {
    private Cart cart;


    public ItemCart(Product product, int amount, Cart cart) {
        super(amount,product);
        this.cart = cart;
    }



    public ItemCart() {
    }


    public String show() {
        return "ItemCart{" +
                "id=" + id +
                ", amount=" + amount +
                ", total=" + total +
                ", cart=" + cart +
                ", product=" + product +
                '}';
    }

    @Override
    public String toString(){
      return  product.getName() + " ( " + amount + " )" + " - "  + product.getPrice();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }




}

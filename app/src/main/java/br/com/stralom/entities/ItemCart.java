package br.com.stralom.entities;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCart extends Item {
    private Cart cart;
    private Long convertedId;

    public ItemCart(Product product, int amount, Cart cart) {
        super(amount,product);
        this.cart = cart;
    }

    public ItemCart(Long id, Product product,int amount, double total,  Cart cart) {
        super(id, amount, total, product);
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

    public Long getConvertedId() {
        return convertedId;
    }

    public void setConvertedId(Long convertedId) {
        this.convertedId = convertedId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }




}

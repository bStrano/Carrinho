package br.com.stralom.entities;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class ItemCart extends Item {
    private Cart cart;
    private Long convertedId;
    private boolean isRemoved;
    private boolean updateStock;

    public ItemCart(Product product, int amount, Cart cart) {
        super(amount,product);
        this.cart = cart;
        isRemoved = false;
        updateStock = false;
    }

    public ItemCart(Long id, Product product,int amount, double total,  Cart cart) {
        super(id, amount, total, product);
        this.cart = cart;
        isRemoved = false;
        updateStock = false;
    }

    public ItemCart() {
        isRemoved = false;
        updateStock = false;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public boolean isUpdateStock() {
        return updateStock;
    }

    public void setUpdateStock(int updateStock) {
        if(updateStock == 0) {
            this.updateStock = false;
        } else {
            this.updateStock = true;
        }
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

package br.com.stralom.entities;

/**
 * Created by Bruno on 05/02/2018.
 */

public class SimpleItem {
    private Long id;
    private String name;
    private int amount;
    private Cart cart;

    public SimpleItem(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public SimpleItem(String name, int amount, Cart cart) {
        this.name = name;
        this.amount = amount;
        this.cart = cart;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "SimpleItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productAmount=" + amount +
                '}';
    }


}

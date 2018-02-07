package br.com.stralom.entities;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Item implements Serializable {
    protected Long id;
    protected int amount;
    protected double total;
    protected Product product;

    public Item(int amount, Product product) {
        this.amount = amount;
        setTotal(amount,product.getPrice());
        this.product = product;
    }

    public Item(int amount, double total, Product product) {
        this.amount = amount;
        this.total = total;
        this.product = product;
    }

    public Item(Long id, int amount, double total, Product product) {
        this.id = id;
        this.amount = amount;
        this.total = total;
        this.product = product;
    }

    public Item() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotal(int amount, double productPrice){
        this.total =  amount * productPrice;
    }

}

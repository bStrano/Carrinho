package br.com.stralom.entities;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class Item implements Serializable {
    protected Long id;
    protected double amount;
    protected double total;
    protected Product product;

    Item(double amount, Product product) {
        this.amount = amount;
        setTotal(amount,product.getPrice());
        this.product = product;
    }

    Item(Long id, double amount, double total, Product product) {
        this.id = id;
        this.amount = amount;
        this.total = total;
        this.product = product;
    }

    Item() {
    }

    public String formatAmount(){
        if(total % 1 == 0){
            return String.valueOf((int) amount);
        } else {
            return String.format("#.2f",amount);
        }
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTotal(double amount, double productPrice){
        this.total =  amount * productPrice;
    }

}

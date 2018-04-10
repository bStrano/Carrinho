package br.com.stralom.entities;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class Product implements Serializable {
    private Long id;
    private String name;
    private double price;
    private Category category;



    public Product() {
    }



    public Product(Long id, String name, double price, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return (getId() + "   " + getName() + " - " + getPrice() + " R$");
    }
}


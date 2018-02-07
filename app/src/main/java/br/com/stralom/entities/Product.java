package br.com.stralom.entities;

import java.io.Serializable;

/**
 * Created by Bruno Strano on 30/12/2017.
 */

public class Product implements Serializable {
    private Long id;
    private String name;
    private double price;
    private String category;



    public Product() {
    }

    public Product(String name, double price) {
        this.price = price;
        this.name = name;
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String name, double price, String category) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return (getId() + "   " + getName() + " - " + getPrice() + " R$");
    }
}


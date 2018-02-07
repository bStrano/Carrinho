package br.com.stralom.entities;


import java.util.List;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class Stock {
    private Long id;
    private int productCount;
    private List<ItemStock> products;

    public Stock(){}



    public Stock(List<ItemStock> products) {
        this.products = products;
        productCount = products.size();
    }

    public Stock(Long id) {
        this.id = id;
    }

    public List<ItemStock> getProducts() {
        return products;
    }

    public void setProducts(List<ItemStock> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }
}

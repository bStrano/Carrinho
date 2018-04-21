package br.com.stralom.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class Cart implements Serializable {
    private Long id;
    private List<ItemCart> listItemCart;
    private double total =0;

    public Cart() {
        listItemCart = new ArrayList<>();
        total = 0;
    }

    public Cart(Long id) {
        this.id = id;
    }

    public Cart(Long id, List<ItemCart> listItemCart, double total) {
        this.id = id;
        this.listItemCart = listItemCart;
        this.total = total;
    }

    public Cart(List<ItemCart> listItemCart, double total) {
        this.listItemCart = listItemCart;
        this.total = total;
    }

    public List<ItemCart> getListItemCart() {
        return listItemCart;
    }

    public void setListItemCart(List<ItemCart> listItemCart) {
        this.listItemCart = listItemCart;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}

package br.com.stralom.compras.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Bruno Strano on 03/01/2018.
 */

public class Cart implements Serializable {
    private Long id;
    private List<Product> listItemCart;
    private double total =0;

    public Cart() {
        listItemCart = new ArrayList<>();
        total = 0;
    }

    public Cart(Long id) {
        this.id = id;
    }

    public Cart(Long id, List<Product> listItemCart, double total) {
        this.id = id;
        this.listItemCart = listItemCart;
        this.total = total;
    }

    public Cart(List<Product> listItemCart, double total) {
        this.listItemCart = listItemCart;
        this.total = total;
    }

    public List<Product> getListItemCart() {
        return listItemCart;
    }

    public void setListItemCart(List<Product> listItemCart) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cart)) return false;
        Cart cart = (Cart) o;
        return Double.compare(cart.total, total) == 0 &&
                Objects.equals(id, cart.id) &&
                Objects.equals(listItemCart, cart.listItemCart);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, listItemCart, total);
    }
}

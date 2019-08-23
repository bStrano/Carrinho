package br.com.stralom.compras.interfaces;


import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;

public interface StockUpdateCallback{
    void edit(Product itemStock);
    boolean isEditModeOn();
}



package br.com.stralom.compras.interfaces;


import br.com.stralom.compras.entities.ItemStock;

public interface StockUpdateCallback{
    void edit(ItemStock itemStock);
    boolean isEditModeOn();
}



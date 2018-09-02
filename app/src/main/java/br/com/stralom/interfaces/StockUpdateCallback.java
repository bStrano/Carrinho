package br.com.stralom.interfaces;


import br.com.stralom.entities.ItemStock;

public interface StockUpdateCallback{
    void edit(ItemStock itemStock);
    boolean isEditModeOn();
}



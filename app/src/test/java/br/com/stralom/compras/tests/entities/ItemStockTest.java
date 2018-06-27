package br.com.stralom.compras.tests.entities;

import org.junit.Test;

import br.com.stralom.entities.ItemStock;

import static org.junit.Assert.*;

/**
 * Example local unit empty_list, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ItemStockTest {
    @Test
    public void aValidSetStock() {

        ItemStock itemStock = new ItemStock();

        itemStock.setStockPercentage(50);
        itemStock.setStatus();
        assertTrue(itemStock.getStatus() == ItemStock.Status.NEUTRAL);
    }
}
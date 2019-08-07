package br.com.stralom.compras.tests.entities;

import org.junit.Before;
import org.junit.Test;

import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.entities.SimpleItem;
import static org.junit.Assert.*;

public class ItemCartTest {
    private static final int defaultAmount = 10;
    private Cart cart;
    private ItemCart itemCart;

    @Before
    public void setUp(){
        cart = new Cart();
        cart.setId((long) 1);

        Product product = new Product();

        ItemCart itemCart = new ItemCart((long) 1,product,defaultAmount,100,cart);

    }

    @Test
    public void aValidConvertToItemCart() {
        Cart cart = new Cart();
        cart.setId((long) 1);
        SimpleItem simpleItem = new SimpleItem("SimpleItem 1",defaultAmount,cart);
        simpleItem.setId((long) 1);
        ItemCart itemCartExpected = new ItemCart();
        itemCartExpected.setConvertedId(simpleItem.getId());
        

        ItemCart itemCartResult = (ItemCart) ItemCart.convertToItemCart(simpleItem);

        assertTrue(itemCartResult.getConvertedId() == simpleItem.getCart().getId());

    }
}

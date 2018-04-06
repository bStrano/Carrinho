package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 06/01/2018.
 */

public class ItemCartForm {
    private Spinner product;
    private EditText amout;
    private Cart cart;
    private static final String TAG = "[HELPER] ItemCartForm";

    public ItemCartForm(View view, Cart cart) {
        product = view.findViewById(R.id.form_itemCart_product);
        amout = view.findViewById(R.id.form_itemCart_amount);
        this.cart = cart;
    }

     public ItemCart getItemCart(){
        Product product = (Product) this.product.getSelectedItem();
        int amount = Integer.parseInt(amout.getText().toString());

        return new ItemCart(product,amount,cart);


     }
}

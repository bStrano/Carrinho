package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.SimpleItem;

/**
 * Created by Bruno on 05/02/2018.
 */

public class SimpleItemForm {
    private EditText name;
    private EditText amount;
    private Cart cart;

    public SimpleItemForm(View view, Cart cart) {
        this.name = view.findViewById(R.id.form_simpleProduct_name);
        this.amount = view.findViewById(R.id.form_simpleProduct_amount);
        this.cart = cart;
    }

    public SimpleItem getSimpleItem(){
        String name = this.name.getText().toString();
        int amount;
        if(this.amount.getText().toString().matches("") || this.amount.getText().toString().matches("0") ) {
            amount = 0;
        } else {
            amount = Integer.valueOf(this.amount.getText().toString());
        }

        Long cartId = cart.getId();
        return new SimpleItem(name,amount,cart);
    }
}

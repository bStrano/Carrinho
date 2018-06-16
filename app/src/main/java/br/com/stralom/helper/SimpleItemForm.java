package br.com.stralom.helper;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.SimpleItem;

/**
 * Created by Bruno on 05/02/2018.
 */

public class SimpleItemForm extends BasicFormValidation {
    @NotEmpty(messageResId = R.string.validation_obrigatoryField)
    private final EditText name;
    private final EditText amount;
    private final Cart cart;
    private SimpleItem simpleItem;

    public SimpleItemForm(Activity activity,View view, Cart cart) {
        super(activity);

        this.name = view.findViewById(R.id.itemCart_form_simpleProduct_name);
        this.amount = view.findViewById(R.id.itemCart_form_simpleProduct_amount);
        this.cart = cart;
    }

    public SimpleItem getSimpleItem(){
        return simpleItem;
    }

    @Override
    public void onValidationSucceeded() {
        String name = this.name.getText().toString();
        int amount;
        if(this.amount.getText().toString().matches("") || this.amount.getText().toString().matches("0") ) {
            amount = 0;
        } else {
            amount = Integer.valueOf(this.amount.getText().toString());
        }

        simpleItem = new SimpleItem(name,amount,cart);
        validationSuccessful = true;
    }
}

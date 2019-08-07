package br.com.stralom.compras.helper;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.ItemCart;
import br.com.stralom.compras.entities.Product;

/**
 * Created by Bruno Strano on 06/01/2018.
 */

public class ItemCartForm extends BasicFormValidation {
    private final Spinner product;
    @NotEmpty(messageResId = R.string.itemCart_validation_amount)
    private final EditText amout;
    private final Cart cart;
    private final CheckBox updateStock;
    private ItemCart itemCart;


    public ItemCartForm(Activity activity, View view, Cart cart) {
        super(activity);

        product = view.findViewById(R.id.itemcart_form_product);
        amout = view.findViewById(R.id.itemCart_form_productAmount);
        this.cart = cart;
        updateStock = view.findViewById(R.id.itemCart_form_productUpdateStock);
    }

     public ItemCart getItemCart(){
        return itemCart;


     }

    @Override
    public void onValidationSucceeded() {
        Product product = (Product) this.product.getSelectedItem();
        int amount = Integer.parseInt(amout.getText().toString());
        updateStock.isChecked();

        itemCart = new ItemCart(product,amount,cart);
        itemCart.setUpdateStock(updateStock.isChecked());
        validationSuccessful = true;
    }

}

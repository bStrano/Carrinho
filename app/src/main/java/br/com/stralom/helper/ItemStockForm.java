package br.com.stralom.helper;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class ItemStockForm extends BasicFormValidation{
    private final Spinner products;
    @NotEmpty(messageResId = R.string.validation_obrigatoryField)
    private final EditText actualAmount;
    @NotEmpty(messageResId = R.string.validation_obrigatoryField)
    private final EditText maxAmount;
    private ItemStock itemStock;

    public ItemStockForm(Activity activity,View view) {
        super(activity);
        this.products = view.findViewById(R.id.form_itemStock_products);
        this.actualAmount = view.findViewById(R.id.form_itemStock_actualAmount);
        this.maxAmount = view.findViewById(R.id.form_itemStock_maxAmount);
        itemStock = null;
    }

    public ItemStock getItemStock(){
        return itemStock;
    }

    @Override
    public void onValidationSucceeded() {
        Product product = (Product) products.getSelectedItem();
        int maxAmount = Integer.valueOf(this.maxAmount.getText().toString());
        int actualAmount = Integer.valueOf(this.actualAmount.getText().toString());

        itemStock = new ItemStock(maxAmount,product,actualAmount);
        validationSuccessful = true;
    }
}

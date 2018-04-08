package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class ItemRecipeForm {
    private final Spinner products;
    private final EditText amount;


    public ItemRecipeForm(View view) {
        this.products = view.findViewById(R.id.form_itemRecipe_products);
        this.amount = view.findViewById(R.id.form_itemRecipe_amount);
    }

    public ItemRecipe getItemRecipe(){
        Product product = (Product) products.getSelectedItem();
        int amount = Integer.parseInt(this.amount.getText().toString());
        return new ItemRecipe(amount,product);
    }
}

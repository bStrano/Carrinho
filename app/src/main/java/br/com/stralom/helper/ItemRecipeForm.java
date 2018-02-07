package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Item;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 17/01/2018.
 */

public class ItemRecipeForm {
    Spinner products;
    EditText amount;
    Long recipeId;

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

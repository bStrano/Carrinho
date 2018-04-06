package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class ItemStockForm {
    private Spinner products;
    private EditText actualAmount;
    private EditText maxAmount;

    public ItemStockForm(View view) {
        this.products = view.findViewById(R.id.list_productsStock);
        this.actualAmount = view.findViewById(R.id.form_itemStock_actualAmount);
        this.maxAmount = view.findViewById(R.id.form_itemStock_maxAmount);

    }

    public ItemStock getItemStock(){
        Product product = (Product) products.getSelectedItem();
        int maxAmount = Integer.valueOf(this.maxAmount.getText().toString());
        int actualAmount = Integer.valueOf(this.actualAmount.getText().toString());

        return new ItemStock(maxAmount,product,actualAmount);


    }
}

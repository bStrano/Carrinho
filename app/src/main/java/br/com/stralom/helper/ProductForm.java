package br.com.stralom.helper;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 02/01/2018.
 */

public class ProductForm {
    private final EditText name;
    private final EditText price;
    private final Spinner categorySpinner;


    public ProductForm(View view){
        name = view.findViewById(R.id.form_productName);
        price = view.findViewById(R.id.form_productPrice);
        categorySpinner = view.findViewById(R.id.form_productCategory);
    }

    public Product getProduct(){
        String name = this.name.getText().toString();
        double price = Double.parseDouble(this.price.getText().toString());
        Category category = (Category) categorySpinner.getSelectedItem();


        return new Product(name,price,category);
    }
}

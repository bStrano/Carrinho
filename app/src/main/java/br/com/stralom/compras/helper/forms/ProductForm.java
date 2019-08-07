package br.com.stralom.compras.helper.forms;

import android.app.Activity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import android.util.Log;
import android.util.Pair;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.Product;

/**
 * Created by Bruno Strano on 17/07/2018.
 */
public class ProductForm {
    //************************************* Temporario *************************************
    private Cart cart ;

    private boolean isValid = false;
    private Validation validation;

    private TextInputLayout cartAmountLayout;
    private TextInputLayout stockActualAmountLayout;
    private TextInputLayout stockMaxAmountLayout;
    private TextInputLayout nameLayout;
    private TextInputLayout priceLayout;


    private TextInputEditText cartAmountView;
    private TextInputEditText stockActualAmountView;
    private TextInputEditText stockMaxAmountView;
    @NotEmpty
    private TextInputEditText nameView;
    @NotEmpty
    private TextInputEditText priceView;
    private Spinner categoriesView;

    public ProductForm(Activity activity) {
        validation = new Validation(activity);
        // ************************************* Temporario *************************************
        cart = new Cart();
        cart.setId((long) 1);

        this.cartAmountView = activity.findViewById(R.id.registration_product_addCartAmount);
        this.stockActualAmountView = activity.findViewById(R.id.registration_product_addStockActualAmount);
        this.stockMaxAmountView = activity.findViewById(R.id.registration_product_addStockMaxAmount);
        this.nameView = activity.findViewById(R.id.registration_product_name);
        this.priceView = activity.findViewById(R.id.registration_product_price);
        this.categoriesView = activity.findViewById(R.id.registration_product_categories);

        this.cartAmountLayout = activity.findViewById(R.id.registration_product_addCartAmountLayout);
        this.stockActualAmountLayout = activity.findViewById(R.id.registration_product_addStockActualAmountLayout);
        this.stockMaxAmountLayout = activity.findViewById(R.id.registration_product_addStockMaxAmountLayout);
        this.nameLayout = activity.findViewById(R.id.registration_product_nameLayout);
        this.priceLayout = activity.findViewById(R.id.registration_product_priceLayout);
    }

    public Product getValidProduct(){
        if(validation.validateEmpty(Pair.create(nameLayout,nameView),Pair.create(priceLayout,priceView))){
            String name = nameView.getText().toString();
            double price = Double.parseDouble(priceView.getText().toString());
            Category category = (Category) categoriesView.getSelectedItem();
            Log.d("TESTE",category.toString());
            return new Product(name,price,category);
        } else {
            return null;
        }
    }

    public double getValidItemCartAmount(){
        if (validation.validateEmpty(Pair.create(cartAmountLayout,cartAmountView))){
            return Double.parseDouble(cartAmountView.getText().toString());
        } else {
            return -1;
        }
    }

    public Pair<Double,Double> getValidStockAmounts(){
        if(validation.validateEmpty(Pair.create(stockActualAmountLayout,stockActualAmountView), Pair.create(stockMaxAmountLayout,stockMaxAmountView))){
            return Pair.create(Double.valueOf(stockActualAmountView.getText().toString()),Double.valueOf(stockMaxAmountView.getText().toString()));
        } else {
            return null;
        }
    }

}

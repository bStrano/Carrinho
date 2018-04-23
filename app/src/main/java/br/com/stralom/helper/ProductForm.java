package br.com.stralom.helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 02/01/2018.
 */

public class ProductForm implements Validator.ValidationListener {
    private Validator validator;
    private Activity activity;
    private Product product;
    private boolean validationSuccessful;

    @NotEmpty(messageResId = R.string.product_validation_name)
    @Length(max = 25, messageResId = R.string.validation_maxLenght)
    private final EditText name;
    @NotEmpty(messageResId = R.string.product_validation_price)
    private final EditText price;
    private final Spinner categorySpinner;


    public ProductForm(Activity activity, View view) {
        validator = new Validator(this);
        validator.setValidationListener(this);
        name = view.findViewById(R.id.form_productName);
        price = view.findViewById(R.id.form_productPrice);
        categorySpinner = view.findViewById(R.id.form_productCategory);
        this.activity = activity;

    }

    public Product getProduct() {
        return product;
    }

    @Override
    public void onValidationSucceeded() {
        validationSuccessful = true;
        Log.e("Teste","OnValidationSucceded");
        String name = this.name.getText().toString();
        double price = Double.parseDouble(this.price.getText().toString());
        Category category = (Category) categorySpinner.getSelectedItem();

        product = new Product(name, price, category);
        Toast.makeText(activity, "Yay! we got it right!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        validationSuccessful = false;
        Log.e("Teste","OnValidationFailed");
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(activity);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    public Validator getValidator() {
        return validator;
    }

    public boolean isValidationSuccessful() {
        return validationSuccessful;
    }
}

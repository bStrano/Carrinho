package br.com.stralom.helper.forms;

import android.app.Activity;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;
import br.com.stralom.helper.BasicFormValidation;
import br.com.stralom.helper.exceptions.InvalidElementForm;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class ItemStockForm  {
    private Validation validation;

    private ItemStock itemStock;
    private TextInputLayout actualAmountLayout;
    private TextInputEditText actualAmount;
    private TextInputLayout maxAmountLayout;
    private TextInputEditText maxAmount;


    public ItemStockForm(Activity activity) {
        validation = new Validation(activity);

        this.actualAmountLayout = activity.findViewById(R.id.stock_registration_atualAmountLayout);
        this.actualAmount = activity.findViewById(R.id.stock_registration_atualAmount);
        this.maxAmountLayout = activity.findViewById(R.id.stock_registration_maxAmountLayout);
        this.maxAmount = activity.findViewById(R.id.stock_registration_maxAmount);
        this.itemStock = null;
    }

    public ItemStock getValidItemStock(Product selectedProduct) throws InvalidElementForm {


        Pair<Double,Double> amounts = checkAmounts();
        if(selectedProduct == null){
            throw new InvalidElementForm("Product not selected");
        } else if(amounts == null){
            throw new InvalidElementForm("Empty amounts field");
        }

        itemStock = new ItemStock();
        itemStock.setProduct(selectedProduct);
        itemStock.setAmounts(amounts.first,amounts.second);

        return itemStock;
    }

    private Pair<Double,Double> checkAmounts(){
        if(validation.validateEmpty(Pair.create(actualAmountLayout,actualAmount), Pair.create(maxAmountLayout,maxAmount))){
            return Pair.create(Double.valueOf(actualAmount.getText().toString()),Double.valueOf(maxAmount.getText().toString()));
        } else {
            return null;
        }
    }
}

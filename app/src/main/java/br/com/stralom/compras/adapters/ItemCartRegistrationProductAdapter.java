package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.DBHelper;
import br.com.stralom.compras.entities.Cart;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.Product;

/**
 * Created by Bruno Strano on 12/07/2018.
 */
public class ItemCartRegistrationProductAdapter extends ItemCartRegistrationAdapter<Product>  {
    private static final String TAG = "RegistProductAdapter" ;
    private Product temporaryProduct = null;



    public ItemCartRegistrationProductAdapter(ArrayList<Product> products, Activity activity) {
        super(products,activity);
    }


    @Override
    protected void setUpViewHolderLayout(@NonNull ViewHolder holder, Product product) {
        super.setUpViewHolderLayout(holder, product);
        if(isTemporaryProduct(product)){
            int colorId = ContextCompat.getColor(activity,R.color.bg_temproaryProduct);
            holder.background.setBackgroundColor(colorId);
        } else {
            holder.background.setBackgroundColor(Color.WHITE);
        }
    }

    private boolean isTemporaryProduct(Product product) {
        Log.d(TAG, product.toString());
        Log.d(TAG, product.getCategory().toString());
        Log.d(TAG, product.getCategory().getName());
        return product.getCategory().getName().equals(DBHelper.CATEGORY_TEMPORARY_PRODUCT) ;
    }

    @Override
    protected void removeAmount(Product product){
        if(isTemporaryProduct(product)){
            if(selectedPositions.get(product) == 1){
                selectedPositions.remove(product);
                listClone.remove(product);
                if(!filtering){
                    list.remove(product);
                }


            }  else {
                super.removeAmount(product);
            }

        } else {
            super.removeAmount(product);
        }
    }

    @Override
    protected void addAmount(Product product) {
        if(isTemporaryProduct(product)) {
            if (product == temporaryProduct) {
                Product productClone = temporaryProduct.getClone();
                listClone.remove(temporaryProduct);
                list.remove(temporaryProduct);
                list.add(productClone);
                listClone.add(productClone);
                temporaryProduct = null;
                selectedPositions.put(productClone, 1.0);

            } else {
                super.addAmount(product);
            }
        } else {
            super.addAmount(product);
        }
    }

    public void ifNecessaryCreateOrUpdateSimpleProduct(String filterInput,Cart cart) {

        if(filterInput == null || filterInput.equals("")){

            listClone.remove(temporaryProduct);
            temporaryProduct = null;
            return;
        }

        if(temporaryProduct == null){
            temporaryProduct = new Product();
            temporaryProduct.setCategory(new Category(DBHelper.CATEGORY_TEMPORARY_PRODUCT));
            temporaryProduct.setName(filterInput);
            listClone.add(0,temporaryProduct);

        } else {


            for (Product product:listClone) {
                if(product.getName().equals(filterInput)){
                    listClone.remove(temporaryProduct);
                    temporaryProduct = null;
                    return;
                }
            }
            temporaryProduct.setName(filterInput);

        }





    }
    @Override
    public String getName(Product product) {
        return product.getName();
    }

    @Override
    public double getAmount(Product product) {
        return product.getItemCart().getAmount();
    }

    @Override
    public void setAmount(Product object, double amount) {
        object.getItemCart().setAmount(amount);
    }


}

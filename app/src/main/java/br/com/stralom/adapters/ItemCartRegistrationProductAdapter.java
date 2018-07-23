package br.com.stralom.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Cart;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.SimpleItem;

/**
 * Created by Bruno Strano on 12/07/2018.
 */
public class ItemCartRegistrationProductAdapter extends ItemCartRegistrationAdapter<Product>  {
    private Product temporaryProduct = null;



    public ItemCartRegistrationProductAdapter(ArrayList<Product> products, Activity activity) {
        super(products,activity);
    }


    @Override
    protected void setUpViewHolderLayout(@NonNull ViewHolder holder, Product product) {
        super.setUpViewHolderLayout(holder, product);
        if(product.getCategory() == null){
            holder.background.setBackgroundColor(Color.parseColor("#FFFFE0"));
        } else {
            holder.background.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected void removeAmount(Product product){
        if(product.getCategory() == null){
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
        if(product.getCategory() == null) {
            if (product == temporaryProduct) {
                Product productClone = temporaryProduct.getClone();
                listClone.remove(temporaryProduct);
                list.remove(temporaryProduct);
                list.add(productClone);
                listClone.add(productClone);
                temporaryProduct = null;
                selectedPositions.put(productClone, 1);

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
}

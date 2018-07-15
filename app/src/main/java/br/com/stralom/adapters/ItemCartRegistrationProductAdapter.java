package br.com.stralom.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 12/07/2018.
 */
public class ItemCartRegistrationProductAdapter extends ItemCartRegistrationAdapter<Product>  {

    public ItemCartRegistrationProductAdapter(ArrayList<Product> products, Activity activity) {
        super(products,activity);
    }



    @Override
    public String getName(Product product) {
        return product.getName();
    }


}

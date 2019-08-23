package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Product;

public class StockSpinnerAdapter extends BaseAdapter {
    private ArrayList<Product> products;
    private Activity activity;


    public StockSpinnerAdapter(ArrayList<Product> products, Activity activity) {
        this.products = products;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int i) {
        return products.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(R.layout.spinner_item_itemstock,viewGroup,false);
        TextView productName = view.findViewById(R.id.spinner_itemStock_name);
        ImageView imageView = view.findViewById(R.id.spinner_itemStock_icon);

        Product product = products.get(i);
        if(product.getCategory() != null){
            imageView.setImageResource(product.getCategory().getIconFlag());
        }

        productName.setText(product.getName());


        return view;
    }
}

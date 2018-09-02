package br.com.stralom.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Recipe;

public class StockSpinnerAdapter extends BaseAdapter {
    private ArrayList<ItemStock> stocks;
    private Activity activity;


    public StockSpinnerAdapter(ArrayList<ItemStock> stocks, Activity activity) {
        this.stocks = stocks;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int i) {
        return stocks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return stocks.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(R.layout.spinner_item_itemstock,viewGroup,false);
        TextView productName = view.findViewById(R.id.spinner_itemStock_name);
        ImageView imageView = view.findViewById(R.id.spinner_itemStock_icon);

        ItemStock itemStock = stocks.get(i);
        if(itemStock.getProduct().getCategory() != null){
            imageView.setImageResource(itemStock.getProduct().getCategory().getIconFlag());
        }

        productName.setText(itemStock.getProduct().getName());


        return view;
    }
}

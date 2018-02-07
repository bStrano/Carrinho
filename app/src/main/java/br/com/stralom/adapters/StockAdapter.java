package br.com.stralom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockAdapter extends BaseAdapter {
    private List<ItemStock> itemStocks ;
    private List<Product> products;
    private Context context;

    public StockAdapter(List<ItemStock> itemStocks, Context context) {
        this.itemStocks = itemStocks;
        this.context = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return itemStocks.size();
    }

    @Override
    public Object getItem(int position) {
        return itemStocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemStocks.get(position).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemStock itemStock = itemStocks.get(position);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.list_item_stock, parent , false);

        // Pegarr a lista de produtos pelo construtor
        TextView name = view.findViewById(R.id.stock_name);
        name.setText(itemStock.getProduct().getName());

        TextView maxAmount = view.findViewById(R.id.stock_maxAmount);
        maxAmount.setText(String.valueOf(itemStock.getAmount()));

        TextView actualAmount = view.findViewById(R.id.stock_actualAmount);
        actualAmount.setText(String.valueOf(itemStock.getActualAmount()));

        return view;
    }
}

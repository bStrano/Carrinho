package br.com.stralom.compras.adapters;

import android.app.Activity;
import androidx.databinding.ObservableArrayList;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.ItemStock;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.interfaces.StockUpdateCallback;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockAdapter extends BaseAdapter<StockAdapter.StockViewHolder,Product>  {
    private final ProductDAO productDAO;
    private ArrayList<StockViewHolder> holders;
    private StockUpdateCallback stockUpdateCallback;

    public StockAdapter(StockUpdateCallback stockUpdateCallback, ObservableArrayList<Product> itemStocks, Activity activity) {
        super(itemStocks,activity);
        this.stockUpdateCallback = stockUpdateCallback;
        productDAO = new ProductDAO(activity);
        holders = new ArrayList<>();
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(activity).inflate(R.layout.list_item_stock, parent , false);
        StockViewHolder stockViewHolder = new StockViewHolder(v);
        holders.add(stockViewHolder);
        return stockViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Product product = list.get(position);
        holder.name.setText(product.getName());
        holder.maxAmount.setText(product.getItemStock().getFormattedAmount());
        holder.actualAmount.setText(product.getItemStock().getFormattedActualAmount());
        holder.viewForeground.setBackgroundColor(Color.parseColor("#FAFAFA"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void removePermanently(Product item) {
        productDAO.remove( item.getId());
    }



    @Override
    public void edit() {

    }


    public class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        final TextView maxAmount;
        final TextView actualAmount;
        final View viewForeground ;

        StockViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.stock_name);
            maxAmount = view.findViewById(R.id.stock_maxAmount);
            actualAmount = view.findViewById(R.id.stock_actualAmount);
            viewForeground = view.findViewById(R.id.stock_itemList_foregroundView);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            Product product = list.get(getAdapterPosition());
            //ItemStock itemStock = getItemStockInHolderList(name.getText().toString());
            if((product != null ) && (!stockUpdateCallback.isEditModeOn()) ){
                stockUpdateCallback.edit(product);
            }
        }

    }

    private Product getItemStockInHolderList(String productName){
        for (int i = 0 ; i < holders.size() ; i++){
            StockViewHolder holder = holders.get(i);
            if(holder.name.getText().toString().equals(productName)){
                return list.get(i);
            }
        }
        return null;
    }
    }



package br.com.stralom.adapters;

import android.app.Activity;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemStock;
import br.com.stralom.interfaces.StockUpdateCallback;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockAdapter extends BaseAdapter<StockAdapter.StockViewHolder,ItemStock>  {
    private final ItemStockDAO itemStockDAO;
    private ArrayList<StockViewHolder> holders;
    private StockUpdateCallback stockUpdateCallback;

    public StockAdapter(StockUpdateCallback stockUpdateCallback, ObservableArrayList<ItemStock> itemStocks, Activity activity) {
        super(itemStocks,activity);
        this.stockUpdateCallback = stockUpdateCallback;
        itemStockDAO = new ItemStockDAO(activity);
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
        ItemStock itemStock = list.get(position);
        holder.name.setText(itemStock.getProduct().getName());
        holder.maxAmount.setText(itemStock.getFormattedAmount());
        holder.actualAmount.setText(itemStock.getFormattedActualAmount());
        holder.viewForeground.setBackgroundColor(Color.parseColor("#FAFAFA"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void removePermanently(ItemStock item) {
        itemStockDAO.remove( item.getId());
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

            ItemStock itemStock = list.get(getAdapterPosition());
            //ItemStock itemStock = getItemStockInHolderList(name.getText().toString());
            if((itemStock != null ) && (!stockUpdateCallback.isEditModeOn()) ){
                stockUpdateCallback.edit(itemStock);
            }
        }

    }

    private ItemStock getItemStockInHolderList(String productName){
        for (int i = 0 ; i < holders.size() ; i++){
            StockViewHolder holder = holders.get(i);
            if(holder.name.getText().toString().equals(productName)){
                return list.get(i);
            }
        }
        return null;
    }
    }



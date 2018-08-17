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

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemStock;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockAdapter extends BaseAdapter<StockAdapter.StockViewHolder,ItemStock>  {
    private final ItemStockDAO itemStockDAO;

    public StockAdapter(ObservableArrayList<ItemStock> itemStocks, Activity activity) {
        super(itemStocks,activity);
        itemStockDAO = new ItemStockDAO(activity);
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(activity).inflate(R.layout.list_item_stock, parent , false);
        return new StockViewHolder(v);
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


    public class StockViewHolder extends RecyclerView.ViewHolder  {
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
        }



        }
    }



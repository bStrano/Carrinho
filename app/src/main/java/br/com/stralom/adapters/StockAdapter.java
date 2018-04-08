package br.com.stralom.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.entities.ItemStock;

/**
 * Created by Bruno Strano on 24/01/2018.
 */

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> implements ItemTouchHelperAdapter {
    private final List<ItemStock> itemStocks ;
    private final Activity activity;
    private ItemClickListener clickListener;
    private boolean undoSwipe;
    private final ItemStockDAO itemStockDAO;

    public StockAdapter(List<ItemStock> itemStocks, Activity activity) {
        this.activity = activity;
        this.itemStocks = itemStocks;
        undoSwipe = false;
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
        ItemStock itemStock = itemStocks.get(position);
        holder.name.setText(itemStock.getProduct().getName());
        holder.maxAmount.setText(String.valueOf(itemStock.getAmount()));
        holder.actualAmount.setText(String.valueOf(itemStock.getActualAmount()));
    }

    @Override
    public int getItemCount() {
        return itemStocks.size();
    }


   // ItemTouchHelper Implementation {
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
    }

    @Override
    public void onItemDismiss(int position) {

        final ItemStock itemStock = itemStocks.get(position);
        final int deletedIndex = position;

        String name = itemStock.getProduct().getName();

        // Remover Temporiariamente
        itemStocks.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.stock_view_main), name + " removido do estoque!", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);

        // Desfazer remoção
        snackbar.setAction("DESFAZER", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemStocks.add(deletedIndex,itemStock);
                notifyItemInserted(deletedIndex);
                undoSwipe = true;
            }
        });

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(!undoSwipe) {
                        itemStockDAO.remove( itemStock.getId());
                }

                undoSwipe = false;

            }
        });

        snackbar.show();
        //

    }

    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return ((StockAdapter.StockViewHolder) viewHolder).viewForeground;
    }
    // }

    public class StockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        final TextView maxAmount;
        final TextView actualAmount;
        final View viewForeground ;
        final View viewBackground;

        StockViewHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            name = view.findViewById(R.id.stock_name);
            maxAmount = view.findViewById(R.id.stock_maxAmount);
            actualAmount = view.findViewById(R.id.stock_actualAmount);
            viewForeground = view.findViewById(R.id.stock_itemList_foregroundView);
            viewBackground = view.findViewById(R.id.stock_itemList_backgroundView);
        }


        @Override
        public void onClick(View view) {
            if(clickListener != null){
                clickListener.onClick(view,getLayoutPosition());
            }

        }
    }

}

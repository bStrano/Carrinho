package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemStock;

import static android.content.ContentValues.TAG;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.cartViewHolder> implements ItemTouchHelperAdapter {
    private final ArrayList<ItemCart> products;
    private final Activity activity;
    private final ItemCartDAO itemCartDAO;
    private final SimpleItemDAO simpleItemDAO;
    private final ItemStockDAO itemStockDAO;
    private boolean undoSwipe = false;
    private Resources res;
    private Snackbar snackbar;


    public CartAdapter(ArrayList<ItemCart> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        res = activity.getResources();
        itemCartDAO = new ItemCartDAO(activity);
        itemStockDAO = new ItemStockDAO(activity);
        simpleItemDAO = new SimpleItemDAO(activity);

    }




    public class cartViewHolder extends  RecyclerView.ViewHolder{
        final TextView productName;
        final TextView productAmount;
        final ImageView categoryIcon;
        final View viewForeground;
        final View viewBackground;

        cartViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.itemCart_itemList_name);
            productAmount = itemView.findViewById(R.id.itemCart_itemList_amount);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.cart_view_foreground);
            categoryIcon = itemView.findViewById(R.id.itemCart_itemList_categoryIcon);

        }
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_item_cart, parent, false);
        return new cartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cartViewHolder holder, int position) {
        ItemCart itemCart = products.get(position);
        //holder.productName.setText(res.getString(R.string.itemcart_itemList_nameAmount,itemCart.getAmount(),itemCart.getProduct().getName()));
        holder.productName.setText(itemCart.getProduct().getName());
        holder.productAmount.setText(res.getString(R.string.itemcart_itemList_amount, itemCart.getAmount()));
        try{
            holder.categoryIcon.setImageResource(itemCart.getProduct().getCategory().getIconFlag());
        } catch (NullPointerException e){
            holder.categoryIcon.setImageResource(R.drawable.ic_add);
        }


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if(fromPosition < toPosition){
            for (int i = fromPosition ; i < toPosition ; i++){
                Collections.swap(products,i,i+1);
            }
        } else {
            for (int i = fromPosition ; i > toPosition ; i--){
                Collections.swap(products,i,i-1);
            }
        }
        notifyItemMoved(fromPosition,toPosition);

    }



    @Override
    public void onItemDismiss(int position) {
        final ItemCart itemCart = products.get(position);
        String name = itemCart.getProduct().getName();
        Snackbar snackbar = removeTemporarily(position,itemCart,  name + " removido do carrinho.",null);
        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(itemCart.isRemoved()) {
                    if(itemCart.getId() != null) {
                        itemCartDAO.remove( itemCart.getId());
                    } else if(itemCart.getConvertedId() != null){
                        simpleItemDAO.remove( itemCart.getConvertedId());

                    }
                }


            }
        });


        snackbar.show();
        //

    }

    public void completeItem(int position, TextView textView) {
        final ItemCart itemCart = products.get(position);
        snackbar = removeTemporarily(position, itemCart, "Concluido",textView);
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event){
                if(itemCart.isRemoved()){
                    if(itemCart.getId() != null){
                        itemCartDAO.remove( itemCart.getId() );
                        if(itemCart.isUpdateStock()) {
                            ItemStock itemStock = itemStockDAO.findByProductId(itemCart.getProduct().getId());
                            if(itemStock != null){
                                itemStock.setActualAmount(itemCart.getAmount() + itemStock.getActualAmount());
                                itemStock.setTotal(itemStock.getActualAmount(),itemStock.getAmount());
                                itemStockDAO.update(itemStock);
                            }
                        }
                    } else if(itemCart.getConvertedId() != null) {
                        simpleItemDAO.remove( itemCart.getConvertedId());
                    }
                }
                super.onDismissed(transientBottomBar,event);
            }
        });
        snackbar.show();
    }
    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return  ((cartViewHolder) viewHolder).viewForeground;
    }



    @NonNull
    private Snackbar removeTemporarily(final int position, final ItemCart itemCart, String message, final TextView itemCartNameAmount) {
        // Remover Temporiariamente
        products.remove(position);
        notifyItemRemoved(position);
        snackbar = Snackbar.make(activity.findViewById(R.id.cart_view_main), message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);
        itemCart.setRemoved(true);
        // Desfazer remoção
        snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.add(position,itemCart);
                notifyItemInserted(position);
                itemCart.setRemoved(false);
                if( itemCartNameAmount != null) {
                    itemCartNameAmount.setPaintFlags( (itemCartNameAmount.getPaintFlags()) & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
            }
        });
        return snackbar;
    }

    public void dismissSnackbar(){
        if(snackbar != null){
            snackbar.dismiss();
        }

    }



}

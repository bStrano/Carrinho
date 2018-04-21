package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.CartDAO;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Item;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.ItemStock;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.mViewHolder> implements ItemTouchHelperAdapter {
    private final List<ItemCart> products;
    private final Activity activity;
    private final ItemCartDAO itemCartDAO;
    private final SimpleItemDAO simpleItemDAO;
    private final ItemStockDAO itemStockDAO;
    private boolean undoSwipe = false;
    private Resources res;

    public CartAdapter(List<ItemCart> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        res = activity.getResources();
        itemCartDAO = new ItemCartDAO(activity);
        itemStockDAO = new ItemStockDAO(activity);
        simpleItemDAO = new SimpleItemDAO(activity);
    }


     public class mViewHolder extends  RecyclerView.ViewHolder{
        final TextView product_NameAmount;
        final View viewForeground;
        final View viewBackground;

        mViewHolder(View itemView) {
            super(itemView);

            product_NameAmount = itemView.findViewById(R.id.itemCart_itemList_nameAmount);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.cart_view_foreground);
        }
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_item_cart, parent, false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, int position) {
        ItemCart itemCart = products.get(position);
        holder.product_NameAmount.setText(res.getString(R.string.itemcart_itemList_nameAmount,itemCart.getAmount(),itemCart.getProduct().getName()));

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
    public void onItemDismiss(final int position) {

        final ItemCart itemCart = products.get(position);
        String name = itemCart.getProduct().getName();

        Snackbar snackbar = removeTemporarily(position,itemCart,  name + " removido do carrinho.");

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(!itemCart.isRemoved()) {
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

    public void remove(int position) {
        final ItemCart itemCart = products.get(position);
        Snackbar snackbar = removeTemporarily(position, itemCart, "Concluido");

        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event){
                if(!itemCart.isRemoved()){
                    if(itemCart.getId() != null){
                        itemCartDAO.remove( itemCart.getId() );
                        if(itemCart.isUpdateStock()) {
                            ItemStock itemStock = itemStockDAO.findByProductId(itemCart.getId());
                            itemStock.setAmount(itemCart.getAmount() + itemStock.getActualAmount());
                            itemStock.setTotal(itemStock.getActualAmount(),itemStock.getAmount());
                            itemStockDAO.update(itemStock);
                        }
                    } else if(itemCart.getConvertedId() != null) {
                        simpleItemDAO.remove( itemCart.getConvertedId());
                    }
                }
            }
        });
        snackbar.show();
    }
    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return  ((CartAdapter.mViewHolder) viewHolder).viewForeground;
    }



    @NonNull
    private Snackbar removeTemporarily(final int position, final ItemCart itemCart, String message) {
        // Remover Temporiariamente
        products.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.cart_view_main), message, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);

        // Desfazer remoção
        snackbar.setAction(R.string.snackbar_undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.add(position,itemCart);
                notifyItemInserted(position);
                itemCart.setRemoved(true);
            }
        });
        return snackbar;
    }
}

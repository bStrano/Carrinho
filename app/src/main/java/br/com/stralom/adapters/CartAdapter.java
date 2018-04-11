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
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.ItemCart;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.mViewHolder> implements ItemTouchHelperAdapter {
    private final List<ItemCart> products;
    private final Activity activity;
    private final CartDAO cartDAO;
    private final SimpleItemDAO simpleItemDAO;
    private boolean undoSwipe = false;
    private Resources res;

    public CartAdapter(List<ItemCart> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        res = activity.getResources();
        cartDAO = new CartDAO(activity);
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

        // Remover Temporiariamente
        products.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.cart_view_main), name + " removido do carrinho!", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);

        // Desfazer remoção
        snackbar.setAction("DESFAZER", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                products.add(position,itemCart);
                notifyItemInserted(position);
                undoSwipe = true;
            }
        });

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(!undoSwipe) {
                    Log.e("TAG","ItemCart ID: " + itemCart.getId() + "SimpleItem ID: " + itemCart.getConvertedId());
                    if(itemCart.getId() != null) {
                        cartDAO.remove( itemCart.getId());
                    } else if(itemCart.getConvertedId() != null){
                        simpleItemDAO.remove( itemCart.getConvertedId());

                    }
                }


            }
        });

        snackbar.show();
        //

    }

    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return  ((CartAdapter.mViewHolder) viewHolder).viewForeground;
    }




}

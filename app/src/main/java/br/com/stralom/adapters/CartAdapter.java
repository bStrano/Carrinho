package br.com.stralom.adapters;

import android.app.Activity;
import android.graphics.Color;
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
    private List<ItemCart> products;
    private Activity activity;
    private CartDAO cartDAO;
    private SimpleItemDAO simpleItemDAO;
    private boolean undoSwipe = false;

    public CartAdapter(List<ItemCart> products, Activity activity) {
        this.products = products;
        this.activity = activity;
        cartDAO = new CartDAO(activity);
        simpleItemDAO = new SimpleItemDAO(activity);
    }


    public class mViewHolder extends  RecyclerView.ViewHolder{
        public TextView productName;
        public TextView productAmount;
        public View viewForeground;
        public View viewBackground;

        public mViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productAmount = itemView.findViewById(R.id.product_amount);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.cart_view_foreground);
        }
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_item_cart, parent, false);
        return new mViewHolder(v);
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {
        ItemCart itemCart = products.get(position);
        holder.productName.setText(itemCart.getProduct().getName());
        holder.productAmount.setText(String.valueOf(itemCart.getAmount()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
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
        return true;

    }



    @Override
    public void onItemDismiss(final int position) {

        final ItemCart itemCart = products.get(position);
        final int deletedIndex = position;

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
                products.add(deletedIndex,itemCart);
                notifyItemInserted(deletedIndex);
                undoSwipe = true;
            }
        });

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(undoSwipe == false) {
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

    public void removeItem(int position){

    }


}

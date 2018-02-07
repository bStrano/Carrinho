package br.com.stralom.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemCart;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.mViewHolder> implements ItemTouchHelperAdapter {
    private List<ItemCart> products;
    private Context context;

    public CartAdapter(List<ItemCart> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_cart, parent, false);
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
    public void onItemDismiss(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    public class mViewHolder extends  RecyclerView.ViewHolder{
        TextView productName;
        TextView productAmount;

        public mViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.product_name);
            productAmount = itemView.findViewById(R.id.product_amount);
        }
    }



}

package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.ObservableArrayList;
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

import java.util.HashMap;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.interfaces.EditMenuInterface;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends BaseAdapter<CartAdapter.CartViewHolder,ItemCart>  {
    private final ItemCartDAO itemCartDAO;
    private final SimpleItemDAO simpleItemDAO;
    private final ItemStockDAO itemStockDAO;
    private Resources res;




    public CartAdapter(ObservableArrayList<ItemCart> itemCarts, Activity activity) {
        super(itemCarts,activity);
        res = activity.getResources();
        itemCartDAO = new ItemCartDAO(activity);
        itemStockDAO = new ItemStockDAO(activity);
        simpleItemDAO = new SimpleItemDAO(activity);
    }

    @Override
    public void edit() {

    }


    public class CartViewHolder extends  RecyclerView.ViewHolder{
        final TextView productName;
        final TextView productAmount;
        final ImageView categoryIcon;
        final View viewBackground;


        CartViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.itemCart_itemList_name);
            productAmount = itemView.findViewById(R.id.itemCart_itemList_amount);
            viewBackground = itemView.findViewById(R.id.cart_view_foreground);
            categoryIcon = itemView.findViewById(R.id.itemCart_itemList_categoryIcon);

        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ItemCart itemCart = list.get(position);
        //holder.productName.setText(res.getString(R.string.itemcart_itemList_nameAmount,itemCart.getAmount(),itemCart.getProduct().getName()));
        holder.productName.setText(itemCart.getProduct().getName());
        holder.productAmount.setText(res.getString(R.string.itemcart_itemList_amount, itemCart.getAmount()));
        holder.viewBackground.setBackgroundColor(Color.parseColor("#FAFAFA"));

        try{
            holder.categoryIcon.setImageResource(itemCart.getProduct().getCategory().getIconFlag());
        } catch (NullPointerException e){
            holder.categoryIcon.setImageResource(R.drawable.ic_add);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void removePermanently(ItemCart itemCart) {
        Log.d("DEBUG", "24984984");
        if(itemCart.getId() != null) {
            itemCartDAO.remove( itemCart.getId());
        } else if(itemCart.getConvertedId() != null){
            simpleItemDAO.remove( itemCart.getConvertedId());

        }
    }


//    public void completeItem(int position, TextView textView) {
//        final ItemCart itemCart = itemCarts.get(position);
//        snackbar = removeTemporarily(position, itemCart, "Concluido",textView);
//        snackbar.addCallback(new Snackbar.Callback(){
//            @Override
//            public void onDismissed(Snackbar transientBottomBar, int event){
//                if(itemCart.isRemoved()){
//                    if(itemCart.getId() != null){
//                        itemCartDAO.remove( itemCart.getId() );
//                        if(itemCart.isUpdateStock()) {
//                            ItemStock itemStock = itemStockDAO.findByProductId(itemCart.getProduct().getId());
//                            if(itemStock != null){
//                                itemStock.setActualAmount(itemCart.getAmount() + itemStock.getActualAmount());
//                                itemStock.setTotal(itemStock.getActualAmount(),itemStock.getAmount());
//                                itemStockDAO.update(itemStock);
//                            }
//                        }
//                    } else if(itemCart.getConvertedId() != null) {
//                        simpleItemDAO.remove( itemCart.getConvertedId());
//                    }
//                }
//                super.onDismissed(transientBottomBar,event);
//            }
//        });
//        snackbar.show();
//    }











}

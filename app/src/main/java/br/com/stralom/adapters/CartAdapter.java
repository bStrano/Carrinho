package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ItemCartDAO;
import br.com.stralom.dao.ItemStockDAO;
import br.com.stralom.dao.SimpleItemDAO;
import br.com.stralom.entities.Category;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends BaseAdapter<RecyclerView.ViewHolder,ItemCart>  {
    private final ItemCartDAO itemCartDAO;
    private final SimpleItemDAO simpleItemDAO;
    private final ItemStockDAO itemStockDAO;
    private Resources res;
    private static int categoryCount = 0;

    private ArrayList<Pair<Category,Integer>> holderSections  = new ArrayList<>();
    private ArrayList<Object> listWithSections;
    private ArrayList<Object> listWithSectionsClone;




    public CartAdapter(ObservableArrayList<ItemCart> itemCarts, Activity activity) {
        super(itemCarts,activity);
        res = activity.getResources();
        itemCartDAO = new ItemCartDAO(activity);
        itemStockDAO = new ItemStockDAO(activity);
        simpleItemDAO = new SimpleItemDAO(activity);
        if(list.size() > 0){
            setUpSections();
            listWithSectionsClone = (ArrayList<Object>) listWithSections.clone();
        }



    }

    @Override
    public void edit() {

    }


    public class CartViewHolder extends  RecyclerView.ViewHolder{
        final TextView productName;
        final TextView productAmount;
        final View viewBackground;


        CartViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.itemCart_itemList_name);
            productAmount = itemView.findViewById(R.id.itemCart_itemList_amount);
            viewBackground = itemView.findViewById(R.id.cart_view_foreground);


        }
    }


    class CartSectionViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private ImageView sectionIcon;

        public CartSectionViewHolder(View itemView) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.section_itemcart_name);
            sectionIcon = itemView.findViewById(R.id.section_itemcart_icon);
        }
    }


    public void customNotifyDataSetChanged(){
        if(listWithSections != null){
            listWithSections.clear();
            holderSections.clear();
        }

        if(list.size() > 0){
            setUpSections();
        }

        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {


        if(position == 0){
            return 0;

        } else if (position == 1 ){
            return 1;
        } else if (position >1){
            ItemCart itemCart = list.get(getItemPosition(position));
            String categoryName = itemCart.getProduct().getCategory().getName();

            String previousCategoryName = list.get(getItemPosition(position-1)).getProduct().getCategory().getName();
            if(!categoryName.equals(previousCategoryName)){
                return 0;
            }
        }
            return 1;



//        ItemCart itemCart = list.get(categoryCount);
//        if( !holderSections.contains(itemCart.getProduct().getCategory())) {
//            Log.d("DEBUGS", String.valueOf(categoryCount));
//            categoryCount++;
//            return 0;
//        } else {
//            Log.d("DEBUG","YYY");
//            return 1;
//        }
    }

    @Override
    public int getItemPosition(int position){
        int categories = 0;
        if(position == 0 || position == 1){
            return  0;
        }
        for (Pair pair: holderSections) {
            if((int) pair.second < position ){
                categories++;
            }  else {
                break;
            }
        }
        return position - categories;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view ;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(activity).inflate(R.layout.list_section_cart,parent, false);

                view.setClickable(false);
                view.setFocusable(false);
                view.setEnabled(false);
                return new CartSectionViewHolder(view);
            default:
                view = LayoutInflater.from(activity).inflate(R.layout.list_item_cart, parent, false);
                return new CartViewHolder(view);

        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                Category category =  list.get(getItemPosition(position)).getProduct().getCategory();
                CartSectionViewHolder sectionHolder = (CartSectionViewHolder) holder;
                sectionHolder.sectionName.setText(category.getName());
                sectionHolder.sectionIcon.setImageResource(category.getIconFlag());
                break;
            case 1:
                ItemCart itemCart =  list.get(getItemPosition(position));
                CartViewHolder cartViewHolder = (CartViewHolder) holder;
                cartViewHolder.productName.setText(itemCart.getProduct().getName());
                cartViewHolder.productAmount.setText(res.getString(R.string.itemcart_itemList_amount, itemCart.getAmount()));
                cartViewHolder.viewBackground.setBackgroundColor(Color.parseColor("#FAFAFA"));
                break;


        }



    }

    @Override
    public int getItemCount() {
        return list.size()+holderSections.size();
    }

    @Override
    public void removePermanently(ItemCart itemCart) {
        if(itemCart.getId() != null) {
            itemCartDAO.remove( itemCart.getId());
        } else if(itemCart.getConvertedId() != null){
            simpleItemDAO.remove( itemCart.getConvertedId());

        }
    }

    @Override
    public void removeTemporally() {
        int removeElements = 0;
        for (Map.Entry hash:selectedElements.entrySet())
        {
            list.remove((int) hash.getKey() - removeElements);
            removeElements++;
        }

        customNotifyDataSetChanged();

    }

    @Override
    protected void undoRemove(){
        for (Map.Entry hash: selectedElementsClone.entrySet()) {
            list.add((Integer) hash.getKey(), (ItemCart) hash.getValue());
        }
        customNotifyDataSetChanged();
    }



    private void setUpSections(){
        listWithSections = new ArrayList<>();
        Product initialProduct = list.get(0).getProduct();
        Category initialCategory = initialProduct.getCategory();
        newSection(initialProduct, initialCategory);

        ItemCart itemCart;
        for (int index = 1  ; index<list.size() ; index++ ) {
            itemCart = list.get(index);
            if(!itemCart.getProduct().getCategory().getName().equals(initialCategory.getName())){
                newSection(itemCart.getProduct(),itemCart.getProduct().getCategory());
                initialCategory = itemCart.getProduct().getCategory();
            } else {
                listWithSections.add(itemCart.getProduct());
            }

        }
    }

    private void newSection(Product initialProduct, Category initialCategory) {
        holderSections.add(Pair.create(initialCategory,listWithSections.size()));
        listWithSections.add(initialCategory);
        listWithSections.add(initialProduct);

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

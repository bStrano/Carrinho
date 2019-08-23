package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.content.res.Resources;
import androidx.databinding.ObservableArrayList;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.ProductDAO;
import br.com.stralom.compras.entities.Category;
import br.com.stralom.compras.entities.Product;
import br.com.stralom.compras.interfaces.ItemCheckListener;

/**
 * Created by Bruno Strano on 30/01/2018.
 */

public class CartAdapter extends BaseAdapter<RecyclerView.ViewHolder,Product>  {
    private static final String TAG = "CartAdapter";
    private Resources res;
    private List<RecyclerView.ViewHolder> listCartHolder;
    private ProductDAO productDAO;


    private Pair<Product,Integer> concludedElement;
    private ItemCheckListener itemCheckListener;


    private ArrayList<Pair<Category,Integer>> holderSections  = new ArrayList<>();
    private ArrayList<Object> listWithSections;





    public CartAdapter(ItemCheckListener itemCheckListener,ObservableArrayList<Product> products, Activity activity) {
        super(products,activity);
        res = activity.getResources();

        if(list.size() > 0){
            setUpSections();
        }
        listCartHolder = new ArrayList<>();
        this.itemCheckListener = itemCheckListener;
        productDAO = new ProductDAO(activity);
    }

    @Override
    public void edit() {

    }


    public class CartViewHolder extends  RecyclerView.ViewHolder{
        final TextView productName;
        final TextView productAmount;
        final View viewBackground;
        final CheckBox checkBox;

        CartViewHolder(View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.itemCart_itemList_name);
            productAmount = itemView.findViewById(R.id.itemCart_itemList_amount);
            viewBackground = itemView.findViewById(R.id.cart_view_foreground);
            checkBox = itemView.findViewById(R.id.itemCart_itemList_check);


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
        Log.d(TAG, "Custom Notifyt Dataset changed");
        if(listWithSections != null){
            Log.d(TAG, "listWithSections != null");
            listWithSections.clear();
            holderSections.clear();
        }

        if(list.size() > 0){
            Log.d(TAG, "listsize > 0");
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
            Product product = list.get(getItemPosition(position));
            String categoryName = product.getCategory().getTag();

            String previousCategoryName = list.get(getItemPosition(position-1)).getCategory().getTag();
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()){
            case 0:
                Category category =  list.get(getItemPosition(position)).getCategory();
                CartSectionViewHolder sectionHolder = (CartSectionViewHolder) holder;
                sectionHolder.sectionName.setText(category.getTag());
                sectionHolder.sectionIcon.setImageResource(category.getIconFlag());
                break;
            case 1:
                Product product =  list.get(getItemPosition(position));
                CartViewHolder cartViewHolder = (CartViewHolder) holder;
                cartViewHolder.productName.setText(product.getName());
                cartViewHolder.productAmount.setText(res.getString(R.string.itemcart_itemList_amount, product.getItemCart().getFormattedAmount()));
                cartViewHolder.viewBackground.setBackgroundColor(Color.parseColor("#FAFAFA"));
                cartViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            concludeElement(holder.getAdapterPosition());
                            itemCheckListener.showConfirmSnackbar();
                            compoundButton.setChecked(false);
                        }
                    }
                });
                break;


        }



    }

    public void undoConcludedElement(Pair<Product,Integer> concludedElement ){
        concludedElement.first.getItemCart().setRemoved(false);
        list.add(concludedElement.second,concludedElement.first);
        customNotifyDataSetChanged();
    }

    private void concludeElement(int position){
        int itemIndex = getItemPosition(position);
        Product product = list.get(itemIndex);
        product.getItemCart().setRemoved(true);
        concludedElement = Pair.create(product, itemIndex);
        list.remove(itemIndex);


        customNotifyDataSetChanged();


    }

    @Override
    public int getItemCount() {
        return list.size()+holderSections.size();
    }

    @Override
    public void removePermanently(Product product) {
        if(product.getId() != null) {
            productDAO.remove(product.getId());
            //itemCartDAO.remove(product.getId());
        }
//        } else if(product.getConvertedId() != null){
//            simpleItemDAO.remove( product.getConvertedId());
//
//        }
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
            list.add((Integer) hash.getKey(), (Product) hash.getValue());
        }
        customNotifyDataSetChanged();
    }



    private void setUpSections(){
        listWithSections = new ArrayList<>();
        Product initialProduct = list.get(0);
        Category initialCategory = initialProduct.getCategory();
        newSection(initialProduct, initialCategory);

        Product product;
        for (int index = 1  ; index<list.size() ; index++ ) {
            product = list.get(index);
            if(!product.getCategory().getTag().equals(initialCategory.getTag())){
                newSection(product,product.getCategory());
                initialCategory = product.getCategory();
            } else {
                listWithSections.add(product);
            }

        }
    }

    private void newSection(Product initialProduct, Category initialCategory) {
        holderSections.add(Pair.create(initialCategory,listWithSections.size()));
        listWithSections.add(initialCategory);
        listWithSections.add(initialProduct);

    }


    public Pair<Product, Integer> getConcludedElement() {
        return concludedElement;
    }

    public void setConcludedElement(Pair<Product, Integer> concludedElement) {
        this.concludedElement = concludedElement;
    }












}

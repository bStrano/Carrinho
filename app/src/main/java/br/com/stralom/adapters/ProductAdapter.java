package br.com.stralom.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.ObservableArrayList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.dao.ProductDAO;
import br.com.stralom.entities.Product;
import br.com.stralom.interfaces.ItemClickListener;

/**
 * Created by Bruno on 24/02/2018.
 */

public class ProductAdapter extends BaseAdapter<ProductAdapter.ProductViewHolder,Product> implements Filterable {
    private final ProductDAO productDAO;
    private Resources res;
    private ArrayList<Product> listClone;
    final private ItemClickListener clickListener;

    public ProductAdapter(ObservableArrayList<Product> products, Activity activity, ItemClickListener recyclerClickListener) {
        super(products,activity);
        listClone = (ArrayList<Product>) products.clone();
        res = activity.getResources();
        productDAO = new ProductDAO(activity);
        this.clickListener = recyclerClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = list.get(position);
        holder.name.setText(product.getName());
        holder.price.setText(String.format(res.getString(R.string.product_itemList_price), product.getPrice()));
         holder.categoryIcon.setImageResource(product.getCategory().getIconFlag());
         holder.categoryIcon.setTag(product.getCategory().getIconFlag());
        holder.viewForeground.setBackgroundColor(Color.parseColor("#FAFAFA"));

        //holder.categoryIcon.setImageDrawable(R.drawable.ic_add);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void removePermanently(Product item) {
        productDAO.remove(item.getId());
    }


    @Override
    public void edit() {

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Product> filteredList = new ArrayList<>();
                String filteredString = charSequence.toString().toLowerCase().trim();
                if(filteredString.isEmpty()){
                    filteredList.addAll(listClone);
                } else {
                    for (Product product:listClone) {
                        if(product.getName().toLowerCase().trim().startsWith(filteredString)){
                            filteredList.add(product);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
                
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list.clear();
                list.addAll((ArrayList<Product>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView categoryIcon;
        final TextView name;
        final TextView price;
        final View viewForeground;




        ProductViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_itemList_name);
            price = itemView.findViewById(R.id.product_itemList_price);
            categoryIcon = itemView.findViewById(R.id.product_itemList_categoryIcon);
            viewForeground = itemView.findViewById(R.id.product_view_foreground);

            itemView.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            clickListener.onClick(view,position);
        }
    }


    // Retorna o icone de acordo com a categoria. As categorias
    int getIcon(Product product){
        String productName = product.getName();
        String categories[] = res.getStringArray(R.array.categories);

        return 0;
    }
}

package br.com.stralom.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemCart;
import br.com.stralom.entities.Product;

/**
 * Created by Bruno Strano on 12/07/2018.
 */
public class ItemCartProductAdapter extends RecyclerView.Adapter<ItemCartProductAdapter.ViewHolder> implements Filterable {
    private ArrayList<Product> products;
    private ArrayList<Product> productsClone;
    private Activity activity;
    private static boolean filtering = false;
    // <Index,Amount>
    private HashMap<Product,Integer> selectedPositions;

    public ItemCartProductAdapter(ArrayList<Product> products, Activity activity) {
        this.products = products;
        productsClone = (ArrayList<Product>) this.products.clone();
        this.activity = activity;
        selectedPositions = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_itemcartregistration_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.productAmount.setText("");

        Product product1 = products.get(position);
        if(selectedPositions.containsKey(product1)){
            setAddButtonChecked(holder);
            holder.manageButton.setEnabled(true);
           if(selectedPositions.get(product1) ==  1){
                holder.manageButton.setBackgroundResource(R.drawable.ic_cancel);
            } else {
                holder.manageButton.setBackgroundResource(R.drawable.ic_remove);
            }


            holder.productAmount.setText(String.valueOf(selectedPositions.get(product1)));
        } else {
            holder.manageButton.setChecked(false);
            holder.addButton.setChecked(false);
            holder.addButton.setBackgroundResource(R.drawable.ic_add_with_circle);
            holder.manageButton.setEnabled(false);
            holder.manageButton.setBackgroundResource(0);
        }


        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAddButtonChecked(holder);
                Product product = getProduct(position);
                if(!selectedPositions.containsKey(product)){
                    selectedPositions.put(product,1);
                } else {
                    selectedPositions.put(product,selectedPositions.get(product) + 1);
                    //selectedPositions.put(position, selectedPositions.get(position).getAmount() +1);

                }
                notifyDataSetChanged();
                Toast.makeText(activity,"Teste: " + selectedPositions.get(product),Toast.LENGTH_LONG).show();
            }
        });
        holder.manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = getProduct(position);
                if(selectedPositions.get(product)== 1){

                    selectedPositions.remove(product);
                } else {
                    selectedPositions.put(product, selectedPositions.get(product) -1);
                }
                notifyDataSetChanged();
            }
        });


    }

    private Product getProduct(int position) {
        if(filtering){
            return  products.get(position);
        } else {
            return productsClone.get(position);
        }
    }


    private void setAddButtonChecked(@NonNull ViewHolder holder) {
        holder.addButton.setChecked(true);
        holder.addButton.setBackgroundResource(R.drawable.ic_added_with_circle);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Product> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0) {
                    filtering = false;
                    filteredList.addAll(productsClone);
                } else {
                    filtering = true;
                    for (Product product : productsClone) {
                        if (product.getName().toLowerCase().trim().contains(charSequence.toString().toLowerCase().trim())) {
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
                products.clear();
                products.addAll((Collection<? extends Product>) filterResults.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ToggleButton addButton;
        public ToggleButton manageButton;
        public TextView productName;
        public TextView productAmount;


        public ViewHolder(View itemView) {
            super(itemView);
            this.addButton = itemView.findViewById(R.id.registration_itemCart_btnAdd);
            this.manageButton = itemView.findViewById(R.id.registration_itemCart_btnManagement);
            this.productName = itemView.findViewById(R.id.registration_itemCart_txtProductName);
            this.productAmount = itemView.findViewById(R.id.registration_itemCart_txtProductAmount);
        }
    }
}

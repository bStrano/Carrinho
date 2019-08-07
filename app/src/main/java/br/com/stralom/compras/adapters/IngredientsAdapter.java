package br.com.stralom.compras.adapters;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> implements Filterable{
    private ArrayList<Product> products;
    private ArrayList<Product> productsClone;
    private ArrayList<ItemRecipe> ingredients;
    private Activity activity;
    private ArrayList<ViewHolder> holders;


    public IngredientsAdapter(Activity activity, ArrayList<Product> products, ArrayList<ItemRecipe> ingredients) {
        this.products = products;
        this.activity = activity;
        this.ingredients = ingredients;
        this.productsClone = (ArrayList<Product>) products.clone();
        holders = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_itemrecipe,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        holders.add(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getName());
        holder.product = product;
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.amountLayout.requestFocus();

            }
        });
        ItemRecipe ingredient = getIngredient(holder.productName.getText().toString());
        if (ingredient != null) {
            ingredient.setProduct(product);
            ingredient.setTotal(ingredient.getAmount(),product.getPrice());
            holder.amount.setText(String.valueOf(ingredient.getAmount()));
            holder.mainView.setBackgroundResource(R.color.bg_lightBlue);
        } else {
            holder.amount.setText("");
            holder.mainView.setBackgroundResource(android.R.color.background_light);
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<Product> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0 ) {
                    filteredList.addAll(productsClone);
                } else {
                    for (Product product : productsClone) {
                        if (product.getName().toLowerCase().trim().startsWith(charSequence.toString().toLowerCase().trim())) {
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
                products.addAll((ArrayList<Product>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout mainView;
        TextView productName;
        TextInputEditText amount;
        TextInputLayout amountLayout;
        Product product;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView.findViewById(R.id.item_ingredient_main);
            productName = itemView.findViewById(R.id.item__ingredient_ProductName);
            amount = itemView.findViewById(R.id.item_ingredient_amount);
            amountLayout = itemView.findViewById(R.id.item_ingredient_amountLayout);

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int start, int before, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int start, int before, int i2) {

                    String productNameString = productName.getText().toString();
                    ItemRecipe itemRecipe = getIngredient(productNameString);
                    if( (!charSequence.toString().isEmpty() && (!charSequence.toString().equals("0")))){
                        mainView.setBackgroundResource(R.color.bg_lightBlue);
                        Log.d("SelectedProducts", "Start: " + start + "Before: " + before + " i2: " + i2  );
                        if(start == 0 ){


                            if(itemRecipe == null){
                                itemRecipe = new ItemRecipe(Double.parseDouble(amount.getText().toString()), product);
                                ingredients.add(itemRecipe);
                            }

                        } else  {
                            itemRecipe.setAmount(Double.parseDouble(amount.getText().toString()));
                        }

                    } else {
                        if(itemRecipe != null){
                            ingredients.remove(itemRecipe);
                        }
                        mainView.setBackgroundResource(R.color.bg_basic);
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    private ItemRecipe getIngredient(String productName){

        for (ItemRecipe itemRecipe: ingredients) {
            if(itemRecipe.getProduct() != null){
                if(itemRecipe.getProduct().getName().equals(productName)){
                    return itemRecipe;
                }
            }

        }
        return null;
    }



    public ArrayList<ItemRecipe> getSelectedItems() {
        return ingredients;
    }

}

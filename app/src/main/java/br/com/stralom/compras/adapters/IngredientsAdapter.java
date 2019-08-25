package br.com.stralom.compras.adapters;


import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.RecyclerView;
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
    private static final String TAG = "INGREDIENTADAPTER" ;
    private ArrayList<ItemRecipe> ingredients;
    private ArrayList<ItemRecipe> ingredientsClone;
    private Activity activity;
    private ArrayList<ViewHolder> holders;


    public IngredientsAdapter(Activity activity, ArrayList<ItemRecipe> ingredients) {
        this.activity = activity;
        this.ingredients = ingredients;
        this.ingredientsClone = (ArrayList<ItemRecipe>) this.ingredients.clone();
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
        Log.d("on bind view golder : ", String.valueOf(position));
        ItemRecipe ingredient = ingredients.get(position);
        Log.d(TAG,"on bind view golder" +  ingredient.toString());
        holder.productName.setText(ingredient.getProduct().getName());
        holder.mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.amountLayout.requestFocus();

            }
        });

        if (ingredient.getAmount() > 0) {
            holder.amount.setText(String.valueOf(ingredient.getAmount()));
            holder.mainView.setBackgroundResource(R.color.bg_lightBlue);
        } else {
            holder.amount.setText("");
            holder.mainView.setBackgroundResource(android.R.color.white);
        }
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<ItemRecipe> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0 ) {
                    filteredList.addAll(ingredientsClone);
                } else {
                    for (ItemRecipe ingredient : ingredientsClone) {
                        if (ingredient.getProduct().getName().toLowerCase().trim().startsWith(charSequence.toString().toLowerCase().trim())) {
                            filteredList.add(ingredient);
                        }
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ingredients.clear();
                ingredients.addAll((ArrayList<ItemRecipe>) filterResults.values);
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
                        itemRecipe.setAmount(Double.parseDouble(amount.getText().toString()));
                    } else {
                        itemRecipe.setAmount(0.0);
                        mainView.setBackgroundResource(R.color.bg_lightBlue);
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
        ArrayList<ItemRecipe> selectedIngredients = new ArrayList<>();
        for(ItemRecipe ingredient: ingredients){
            if(ingredient.getAmount() > 0) {
                selectedIngredients.add(ingredient);
            }
        }
        return selectedIngredients;
    }

}

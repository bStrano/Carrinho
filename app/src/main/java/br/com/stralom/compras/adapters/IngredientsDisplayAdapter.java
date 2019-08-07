package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;

public class IngredientsDisplayAdapter extends RecyclerView.Adapter<IngredientsDisplayAdapter.ViewHolder> {
    private Activity activity;
    private ArrayList<ItemRecipe> ingredients;

    public IngredientsDisplayAdapter(Activity activity, ArrayList<ItemRecipe> ingredients) {
        this.activity = activity;
        this.ingredients = ingredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView amount;


        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.item__ingredient_ProductName);
            amount = itemView.findViewById(R.id.item_ingredient_amount);

            productName = itemView.findViewById(R.id.list_item_readitemrecipe_product);
            amount = itemView.findViewById(R.id.list_item_readitemrecipe_amount);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_readitemrecipe,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemRecipe itemRecipe = ingredients.get(position);
        holder.productName.setText(itemRecipe.getProduct().getName());
        String amount = String.valueOf(itemRecipe.getFormattedAmount() + "x");
        holder.amount.setText(amount);
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}

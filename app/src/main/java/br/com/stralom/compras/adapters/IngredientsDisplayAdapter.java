package br.com.stralom.compras.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.ItemRecipe;
import br.com.stralom.compras.entities.Product;

public class IngredientsDisplayAdapter extends RecyclerView.Adapter<IngredientsDisplayAdapter.ViewHolder> {
    private static final String TAG = "IngsDispAdapter" ;
    private Activity activity;
    private ArrayList<ItemRecipe> ingredients;
    private ArrayList<Product> products;

    public IngredientsDisplayAdapter(Activity activity, ArrayList<ItemRecipe> ingredients,ArrayList<Product> products) {
        this.activity = activity;
        this.ingredients = ingredients;
        this.products = products;
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
        Product product = products.get(position);
        Log.d(TAG,itemRecipe.toString());
        holder.productName.setText(product.getName());
        String amount = String.valueOf(itemRecipe.getFormattedAmount() + "x");
        holder.amount.setText(amount);
    }


    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}

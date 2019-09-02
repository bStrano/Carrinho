package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.content.res.Resources;
import androidx.databinding.ObservableArrayList;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Profile;
import br.com.stralom.compras.entities.Recipe;



public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ProfileItemAdapterHolder> {

    private ArrayList<Profile> profileList;
    private Activity activity;

    public ProfileItemAdapter(ArrayList<Profile> profileList, Activity activity) {
        super();
        this.profileList = profileList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ProfileItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_profiles,parent, false);
        return new ProfileItemAdapterHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ProfileItemAdapterHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.productText.setText(activity.getString(R.string.profile_itemList_productCount, profile.getProductsNumber()));
        holder.recipeText.setText(activity.getString(R.string.profile_itemList_recipeCount,profile.getRecipesNumber()));
        holder.cartText.setText(activity.getString(R.string.profile_itemList_cartCount,profile.getItemCartNumber()));
        holder.stockText.setText(activity.getString(R.string.profile_itemList_stockCount, profile.getItemStockNumber()));
//        Recipe recipe = list.get(position);
//        holder.name.setText(String.format(res.getString(R.string.recipe_itemList_name),recipe.getName()));
//        holder.price.setText(String.format(res.getString(R.string.recipe_itemList_price), recipe.getTotal()));
//        holder.ingredientCount.setText(String.format(res.getString(R.string.recipe_itemList_ingredientCount),recipe.getIngredientCount()));
//        holder.foregroundView.setBackgroundColor(Color.parseColor("#FAFAFA"));


    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }




    public class ProfileItemAdapterHolder extends RecyclerView.ViewHolder{
        public TextView productText;
        public TextView recipeText;
        public TextView cartText;
        public TextView stockText;
        public ProfileItemAdapterHolder(@NonNull View itemView) {
            super(itemView);
            this.productText = itemView.findViewById(R.id.item_profile_product);
            this.recipeText = itemView.findViewById(R.id.item_profile_recipe);
            this.cartText = itemView.findViewById(R.id.item_profile_cart);
            this.stockText = itemView.findViewById(R.id.item_profile_stock);
        }
    }





}

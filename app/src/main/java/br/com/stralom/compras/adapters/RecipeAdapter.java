package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.content.res.Resources;
import androidx.databinding.ObservableArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.stralom.compras.R;
import br.com.stralom.compras.dao.RecipeDAO;
import br.com.stralom.compras.entities.Recipe;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class RecipeAdapter extends BaseAdapter<RecipeAdapter.RecipeViewHolder,Recipe> {
    private final RecipeDAO recipeDAO;
    private final Resources res ;

    public RecipeAdapter(ObservableArrayList<Recipe> recipeList, Activity activity) {
        super(recipeList,activity);
        res = activity.getResources();
        recipeDAO = new RecipeDAO(activity);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe,parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = list.get(position);
        holder.name.setText(String.format(res.getString(R.string.recipe_itemList_name),recipe.getName()));
        holder.price.setText(String.format(res.getString(R.string.recipe_itemList_price), recipe.getTotal()));
        holder.ingredientCount.setText(String.format(res.getString(R.string.recipe_itemList_ingredientCount),recipe.getIgredientCount()));
        holder.foregroundView.setBackgroundColor(Color.parseColor("#FAFAFA"));

        String imagePath = recipe.getImagePath();
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmapReduced = Bitmap.createScaledBitmap(bitmap,100,100,true);
            holder.image.setImageBitmap(bitmapReduced);
            holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void removePermanently(Recipe item) {
        recipeDAO.remove(item.getId());
    }




    @Override
    public void edit() {

    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        final TextView name;
        final TextView price;
        final TextView ingredientCount;
        final ImageView image;
        final View foregroundView;


        RecipeViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recipe_name);
            price = itemView.findViewById(R.id.recipe_price);
            ingredientCount = itemView.findViewById(R.id.recipe_ingredientCount);
            image = itemView.findViewById(R.id.recipe_image);
            foregroundView = itemView.findViewById(R.id.recipe_view_foreground);
        }
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Recipe recipe = list.get(position);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = convertView;
//        if(convertView == null) {
//            view = inflater.inflate(R.layout.list_item_recipe,parent, false);
//        }
//
//        TextView name = view.findViewById(R.id.recipe_name);
//        name.setText(recipe.getName());
//
//        TextView ingredientCount = view.findViewById(R.id.recipe_ingredientCount);
//        ingredientCount.setText("Numero de Ingrediente: " + String.valueOf(recipe.getIgredientCount()));
//
//        TextView price = view.findViewById(R.id.recipe_price);
//        price.setText("Preço: " + String.valueOf(recipe.getTotal()) + " R$");
//
//        ImageView image = view.findViewById(R.id.recipe_image);
//        String imagePath = recipe.getImagePath();
//        if(imagePath != null) {
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            Bitmap bitmapReduced = Bitmap.createScaledBitmap(bitmap,100,100,true);
//            image.setImageBitmap(bitmapReduced);
//            image.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
//
//
//        return view;
//    }


}

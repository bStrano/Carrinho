package br.com.stralom.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.dao.RecipeDAO;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> implements ItemTouchHelperAdapter {
    private List<Recipe> recipeList;
    private Activity activity ;
    private boolean undoSwipe;
    private RecipeDAO recipeDAO;

    public RecipeAdapter(List<Recipe> recipeList, Activity activity) {
        this.recipeList = recipeList;
        this.activity = activity;
        this.undoSwipe = false;
        recipeDAO = new RecipeDAO(activity);
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_recipe,parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.name.setText(recipe.getName());
        holder.price.setText("Preço: " + Double.toString(recipe.getTotal()));
        holder.ingredientCount.setText("Numero de Ingredientes: " + Integer.toString(recipe.getIgredientCount()));


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
        return recipeList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        final Recipe recipe = recipeList.get(position);
        final int deletedIndex = position;

        String name = recipe.getName();

        // Remover Temporiariamente
        recipeList.remove(position);
        notifyItemRemoved(position);

        Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.recipe_view_main), name + " removido do carrinho!", Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.BLUE);

        // Desfazer remoção
        snackbar.setAction("DESFAZER", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeList.add(deletedIndex,recipe);
                notifyItemInserted(deletedIndex);
                undoSwipe = true;
            }
        });

        // Remover Definitivamente
        snackbar.addCallback(new Snackbar.Callback(){
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                if(undoSwipe == false) {
                     recipeDAO.remove(recipe.getId());
                } else {
                    undoSwipe = false;
                }


            }
        });

        snackbar.show();
        //

    }

    @Override
    public View getForegroundView(RecyclerView.ViewHolder viewHolder) {
        return ((RecipeViewHolder) viewHolder).foregroundView;
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView price;
        TextView ingredientCount;
        ImageView image;
        View foregroundView;


        public RecipeViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recipe_name);
            price = itemView.findViewById(R.id.recipe_price);
            ingredientCount = itemView.findViewById(R.id.recipe_ingredientCount);
            image = itemView.findViewById(R.id.recipe_image);
            foregroundView = itemView.findViewById(R.id.product_view_foreground);
        }
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Recipe recipe = recipeList.get(position);
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

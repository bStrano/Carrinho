package br.com.stralom.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 11/01/2018.
 */

public class RecipeAdapter extends BaseAdapter {
    private List<Recipe> recipeList;
    private Context context ;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  recipeList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = recipeList.get(position);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = convertView;
        if(convertView == null) {
            view = inflater.inflate(R.layout.list_item_recipe,parent, false);
        }

        TextView name = view.findViewById(R.id.recipe_name);
        name.setText(recipe.getName());

        TextView ingredientCount = view.findViewById(R.id.recipe_ingredientCount);
        ingredientCount.setText("Numero de Ingrediente: " + String.valueOf(recipe.getIgredientCount()));

        TextView price = view.findViewById(R.id.recipe_price);
        price.setText("Preço: " + String.valueOf(recipe.getTotal()) + " R$");

        ImageView image = view.findViewById(R.id.recipe_image);
        String imagePath = recipe.getImagePath();
        if(imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmapReduced = Bitmap.createScaledBitmap(bitmap,100,100,true);
            image.setImageBitmap(bitmapReduced);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
        }


        return view;
    }
}

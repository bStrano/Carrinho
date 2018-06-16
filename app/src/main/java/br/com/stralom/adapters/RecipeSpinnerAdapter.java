package br.com.stralom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.entities.Recipe;

public class RecipeSpinnerAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private Context context;
    private LayoutInflater layoutInflater;

    public RecipeSpinnerAdapter(ArrayList<Recipe> recipes, Context context, LayoutInflater layoutInflater) {
        this.recipes = recipes;
        this.context = context;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return recipes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return recipes.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.spinner_item_recipe,viewGroup,false);
        TextView recipeName = view.findViewById(R.id.recipe_spinner_name);

        Recipe recipe = recipes.get(i);
        recipeName.setText(recipe.getName());


        return view;
    }
}

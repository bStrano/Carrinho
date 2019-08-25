package br.com.stralom.compras.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.stralom.compras.R;
import br.com.stralom.compras.entities.Recipe;

public class RecipeSpinnerAdapter extends BaseAdapter {
    private ArrayList<Recipe> recipes;
    private Activity activity;
    private LayoutInflater layoutInflater;

    public RecipeSpinnerAdapter(ArrayList<Recipe> recipes, Activity activity) {
        this.recipes = recipes;
        this.activity = activity;
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
        return -1;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = activity.getLayoutInflater().inflate(R.layout.spinner_item_recipe,viewGroup,false);
        TextView recipeName = view.findViewById(R.id.recipe_spinner_name);

        Recipe recipe = recipes.get(i);
        recipeName.setText(recipe.getName());


        return view;
    }
}

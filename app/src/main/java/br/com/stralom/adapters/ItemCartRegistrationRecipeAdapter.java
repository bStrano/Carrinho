package br.com.stralom.adapters;

import android.app.Activity;

import java.util.ArrayList;

import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

/**
 * Created by Bruno Strano on 14/07/2018.
 */
public class ItemCartRegistrationRecipeAdapter extends ItemCartRegistrationAdapter<Recipe>  {

    public ItemCartRegistrationRecipeAdapter(ArrayList<Recipe> list, Activity activity) {
        super(list, activity);
    }

    @Override
    public String getName(Recipe recipe) {
        return recipe.getName();
    }
}

package br.com.stralom.compras.UI.matchers;

import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import br.com.stralom.adapters.CartAdapter;
import br.com.stralom.adapters.RecipeAdapter;
import br.com.stralom.compras.R;
import br.com.stralom.entities.ItemRecipe;
import br.com.stralom.entities.Product;
import br.com.stralom.entities.Recipe;

public class RecipeMatcher {

    public static Matcher<RecyclerView.ViewHolder> withRecipeHolder(final String text) {
        return new BoundedMatcher<RecyclerView.ViewHolder, RecipeAdapter.RecipeViewHolder>(RecipeAdapter.RecipeViewHolder.class) {

            @Override
            protected boolean matchesSafely(RecipeAdapter.RecipeViewHolder item) {
                TextView name = item.itemView.findViewById(R.id.recipe_name);
                if(name == null) {
                    return false;
                }
                return name.getText().toString().matches(text);

            }

            @Override
            public void describeTo(Description description) {
                description.appendText("No ViewHolder found [" + text + "]");
            }

        };
    }

    public static Matcher<Object> recipeSpinnerWithText(final String recipeName) {
        return new BoundedMatcher<Object, Recipe>(Recipe.class) {

            @Override
            protected boolean matchesSafely(Recipe item) {
                return item.getName().equals(recipeName);
            }


            @Override
            public void describeTo(Description description) {
                description.appendText("Not found  [" + recipeName + "]");
            }
        };
    }


}
